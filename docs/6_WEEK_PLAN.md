# JackMart — Week 6: Reviews + Validation
**Window:** Aug 31 – Sep 6, 2026

## Goal
Complete reviews/ratings and strengthen input/edge-case validation.

## Deliverables
- Buyer can review products from delivered orders.
- Rating is restricted to 1–5.
- A buyer cannot review a product they did not purchase.
- Duplicate review for the same buyer/product is rejected.
- Empty cart cannot be checked out.
- Quantity cannot exceed available stock.
- Public JSP output uses JSTL escaping.
- Manual edge-case test sheet updated.

## Suggested commits
1. `feat: add completed-order product reviews`
2. `fix: validate cart and review edge cases`
3. `test: add review and checkout validation tests`

## Definition of Done
Reviews are stored only for eligible purchases and all Week 3–6 acceptance tests pass.
