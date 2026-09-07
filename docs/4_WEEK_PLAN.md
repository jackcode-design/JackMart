# JackMart — Week 4: Admin Panel
**Window:** Aug 17 – Aug 23, 2026

## Goal
Complete seller dashboard work and add the admin management panel.

## Deliverables
- Admin can view users.
- Admin can view all orders.
- Admin can view all product listings.
- Admin can remove a listing.
- Admin routes are protected by role checks.
- Admin actions are logged at application level.

## Suggested commits
1. `feat: add admin management dao`
2. `feat: add admin dashboard`
3. `test: protect admin routes and moderation actions`

## Definition of Done
Buyer/Seller accounts cannot access admin URLs, and listing removal is performed through the service/DAO layers.
