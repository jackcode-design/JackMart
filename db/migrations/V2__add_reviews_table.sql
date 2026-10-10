-- V2__add_reviews_table.sql : standalone reviews table for existing databases.
-- Fresh installs already get this table from V1. Never hand-edit a live table.
CREATE TABLE IF NOT EXISTS reviews (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  rating INT NOT NULL,
  comment VARCHAR(2000),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_rev_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  CONSTRAINT fk_rev_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT chk_rating CHECK (rating BETWEEN 1 AND 5),
  CONSTRAINT uq_review UNIQUE (product_id, user_id)
);
CREATE INDEX IF NOT EXISTS idx_rev_product ON reviews(product_id);
CREATE INDEX IF NOT EXISTS idx_rev_user ON reviews(user_id);
