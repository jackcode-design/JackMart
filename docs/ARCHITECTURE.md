# Architecture

```text
Browser (JSP + CSS + vanilla JS)
          |
          v
       Filters
 AuthFilter / EncodingFilter
          |
          v
      Controllers
          |
          v
       Services
          |
          v
         DAOs
          |
          v
 HikariCP DataSource
          |
          v
       H2 Database
```

Controllers contain HTTP orchestration only. Business rules are in services. SQL is in DAO implementations.
