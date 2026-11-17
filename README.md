# Sleigh List: frontend shell

Plain HTML, CSS and JS with sample content. There's no backend or database yet,
and nothing gets saved. Open `index.html` (or `login.html`) in a browser.

## Pages

| File | Page |
| --- | --- |
| `login.html`, `register.html` | Log in / create account |
| `forgot-password.html` → `check-email.html` → `reset-password.html` | Password reset by email (with a "Resend email" button) |
| `groups.html` | Your groups, create or join one |
| `group.html` | Group dashboard |
| `friend-list.html` | A member's list with claims |
| `friend-list-owner.html` | The same list as its owner sees it (claims hidden) |
| `lists.html` | My wish lists |
| `products.html` | Browse products |
| `finder.html` → `finder-results.html` | AI gift finder |
| `budget.html` | Budget tracker |
| `santa.html` → `santa-drawn.html` | Secret Santa setup, then reveal |

## How the buttons behave

- Forms with `data-go` move to the next page in the flow, e.g. log in goes to `groups.html`.
- Forms with `data-shell` show a "not connected yet" toast.
- Every form has a `data-endpoint` attribute naming the request the backend
  will need to handle, e.g. `POST /claims`.

## Environment

`.env.example` lists the keys the app needs (the Resend API key for the password
reset emails). Copy it to `.env` and fill it in. `.env` is gitignored.
