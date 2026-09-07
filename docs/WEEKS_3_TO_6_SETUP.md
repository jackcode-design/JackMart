# Weeks 3–6 Setup

These files extend the existing two-week JackMart package.

1. Keep the existing `JackMart` directory.
2. Copy the Week 3–6 files over it.
3. Run the database migrations in numerical order:
   - `V2__add_reviews_table.sql`
   - `V3__add_index_orders_status.sql`
   - `V4__add_unique_review.sql`
4. Build with Maven:
   `mvn clean verify`
5. Deploy `target/jackmart.war` to Tomcat 9.
6. Test `/seller`, `/admin`, `/orders`, `/review`, and `/api/v1/health`.

The existing specification requires JDK 17, Maven, Tomcat 9.0.x, H2, HikariCP, JSP/JSTL, JUnit 5 and Mockito. It also requires parameterized SQL, BCrypt password hashing, HttpSession authentication and try-with-resources.
