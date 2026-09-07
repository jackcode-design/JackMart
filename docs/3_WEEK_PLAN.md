# JackMart — Week 3: Seller Dashboard
**Window:** Aug 10 – Aug 16, 2026

## Goal
Implement seller-side product listing management while preserving the layered MVC architecture.

## Deliverables
- Seller can create a product.
- Seller can edit only their own products.
- Seller can delete only their own products.
- Seller can view incoming orders for their products.
- Validation rejects blank names, negative prices and negative stock.
- Controllers contain HTTP orchestration only; SQL remains in DAO classes.

## Suggested commits
1. `feat: add seller product CRUD dao and service`
2. `feat: add seller dashboard and product forms`
3. `test: validate seller ownership and product input`

## Definition of Done
Seller CRUD works against H2, unauthorized users are rejected, and all SQL uses PreparedStatement.
