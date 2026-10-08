-- V3__add_index_orders_status.sql : index on orders status (idempotent; already part of V1 for fresh installs).
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(order_status);
CREATE INDEX IF NOT EXISTS idx_orders_buyer ON orders(buyer_id);
CREATE INDEX IF NOT EXISTS idx_oi_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_oi_product ON order_items(product_id);
CREATE INDEX IF NOT EXISTS idx_cart_user ON cart_items(user_id);
CREATE INDEX IF NOT EXISTS idx_cart_product ON cart_items(product_id);
CREATE INDEX IF NOT EXISTS idx_products_seller ON products(seller_id);
