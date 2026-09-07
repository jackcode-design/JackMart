# JackMart — Weeks 3–6 Test Cases

## Seller
1. Login as seller.
2. Open `/seller`.
3. Add valid product → product appears.
4. Submit blank name → HTTP 400.
5. Submit negative price/stock → HTTP 400.
6. Delete own product → product disappears.
7. Attempt to modify/delete another seller's product → operation rejected.

## Admin
1. Login as admin.
2. Open `/admin` → users, products and orders are visible.
3. Login as buyer and open `/admin` → HTTP 403.
4. Remove a product from admin panel → listing is removed when no dependent rows block the operation.

## Order status
1. Seller sees an incoming PENDING order.
2. Change to CONFIRMED.
3. Change to SHIPPED.
4. Change to DELIVERED.
5. Attempt DELIVERED → SHIPPED → rejected.

## Reviews
1. Buyer with DELIVERED order opens review form.
2. Submit rating 1–5 → saved.
3. Submit rating 0 or 6 → rejected.
4. Buyer without delivered purchase attempts review → rejected.
5. Same buyer reviews same product twice → rejected.

## Health
1. GET `/api/v1/health`.
2. Expected successful response: `{"status":"UP","db":"UP"}`.
