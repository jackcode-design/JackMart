# Required Diagrams

## D1 — ER Diagram
```text
USERS 1---N PRODUCTS
USERS 1---N ORDERS
ORDERS 1---N ORDER_ITEMS
PRODUCTS 1---N ORDER_ITEMS
USERS 1---N CART_ITEMS
PRODUCTS 1---N CART_ITEMS
USERS 1---N REVIEWS
PRODUCTS 1---N REVIEWS
```

## D2 — Use Case
Actors: Buyer, Seller, Admin.
Buyer: register/login, browse/search, cart, checkout, order history.
Seller: manage listings, view incoming orders.
Admin: manage users/orders/listings.

## D3 — Place Order Sequence
```text
Browser -> OrderServlet -> OrderService -> OrderDAO -> H2
Browser <- OrderServlet <- OrderService <- OrderDAO <- H2
```
