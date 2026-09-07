package com.jack.jackmart.dao.jdbc;
import com.jack.jackmart.dao.UserDAO; import com.jack.jackmart.model.User; import javax.sql.DataSource; import java.sql.*; import java.time.LocalDateTime; import java.util.*;
public class UserDAOImpl implements UserDAO {
 private final DataSource ds; public UserDAOImpl(DataSource ds){this.ds=ds;}
 public Optional<User> findByEmail(String email)throws Exception{String sql="SELECT id,name,email,password_hash,role,created_at FROM users WHERE email=?";
  try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,email);try(ResultSet r=p.executeQuery()){if(!r.next())return Optional.empty();return Optional.of(map(r));}}}
 public long create(User u)throws Exception{String sql="INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)";try(Connection c=ds.getConnection();PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){p.setString(1,u.getName());p.setString(2,u.getEmail());p.setString(3,u.getPasswordHash());p.setString(4,u.getRole());p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getLong(1);}}}
 private User map(ResultSet r)throws SQLException{return new User(r.getLong(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getTimestamp(6).toLocalDateTime());}
}