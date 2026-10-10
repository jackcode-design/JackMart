package com.js.jsmart.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal SQL script splitter and executor for migration files.
 * The previous naive {@code split(";")} broke on semicolons inside
 * {@code --} comments, aborting startup migrations. This splitter strips
 * {@code --} line comments and {@code /* *\/} block comments and respects
 * single-quoted string literals (including {@code ''} escapes), splitting
 * only on semicolons that terminate a real statement.
 */
public final class SqlScriptRunner {
  private SqlScriptRunner() { }

  /**
   * Split a SQL script into executable statements.
   * @param script full script text with comments
   * @return non-empty trimmed statements without trailing semicolons
   */
  public static List<String> split(String script) {
    List<String> out = new ArrayList<>();
    StringBuilder cur = new StringBuilder();
    boolean inString = false;
    boolean inBlock = false;
    int i = 0;
    while (i < script.length()) {
      char c = script.charAt(i);
      char next = i + 1 < script.length() ? script.charAt(i + 1) : 0;
      if (inBlock) {
        if (c == '*' && next == '/') {
          inBlock = false;
          i += 2;
        } else {
          i++;
        }
      } else if (inString) {
        cur.append(c);
        if (c == '\'') {
          if (next == '\'') {
            cur.append(next);
            i += 2;
          } else {
            inString = false;
            i++;
          }
        } else {
          i++;
        }
      } else if (c == '-' && next == '-') {
        while (i < script.length() && script.charAt(i) != '\n') {
          i++;
        }
      } else if (c == '/' && next == '*') {
        inBlock = true;
        i += 2;
      } else if (c == '\'') {
        inString = true;
        cur.append(c);
        i++;
      } else if (c == ';') {
        String stmt = cur.toString().trim();
        if (!stmt.isEmpty()) {
          out.add(stmt);
        }
        cur.setLength(0);
        i++;
      } else {
        cur.append(c);
        i++;
      }
    }
    String tail = cur.toString().trim();
    if (!tail.isEmpty()) {
      out.add(tail);
    }
    return out;
  }

  /**
   * Execute every statement of a migration script on one connection.
   * @param con open connection
   * @param script full script text
   * @throws SQLException if any statement fails
   */
  public static void execute(Connection con, String script) throws SQLException {
    try (Statement st = con.createStatement()) {
      for (String stmt : split(script)) {
        st.execute(stmt);
      }
    }
  }
}
