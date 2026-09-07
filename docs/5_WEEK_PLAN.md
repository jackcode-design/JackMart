# JackMart — Week 5: Search + Order Status
**Window:** Aug 24 – Aug 30, 2026

## Goal
Harden product search/filtering and add the order status workflow.

## Deliverables
- Keyword search.
- Category filter.
- Combined keyword + category filtering.
- Seller can view incoming orders.
- Seller can move an order through Pending → Confirmed → Shipped → Delivered.
- Invalid status transitions are rejected.
- Buyer can see current order status.

## Suggested commits
1. `feat: harden product search and category filters`
2. `feat: add seller order status workflow`
3. `test: cover valid and invalid order transitions`

## Definition of Done
Search works with empty filters and status changes follow the allowed workflow.
