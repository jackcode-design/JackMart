package com.jack.jackmart.dao.jdbc;

import com.jack.jackmart.dao.SellerDAO;
import com.jack.jackmart.model.Order;
import com.jack.jackmart.model.Product;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class SellerDAOImpl implements SellerDAO {
    private final DataSource ds;
    public SellerDAOImpl(DataSource ds){this.ds=ds;}

    public long createProduct(Product p)throws Exception{
        String sql="INSERT INTO products(seller_id,name,description,price,stock_qty,category,image_url) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setLong(1,p.getSellerId()); ps.setString(2,p.getName()); ps.setString(3,p.getDescription());
            ps.setBigDecimal(4,p.getPrice()); ps.setInt(5,p.getStockQty()); ps.setString(6,p.getCategory()); ps.setString(7,p.getImageUrl());
            ps.executeUpdate(); try(ResultSet rs=ps.getGeneratedKeys()){if(rs.next())return rs.getLong(1);}
        }
        throw new SQLException("Product id was not generated");
    }

    public boolean updateProduct(Product p,long sellerId)throws Exception{
        String sql="UPDATE products SET name=?,description=?,price=?,stock_qty=?,category=?,image_url=? WHERE id=? AND seller_id=?";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,p.getName()); ps.setString(2,p.getDescription()); ps.setBigDecimal(3,p.getPrice());
            ps.setInt(4,p.getStockQty()); ps.setString(5,p.getCategory()); ps.setString(6,p.getImageUrl());
            ps.setLong(7,p.getId()); ps.setLong(8,sellerId); return ps.executeUpdate()==1;
        }
    }

    public boolean deleteProduct(long productId,long sellerId)throws Exception{
        String sql="DELETE FROM products WHERE id=? AND seller_id=?";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,productId); ps.setLong(2,sellerId); return ps.executeUpdate()==1;
        }
    }

    public List<Product> findProductsBySeller(long sellerId)throws Exception{
        String sql="SELECT id,seller_id,name,description,price,stock_qty,category,image_url,created_at FROM products WHERE seller_id=? ORDER BY id DESC";
        List<Product> out=new ArrayList<>();
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,sellerId); try(ResultSet rs=ps.executeQuery()){while(rs.next())out.add(mapProduct(rs));}
        } return out;
    }

    public List<Order> findIncomingOrders(long sellerId)throws Exception{
        String sql="SELECT DISTINCT o.id,o.buyer_id,o.status,o.total_amount,o.created_at FROM orders o JOIN order_items oi ON oi.order_id=o.id JOIN products p ON p.id=oi.product_id WHERE p.seller_id=? ORDER BY o.id DESC";
        List<Order> out=new ArrayList<>();
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,sellerId); try(ResultSet rs=ps.executeQuery()){while(rs.next())out.add(mapOrder(rs));}
        } return out;
    }

    public boolean updateOrderStatus(long orderId,long sellerId,String newStatus)throws Exception{
        String sql="UPDATE orders SET status=? WHERE id=? AND EXISTS (SELECT 1 FROM order_items oi JOIN products p ON p.id=oi.product_id WHERE oi.order_id=orders.id AND p.seller_id=?)";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,newStatus); ps.setLong(2,orderId); ps.setLong(3,sellerId);
            if(ps.executeUpdate()!=1)return false;
        }
        return true;
    }

    private Product mapProduct(ResultSet r)throws SQLException{
        Product p=new Product(); p.setId(r.getLong(1)); p.setSellerId(r.getLong(2)); p.setName(r.getString(3));
        p.setDescription(r.getString(4)); p.setPrice(r.getBigDecimal(5)); p.setStockQty(r.getInt(6)); p.setCategory(r.getString(7));
        p.setImageUrl(r.getString(8)); p.setCreatedAt(r.getTimestamp(9).toLocalDateTime()); return p;
    }
    private Order mapOrder(ResultSet r)throws SQLException{
        Order o=new Order(); o.setId(r.getLong(1)); o.setBuyerId(r.getLong(2)); o.setStatus(r.getString(3));
        o.setTotalAmount(r.getBigDecimal(4)); o.setCreatedAt(r.getTimestamp(5).toLocalDateTime()); return o;
    }
}
