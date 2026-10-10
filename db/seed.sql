-- Seed data (kept separate from migrations). One idempotent MERGE per demo account,
-- safe to rerun. Placeholder hashes are for offline/manual seeding only; the running
-- application seeds real BCrypt hashes through AppContextListener when the table is empty.
-- Default demo accounts: admin@jackmart.local / Admin@123 | seller1@jackmart.local / Seller@123 | buyer1@jackmart.local / Buyer@123
MERGE INTO users (id, email, password_hash, full_name, user_role) KEY(id)
VALUES (1, 'admin@jackmart.local', '$2a$10$7EqYBGJ9hQA9k5p8p8p8puzxJxJxJxJxJxJxJxJxJxJxJxJxJxJx.', 'JackMart Admin', 'ADMIN');
MERGE INTO users (id, email, password_hash, full_name, user_role) KEY(id)
VALUES (2, 'seller1@jackmart.local', '$2a$10$7EqYBGJ9hQA9k5p8p8p8puzxJxJxJxJxJxJxJxJxJxJxJxJxJxJx.', 'Asha Seller', 'SELLER');
MERGE INTO users (id, email, password_hash, full_name, user_role) KEY(id)
VALUES (3, 'seller2@jackmart.local', '$2a$10$7EqYBGJ9hQA9k5p8p8p8puzxJxJxJxJxJxJxJxJxJxJxJxJxJxJx.', 'Ravi Seller', 'SELLER');
MERGE INTO users (id, email, password_hash, full_name, user_role) KEY(id)
VALUES (4, 'buyer1@jackmart.local', '$2a$10$7EqYBGJ9hQA9k5p8p8p8puzxJxJxJxJxJxJxJxJxJxJxJxJxJxJx.', 'Meera Buyer', 'BUYER');
MERGE INTO users (id, email, password_hash, full_name, user_role) KEY(id)
VALUES (5, 'buyer2@jackmart.local', '$2a$10$7EqYBGJ9hQA9k5p8p8p8puzxJxJxJxJxJxJxJxJxJxJxJxJxJxJx.', 'Arjun Buyer', 'BUYER');
