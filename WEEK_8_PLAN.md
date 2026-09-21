# JackMart — Week 7: Security + Test Coverage
**Window:** Sep 7 – Sep 13, 2026

## Goal
Complete the security checklist from the project specification and finish unit/DAO test coverage.

## Work completed in this package
1. Add `SecurityHeadersFilter` for browser security headers.
2. Add `RequestIdFilter` and MDC request correlation.
3. Harden `AuthFilter` and `RoleFilter` to use existing sessions only.
4. Protect the review endpoint with authentication.
5. Escape the remaining order/cart/review JSP values with JSTL.
6. Add password hashing tests.
7. Add authentication and role-filter tests.
8. Add H2 DAO integration tests, including a SQL-injection regression case.
9. Add review-service validation tests.
10. Document the security checklist and manual security tests.

## Suggested commits
1. `feat: harden servlet security filters`
2. `test: add auth role and dao coverage`
3. `docs: complete week 7 security checklist`

## Definition of Done
- All automated tests pass.
- All protected routes require a valid session.
- Queries remain parameterized.
- BCrypt is verified by tests.
- User-controlled JSP values are escaped.
- Security headers are present.
- No stack traces are shown by configured error pages.
