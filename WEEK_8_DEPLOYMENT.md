# JackMart — Week 7 Security Checklist

| Check | Verification | Status |
|---|---|---|
| Parameterized SQL | Search JDBC code for `Statement` and verify data queries use `PreparedStatement` | [ ] |
| BCrypt | Inspect `PasswordUtil` and run `PasswordUtilTest` | [ ] |
| Session regeneration | Login invalidates the old session before creating a new one | [ ] |
| Session timeout | `web.xml` has 30-minute timeout; login also sets 1800 seconds | [ ] |
| Auth protection | `/cart/*`, `/checkout`, `/orders`, `/review`, `/seller/*`, `/admin/*` protected | [ ] |
| Role protection | Seller and Admin filters reject wrong roles | [ ] |
| XSS output | User-controlled JSP values use `<c:out>` | [ ] |
| Error handling | 404/500 go to `error.jsp` without stack traces | [ ] |
| Credentials | Real DB credentials are environment variables / ignored config | [ ] |
| Security headers | `SecurityHeadersFilter` is registered | [ ] |
| Request tracing | `RequestIdFilter` adds `X-Request-Id` and MDC | [ ] |

## Manual security tests

### SQL injection
Try an email such as:

```text
' OR '1'='1
```

The login must not authenticate as another user.

### XSS
Use a product/review string such as:

```text
<script>alert('x')</script>
```

The browser must render it as text rather than execute it.

### Authorization
1. Log in as BUYER.
2. Open `/seller`.
3. Expect HTTP 403.
4. Open `/admin`.
5. Expect HTTP 403.

### Authentication
1. Log out.
2. Open `/cart`, `/checkout`, `/orders`, and `/review`.
3. Expect a redirect to login.

Do not use real credentials or real payment details in test evidence.
