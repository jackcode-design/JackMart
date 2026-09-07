package com.jack.jackmart.dao.jdbc;

import com.jack.jackmart.dao.AdminDAO;
import com.jack.jackmart.model.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class AdminDAOImpl implements AdminDAO {
    private final DataSource ds;
    public AdminDAOImpl(DataSource ds){this.ds=ds;}

    public List<User> findAllUsers()throws Exception{
        String sql="SELECT id,name,email,password_hash,role,created_at FROM users ORDER BY id";
        List<User> out=new ArrayList<>();
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()){User u=new User();u.setId(rs.getLong(1));u.setName(rs.getString(2));u.setEmail(rs.getString(3));u.setPasswordHash(rs.getString(4));u.setRole(rs.getString(5));u.setCreatedAt(rs.getTimestamp(6).toLocalDateTime());out.add(u);}
        } return out;
    }

    public List<Order> findAllOrders()throws Exception{
        String sql="SELECT id,buyer_id,status,total_amount,created_at FROM orders ORDER BY id DESC";
        List<Order> out=new ArrayList<>();
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()){Order o=new Order();o.setId(rs.getLong(1));o.setBuyerId(rs.getLong(2));o.setStatus(rs.getString(3));o.setTotalAmount(rs.getBigDecimal(4));o.setCreatedAt(rs.getTimestamp(5).toLocalDateTime());out.add(o);}
        } return out;
    }

    public List<Product> findAllProducts()throws Exception{
        String sql="SELECT id,seller_id,name,description,price,stock_qty,category,image_url,created_at FROM products ORDER BY id DESC";
        List<Product> out=new ArrayList<>();
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()){Product p=new Product();p.setId(rs.getLong(1));p.setSellerId(rs.getLong(2));p.setName(rs.getString(3));p.setDescription(rs.getString(4));p.setPrice(rs.getBigDecimal(5));p.setStockQty(rs.getInt(6));p.setCategory(rs.getString(7));p.setImageUrl(rs.getString(8));p.setCreatedAt(rs.getTimestamp(9).toLocalDateTime());out.add(p);}
        } return out;
    }

    public boolean deleteProduct(long productId)throws Exception{
        String sql="DELETE FROM products WHERE id=?";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){ps.setLong(1,productId);return ps.executeUpdate()==1;}
    }
}
