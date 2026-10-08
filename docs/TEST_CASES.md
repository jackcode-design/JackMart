# Test cases (manual sheet mirrors automated coverage)
## Automated (`mvn -B clean verify`)
- DAO (embedded H2 `jdbc:h2:mem:test`): UserDAO CRUD + unique email; ProductDAO CRUD + search/filter.
- Service (Mockito DAO mocks): AuthService register/login validation; cart/order/review business rules.
- Servlet/security (Mockito req/resp): HealthServlet 200 + DB check; AuthFilter 401 unauthenticated, 403 role bypass, pass-through for correct role; XSS payload escaped via `c:out`.
- Catalog seed: 50-product seed loads.
## Manual end-to-end (register -> login -> browse -> cart -> checkout -> order -> review)
1. Register buyer -> login -> session ID changes -> browse `/products` -> search `?q=phone` + `?category=Fashion` filters.
2. Add to cart -> update qty -> running total correct -> checkout mock payment -> order PENDING.
3. My Orders shows order; Track page shows status; seller login sees incoming order; seller CONFIRMED->SHIPPED; buyer sees SHIPPED.
4. Admin marks DELIVERED; buyer posts 5-star review; duplicate review rejected; seller cannot review own pending product.
5. Admin `/admin` lists users/orders; deletes listing; buyer gets 404 on removed product.
6. Chat widget: ask "how to add to cart", "checkout", "track order", "seller listing", "reviews" -> domain answers; 11th message in 60s -> rate-limit notice; Gemini key absent -> offline fallback, no trace.
7. Health: `GET /api/v1/health` -> `{"status":"UP","db":"UP"}` (DB down -> 500 envelope).
## Security checklist
- `grep -rn "Statement" src/main/java` -> only PreparedStatement (one Statement in listener for migration bootstrap executing static files, no user input).
- Passwords BCrypt-hashed, never logged; API key only from env; DB creds env-driven + git-ignored.
- Protected routes via AuthFilter (server-side, not UI-only); session fixation: invalidate + new session on login; 30-min timeout.
- Error pages without traces; file upload N/A (image URL validated as http/https, length-capped).
## Load (docs/LOAD_TEST.md)
- Minimum 10 concurrent users / 60s against `/` + `/api/v1/health` via JMeter/`ab`; record p95 + errors.
