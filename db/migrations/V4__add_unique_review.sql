CREATE UNIQUE INDEX IF NOT EXISTS uq_reviews_user_product ON reviews(user_id, product_id);
