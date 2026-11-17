// Small progressive enhancements. Every page still works without JS.
(function () {
    // Sort dropdown on /products submits the filter form right away.
    document.querySelectorAll('[data-autosubmit]').forEach(function (el) {
        el.addEventListener('change', function () {
            var form = el.form || el.closest('form');
            if (form) form.submit();
        });
    });

    // Example prompts on /finder fill the textarea.
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

    // Toasts fade out after a few seconds.
    var toast = document.querySelector('.toast');
    if (toast) {
        setTimeout(function () { toast.classList.add('is-hiding'); }, 3500);
        setTimeout(function () { toast.remove(); }, 4000);
    }
})();
