# Sleigh List frontend

Spring Boot + Mustache, the same setup as Project 2. Every page renders from
**sample data** in `mock/MockData.java` for now. Nothing is saved, and no
database is connected yet.

## Run it

```
mvn spring-boot:run
```

Then open http://localhost:8081. Any username and password works on the login page.

## Pages (for queries.sql)

| URL | Page | Data it needs |
| --- | --- | --- |
| `/login`, `/register` | Log in / sign up | (auth) |
| `/groups` | Your groups, create a group, join by code | groups the user belongs to |
| `/groups/{groupId}` | Group dashboard | member stats, unclaimed counts, top categories, avg list value |
| `/groups/{groupId}/members/{userId}` | Friend's list (claim view) | list items + claim status, hidden when viewer = owner |
| `/groups/{groupId}/members/{userId}?as=owner` | Preview what the owner sees (demo of the spoiler rule) | same, claims stripped |
| `/groups/{groupId}/santa` | Secret Santa setup (admin) or reveal | eligibility, exclusions, your assignment |
| `/lists`, `/lists/{listId}` | My wish lists | user's lists and items |
| `/products` | Product browser (`q`, `category`, `min`, `max`, `rating`, `bestseller`, `sort`) | product search |
| `/finder?q=...` | AI gift finder | Gemini filters, then the same product search |
| `/budget` | Budget tracker | claims totaled per recipient vs budget |

## Requirement coverage (frontend side)

| Requirement | Where it shows up in the UI |
| --- | --- |
| At least 4 data-driven pages, not counting login/register | 7: groups, group dashboard, friend's list, my lists, browse, gift finder, budget, plus Secret Santa |
| Signup and login | `/register`, `/login` (error states: `/login?error`, `/register?error=taken` or `=mismatch`), log out in the nav |
| Retrieve | product search with category, price range, min rating, bestseller, sort, and paging (`/products?page=2`) |
| Insert | add to list, claim ("I'm buying this"), create list, create group, join group, add Secret Santa exclusion |
| Update | priority and note on a list item, mark a claim bought, change budget, run the draw |
| Delete | remove list item, unclaim, leave group, remove exclusion |
| Non-trivial query 1: spoiler-protected list | `/groups/{id}/members/{userId}`. Claims show for everyone except the owner; `?as=owner` demos the owner's view |
| Non-trivial query 2: budget totals per recipient | `/budget` |
| Non-trivial query 3: group stats | `/groups/{id}`: unclaimed per member, top categories, average list value |
| Bonus: Secret Santa eligibility | `/groups/{id}/santa`: "Who can draw whom" with counts |
| Table with 1000+ rows | products. The browser shows the catalog size and pages through results 9 at a time |
| Index on products by category and price | the `/products` category and price filters are exactly that query |
| Index on claims by item or claimer | used by the friend's list and budget pages |
| AI integration (Gemini) | `/finder`: a sentence becomes filters ("How we read that"), then a normal product search |
| AI stretch: natural-language list building | "Add by describing it" on `/lists` opens the finder aimed at that list |
| Dataset columns (title, URL, image, stars, reviews, price, list price, bestseller, category) | product cards show all of them. `imageUrl` falls back to a coloured tile and `productUrl` to a search link when null |
| Secret Santa: only see who *you* got | the reveal card shows only your own match. The organizer sees counts and exclusions, never the pairs |

## Form endpoints the templates already post to

These live in `controllers/PrototypeActionController.java` and just redirect
back with a toast. Move each one into a real controller as you implement it.

| Method + URL | Form fields |
| --- | --- |
| `POST /login` | `username`, `password` |
| `POST /register` | `firstName`, `lastName`, `username`, `password`, `passwordRepeat` |
| `POST /groups` | `name`, `occasion`, `exchangeDate` |
| `POST /groups/join` | `inviteCode` |
| `POST /groups/{groupId}/leave` | none |
| `POST /lists` | `name`, `groupId` |
| `POST /lists/items` | `listId`, `productId` |
| `POST /lists/{listId}/items/{itemId}` | `priority` (`must` / `nice`), `note` |
| `POST /lists/{listId}/items/{itemId}/delete` | none |
| `POST /claims` | `itemId` |
| `POST /claims/{claimId}/purchased` | none |
| `POST /claims/{claimId}/delete` | none |
| `POST /budget` | `amount` |
| `POST /groups/{groupId}/santa/exclusions` | `memberA`, `memberB`, `reason` |
| `POST /groups/{groupId}/santa/exclusions/{exclusionId}/delete` | none |
| `POST /groups/{groupId}/santa/draw` | none |
| `POST /groups/{groupId}/santa/reset` | none |

## Hooking up real data

The view models are in `models/Models.java` (`Product`, `WishList`, `ClaimTag`,
`MemberStat`, `RecipientBudget`, ...). If your service methods return those same
records, the templates won't need changes. Swap the `MockData.xyz()` calls in
each controller for your service calls.

## Files

- `src/main/resources/templates/*.mustache`: pages; shared pieces in `partials/`
- `src/main/resources/static/css/app.css`: all styles
- `src/main/resources/static/js/app.js`: small enhancements (sort auto-submit, finder examples, Secret Santa reveal, toasts)
