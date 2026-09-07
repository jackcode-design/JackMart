# JackMart — 2-Week MVP

Multi-seller e-commerce marketplace built for the Anna University R2025 Semester 3 Java capstone.

## 2-week scope
- Week 1: project skeleton, Tomcat/Maven structure, H2 schema, registration/login/session, base DAO layer.
- Week 2: product browsing/search, cart add/update/remove, mock checkout, buyer order history.
- MVP target: one complete end-to-end journey: register/login → browse → cart → place order.

## Stack
JDK 17, Maven, Tomcat 9, Servlet API (`javax.servlet`), JSP/JSTL, H2, HikariCP, Gson, jBCrypt, JUnit 5, Mockito, SLF4J/Logback.

## Package
`com.jack.jackmart`

## Run
1. Install JDK 17 and Maven.
2. Run `mvn clean verify`.
3. Deploy `target/jackmart.war` to Tomcat 9.
4. Open `http://localhost:8080/jackmart/`.
5. Seed data is loaded by `DatabaseInitializer`.

For local development, the database uses H2 embedded mode. Deployment can be switched to H2 server mode through environment variables.

## Demo accounts
- Admin: `admin@jackmart.local` / `Admin@123`
- Seller: `seller@jackmart.local` / `Seller@123`

Register a buyer from the UI.

## 2-week acceptance journey
Register → login → browse products → add product to cart → review cart → mock checkout → view order history.

## Security notes
- PreparedStatement for SQL.
- BCrypt password hashing.
- HttpSession with timeout and session ID regeneration on login.
- JSTL output escaping.
- H2 credentials are environment-driven.
- No real payment gateway is used.
