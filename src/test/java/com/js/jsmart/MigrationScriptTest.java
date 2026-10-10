package com.js.jsmart;

import com.js.jsmart.dao.JdbcProductDAO;
import com.js.jsmart.dao.ProductDAO;
import com.js.jsmart.listener.AppContextListener;
import com.js.jsmart.model.Product;
import com.js.jsmart.util.DbUtil;
import com.js.jsmart.util.SqlScriptRunner;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Migration, seeding and listing tests. Guards the Render failure where a
 * semicolon inside a V2 header comment split the script mid-comment and
 * aborted startup before any seed ran (empty product list).
 */
class MigrationScriptTest {
  private static final String[] MIGRATIONS =
      {"V1__init_schema.sql", "V2__add_reviews_table.sql", "V3__add_index_orders_status.sql"};

  @Test
  void semicolonInsideLineCommentDoesNotSplit() {
    String script = "-- V2 note (idempotent; already part of V1).\n"
        + "CREATE TABLE IF NOT EXISTS reviews (id BIGINT PRIMARY KEY);\n"
        + "CREATE INDEX IF NOT EXISTS idx_r ON reviews(id);";
    List<String> stmts = SqlScriptRunner.split(script);
    assertEquals(2, stmts.size());
    assertTrue(stmts.get(0).startsWith("CREATE TABLE"));
    assertTrue(stmts.get(1).startsWith("CREATE INDEX"));
  }

  @Test
  void semicolonInsideStringLiteralIsKept() {
    List<String> stmts = SqlScriptRunner.split(
        "INSERT INTO t (name) VALUES ('a;b');\nSELECT 1;");
    assertEquals(2, stmts.size());
    assertTrue(stmts.get(0).contains("'a;b'"));
  }

  @Test
  void blockCommentsAreStripped() {
    List<String> stmts = SqlScriptRunner.split(
        "/* setup; comment */\nCREATE TABLE t (id BIGINT PRIMARY KEY);");
    assertEquals(1, stmts.size());
    assertTrue(stmts.get(0).startsWith("CREATE TABLE"));
  }

  @Test
  void migrationFilesApplyTwiceWithoutError() throws Exception {
    HikariDataSource ds = memPool("jmigfiles");
    try {
      for (int run = 0; run < 2; run++) {
        try (Connection c = ds.getConnection()) {
          for (String name : MIGRATIONS) {
            SqlScriptRunner.execute(c, Files.readString(
                Path.of("db", "migrations", name), StandardCharsets.UTF_8));
          }
        }
      }
      try (Connection c = ds.getConnection()) {
        for (String table : new String[]{
            "USERS", "PRODUCTS", "ORDERS", "ORDER_ITEMS", "CART_ITEMS", "REVIEWS"}) {
          try (ResultSet rs = c.getMetaData().getTables(null, null, table, null)) {
            assertTrue(rs.next(), "table " + table + " must exist after migrations");
          }
        }
      }
    } finally {
      ds.close();
    }
  }

  @Test
  void productionMigrateAndSeedYieldsCatalogAndIsIdempotent() throws Exception {
    HikariDataSource ds = memPool("jmigseed");
    DbUtil.setDataSource(ds);
    try {
      AppContextListener.migrateAndSeed(ds);
      assertEquals(5L, count(ds, "users"), "demo users must be seeded");
      long products = count(ds, "products");
      assertTrue(products >= 12, "at least 12 products must be seeded, found " + products);
      ProductDAO dao = new JdbcProductDAO();
      List<Product> all = dao.findAll(100, 0);
      assertEquals(products, (long) all.size(), "All filter must list every product");
      assertFalse(dao.search("Fashion", null, 100, 0).isEmpty(),
          "category filter must return Fashion products");
      AppContextListener.migrateAndSeed(ds);
      assertEquals(5L, count(ds, "users"), "rerun must not duplicate users");
      assertEquals(products, count(ds, "products"), "rerun must not duplicate products");
    } finally {
      ds.close();
    }
  }

  @Test
  void seedScriptIsValidAndIdempotent() throws Exception {
    HikariDataSource ds = memPool("jseedscript");
    try {
      try (Connection c = ds.getConnection()) {
        SqlScriptRunner.execute(c, Files.readString(
            Path.of("db", "migrations", "V1__init_schema.sql"), StandardCharsets.UTF_8));
        String seed = Files.readString(Path.of("db", "seed.sql"), StandardCharsets.UTF_8);
        SqlScriptRunner.execute(c, seed);
        SqlScriptRunner.execute(c, seed);
      }
      assertEquals(5L, count(ds, "users"), "seed.sql must insert exactly 5 users");
    } finally {
      ds.close();
    }
  }

  private static HikariDataSource memPool(String name) {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl("jdbc:h2:mem:" + name + ";DB_CLOSE_DELAY=-1;MODE=LEGACY");
    cfg.setUsername("sa");
    cfg.setPassword("");
    cfg.setMaximumPoolSize(5);
    return new HikariDataSource(cfg);
  }

  private static long count(HikariDataSource ds, String table) throws Exception {
    try (Connection c = ds.getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
      rs.next();
      return rs.getLong(1);
    }
  }
}
