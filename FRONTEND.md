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
