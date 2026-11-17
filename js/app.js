// Sleigh List frontend shell.
// No backend yet: forms either move to the next page (data-go) or show a toast
// saying what they will do once connected (data-shell). Each form's
// data-endpoint names the request the backend will need to handle.
(function () {
    function toast(message) {
        var old = document.querySelector('.toast');
        if (old) old.remove();
        var el = document.createElement('div');
        el.className = 'toast';
        el.setAttribute('role', 'status');
        el.textContent = message;
        document.body.appendChild(el);
        setTimeout(function () { el.classList.add('is-hiding'); }, 3000);
        setTimeout(function () { el.remove(); }, 3500);
    }

    function passwordsMatch(form) {
        var a = form.querySelector('[name="password"]');
        var b = form.querySelector('[name="passwordRepeat"]');
        var msg = form.querySelector('[data-mismatch]');
        var ok = !a || !b || a.value === b.value;
        if (msg) msg.hidden = ok;
        if (!ok && b) b.focus();
        return ok;
    }

    document.querySelectorAll('form[data-go], form[data-shell]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            if (form.hasAttribute('data-match-passwords') && !passwordsMatch(form)) return;
            if (form.dataset.shell) {
                toast(form.dataset.shell + ': not connected yet');
                return;
            }
            var next = form.dataset.go;
            var carry = form.dataset.carry;
            if (carry) {
                var field = form.querySelector('[name="' + carry + '"]');
                if (field && field.value) next += '?' + carry + '=' + encodeURIComponent(field.value);
            }
            window.location.href = next;
        });
    });

    // Query-string notices, e.g. login.html?reset or login.html?error
    var params = new URLSearchParams(window.location.search);
    document.querySelectorAll('[data-query-notice]').forEach(function (el) {
        if (params.has(el.dataset.queryNotice)) el.hidden = false;
    });

    // check-email.html: show the address and handle "Resend email" with a cooldown.
    var email = params.get('email');
    document.querySelectorAll('[data-email]').forEach(function (el) {
        if (email) el.textContent = email;
    });
    document.querySelectorAll('form[data-resend]').forEach(function (form) {
        var btn = form.querySelector('button');
        var status = document.querySelector('[data-resend-status]');
        var seconds = parseInt(form.dataset.cooldown || '60', 10);
        var label = btn.textContent;
        var timer = null;

        function startCooldown() {
            var left = seconds;
            btn.disabled = true;
            btn.textContent = 'Resend in ' + left + 's';
            timer = setInterval(function () {
                left -= 1;
                if (left <= 0) {
                    clearInterval(timer);
                    btn.disabled = false;
                    btn.textContent = label;
                } else {
                    btn.textContent = 'Resend in ' + left + 's';
                }
            }, 1000);
        }

        form.addEventListener('submit', function (e) {
            e.preventDefault();
            if (btn.disabled) return;
            if (status) status.textContent = 'Sent again. (Shell preview: no email actually goes out yet.)';
            startCooldown();
        });
        startCooldown(); // the first email was just sent
    });

    // Sort dropdown on products.html submits the filter form right away.
    document.querySelectorAll('[data-autosubmit]').forEach(function (el) {
        el.addEventListener('change', function () {
            var form = el.form || el.closest('form');
            if (form) form.submit();
        });
    });

    // Example prompts on the gift finder fill the textarea.
    var finder = document.getElementById('finder-q');
    document.querySelectorAll('[data-example]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            if (!finder) return;
            finder.value = btn.getAttribute('data-example');
            finder.focus();
        });
    });

    // Secret Santa reveal.
    document.querySelectorAll('[data-reveal]').forEach(function (box) {
        var btn = box.querySelector('[data-reveal-btn]');
        var body = box.querySelector('.reveal-body');
        if (!btn || !body) return;
        btn.addEventListener('click', function () {
            btn.setAttribute('aria-expanded', 'true');
            btn.hidden = true;
            body.hidden = false;
            box.classList.add('is-open');
            var link = body.querySelector('a');
            if (link) link.focus();
        });
    });
})();
