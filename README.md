# JackMart

JackMart is an e-commerce marketplace built with Java Servlets, JSP and JDBC as a college capstone project. Buyers can browse products, use a cart, place orders and track them, while sellers can manage their products and orders.

## Live Demo

No public JackMart deployment exists yet. Deploy `target/jackmart.war` to Tomcat 9 per `docs/DEPLOYMENT.md`, then put the live URL here:

`https://<your-host>/jackmart/`

## Problem Statement

Small sellers need a simple Java storefront without framework lock-in: buyers browse/search/cart/checkout/track/review, sellers manage listings and incoming orders, admin moderates users/orders/listings. This capstone implements F1–F8 plus the mandatory shopping-assistant chatbot on Servlets/JSP/JDBC/Tomcat 9 with H2 + HikariCP.

## Features

* Buyer and seller registration/login
* Role-based access for buyer, seller and admin
* Product browsing and category filtering
* Product search and product details
* Shopping cart
* Checkout with mock payment flow
* Order history and order tracking
* Seller product management
* Seller order management
* Admin user/order management and moderation
* Product ratings and reviews
* Shopping assistant chatbot
* Success screens for important actions
* Security protections such as password hashing and session handling

## Tech Stack

* Java 17
* Servlets/JSP
* JDBC
* H2
* HikariCP
* Maven
* Tomcat 9
* JavaScript
* CSS
* Gson
* BCrypt
* JUnit 5
* Mockito

## How the Application Works

Buyer:
Browse → Search → View Product → Add to Cart → Checkout → View Orders → Track Order → Review

Seller:
Login → Manage Products → View Incoming Orders → Update Order Status

Admin:
Login → Manage Users/Orders → Moderate Products

## Project Structure

```
Browser
↓
Servlets / Controllers
↓
Services
↓
DAO
↓
HikariCP
↓
H2 Database
```

Main Java packages: `controller`, `service`, `dao`, `model`, `dto`, `filter`, `listener`, `util`, `exception`

## Running the Project

```bash
git clone <repository-url>
cd "JackMart"
mvn -B clean verify
```

This builds the project and runs all tests. It generates `target/jackmart.war`, which can be deployed to Tomcat 9 by copying it into the Tomcat `webapps` folder and starting the server.

Once running, open:

http://localhost:8080/jackmart/

Health check:

http://localhost:8080/jackmart/api/v1/health

## Testing

The latest verification has:

* Maven build successful
* 26/26 tests passing
* WAR generated successfully

Run everything with:

```bash
mvn -B clean verify
```

## Chatbot

JackMart includes a shopping/help chatbot with:

* Mock provider
* Optional Gemini provider
* Rate limiting
* Response caching
* Fallback response handling

The mock provider works out of the box with no setup. The Gemini provider is optional and needs its own API key configuration.

## Order Tracking

Buyers can follow their order progress through:

PENDING → CONFIRMED → SHIPPED → DELIVERED

An order can also be CANCELLED. Sellers and admins update the status, and the buyer tracking page always shows the current status from the database.

## Environment Variables

See `.env.example`. Never commit real secrets (`.env`, `config.properties`, `data/`, `*.db` are git-ignored).

| Variable | Purpose | Default |
|---|---|---|
| `DB_URL` / `db.url` | JDBC URL (embedded `./data/jackmart` local; `jdbc:h2:tcp://localhost/~/jackmart` server deploy; `jdbc:h2:mem:test` tests) | embedded |
| `DB_USER` / `DB_PASSWORD` | DB credentials | `sa` / empty |
| `AI_CHATBOT_PROVIDER` / `ai.chatbot.provider` | `mock` (offline-safe) or `gemini` | `mock` |
| `GEMINI_API_KEY` | Server-side only, env only, never in JSP/JS | empty (offline fallback) |
| `GEMINI_API_TIMEOUT_MS` | Outbound LLM timeout | `8000` |
| `SESSION_TIMEOUT_MINUTES` | HttpSession timeout | `30` |

## API Endpoints (`/api/v1/...`)

Fixed envelope `{success,data,error}` with correct statuses (200/201/400/401/403/404/409/500); DTOs separate from entities; `UserResponseDTO` never contains `passwordHash`.

| Method | Path | Auth |
|---|---|---|
| `GET` | `/api/v1/health` -> `{"status":"UP","db":"UP"}` (real `SELECT 1` check) | public |
| `POST` | `/api/v1/chat` `{message}` -> `{reply}` (`/api/chat` is a deprecated alias) | public (10/min per-session limit, 500-char cap, per-session repeat cache) |
| `GET/POST` | `/api/v1/cart/*`, `/api/v1/orders/*`, `/api/v1/checkout` | login via `AuthFilter` (401 JSON, 403 on role bypass) |

Page routes: `/products`, `/cart`, `/checkout`, `/orders/*`, `/seller/*` (SELLER/ADMIN), `/admin/*` (ADMIN only, server-side 403).

## Chatbot Setup

Default `mock` provider works offline with 10+ FAQ answers (search, cart, checkout, orders, seller listing, reviews, order status). For Gemini: set `AI_CHATBOT_PROVIDER=gemini` + `GEMINI_API_KEY` env on the server only; failures return a friendly offline fallback, never traces or keys. Widget: floating button + panel (`assets/js/chat.js`).

## Architecture Diagram

```
Browser -> EncodingFilter -> RequestLoggingFilter (MDC requestId) -> AuthFilter
  -> Servlets (thin) -> Services (validation/rules) -> DAO (PreparedStatement only)
  -> HikariCP (AppContextListener) -> H2
```

Details: `docs/ARCHITECTURE.md`. Diagrams (Mermaid sources): `docs/diagrams/er.md`, `docs/diagrams/usecase.md`, `docs/diagrams/sequence-place-order.md`. DB: `db/migrations/V1__init_schema.sql`, `V2__add_reviews_table.sql`, `V3__add_index_orders_status.sql`; canonical `db/schema.sql` + `db/seed.sql` (runtime copies in `src/main/resources/`). Tests: `docs/TEST_CASES.md` (+ `docs/MANUAL_TEST_SHEET.md`, `docs/LOAD_TEST.md`). Deploy: `docs/DEPLOYMENT.md`. Report: `docs/FINAL_REPORT.md`.

## Screenshots

Project screenshots can be added here.

## Deployment

Deployment documentation is available in `docs/DEPLOYMENT.md`, and the project includes a `Dockerfile` for container-based deployment.

## Known Limitations

* Payment is a mock flow.
* Gemini chatbot requires an API key if enabled.
* Some optional features were intentionally left out.

## Author

Jayashree B
