# JackMart architecture (matches actual implementation)
## Flow
Browser (JSP/JSTL + vanilla JS fetch) -> EncodingFilter -> RequestLoggingFilter (requestId/MDC) -> AuthFilter -> Servlets (thin HTTP orchestration) -> Services (validation/business rules) -> DAO interfaces -> Jdbc*DAO (all SQL, PreparedStatement only, try-with-resources) -> HikariCP (owned by AppContextListener) -> H2 (embedded local/test, server mode deploy).
## Packages (`com.js.jsmart`)
- `controller`: Register/Login/Logout, Product, Cart, Checkout, Order, Seller, Admin, Review, Chat, Health, BaseServlet
- `service`: AuthService, ProductService, CartService, OrderService, ReviewService, `service.chat`: ChatProvider, MockChatProvider, GeminiChatProvider, ChatService
- `dao`: UserDAO/ProductDAO/OrderDAO/CartDAO/ReviewDAO + Jdbc*DAO + DAOFactory (Factory pattern)
- `model`: User, Product, Order, OrderItem, CartItem, Review, Role, OrderStatus
- `dto`: ApiResponse (envelope factories = Builder-style), UserResponseDTO (no passwordHash), ProductDTO, RegisterRequest
- `filter`: EncodingFilter, RequestLoggingFilter (MDC requestId), AuthFilter (session + role checks)
- `listener`: AppContextListener (pool Singleton lifecycle + V1..V3 migrations + seed)
- `util`: ConfigUtil, DbUtil, PasswordUtil (BCrypt), ValidationUtil, JsonUtil (Gson), Flash
- `exception`: AppException, ValidationException, UnauthorizedException, ForbiddenException, NotFoundException
## Key decisions
- Single-transaction mock checkout with rollback; forward-only order status table (PENDING->CONFIRMED->SHIPPED->DELIVERED, CANCELLED from PENDING).
- Mock-first chatbot: offline-safe default, Gemini only when `ai.chatbot.provider=gemini` + `GEMINI_API_KEY` env set; per-session 10/min rate limit, 500-char cap, outbound timeout, fixed domain prompt, per-session repeat cache, degraded fallback never leaks traces.
- Migrations: numbered `db/migrations/V1..V3` run at startup in order; `db/schema.sql` + `db/seed.sql` kept; product catalog seed separate (`seed_products.sql`).
- Observability: `GET /api/v1/health` checks real DB (`SELECT 1`); Logback structured logs with MDC `requestId`; custom 404/500 JSPs.
## Diagrams
- `docs/diagrams/er.md`, `usecase.md`, `sequence-place-order.md` (Mermaid sources).
## Patterns
DAO, Front Controller (servlets + AuthFilter), Singleton/managed pool (AppContextListener + DbUtil), Factory (DAOFactory + ChatService provider switch), Strategy (ChatProvider / mock payment strategy in OrderService), Builder (ApiResponse.ok/fail factories + DTO construction).
