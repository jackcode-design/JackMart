package com.jack.jackmart.dao.jdbc;

import com.jack.jackmart.dao.ReviewDAO;
import com.jack.jackmart.model.Review;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class ReviewDAOImpl implements ReviewDAO {
    private final DataSource ds;
    public ReviewDAOImpl(DataSource ds){this.ds=ds;}

    public boolean canReview(long userId,long productId)throws Exception{
        String sql="SELECT 1 FROM orders o JOIN order_items oi ON oi.order_id=o.id WHERE o.buyer_id=? AND oi.product_id=? AND o.status='DELIVERED' LIMIT 1";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,userId);ps.setLong(2,productId);try(ResultSet rs=ps.executeQuery()){return rs.next();}
        }
    }

    public boolean exists(long userId,long productId)throws Exception{
        String sql="SELECT 1 FROM reviews WHERE user_id=? AND product_id=?";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,userId);ps.setLong(2,productId);try(ResultSet rs=ps.executeQuery()){return rs.next();}
        }
    }

    public long create(Review r)throws Exception{
        String sql="INSERT INTO reviews(product_id,user_id,rating,comment) VALUES(?,?,?,?)";
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setLong(1,r.getProductId());ps.setLong(2,r.getUserId());ps.setInt(3,r.getRating());ps.setString(4,r.getComment());ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){if(rs.next())return rs.getLong(1);}
        } throw new SQLException("Review id was not generated");
    }

    public List<Review> findByProduct(long productId)throws Exception{
        String sql="SELECT id,product_id,user_id,rating,comment,created_at FROM reviews WHERE product_id=? ORDER BY created_at DESC";
        List<Review> out=new ArrayList<>();
        try(Connection c=ds.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,productId);try(ResultSet rs=ps.executeQuery()){while(rs.next()){Review r=new Review();r.setId(rs.getLong(1));r.setProductId(rs.getLong(2));r.setUserId(rs.getLong(3));r.setRating(rs.getInt(4));r.setComment(rs.getString(5));r.setCreatedAt(rs.getTimestamp(6).toLocalDateTime());out.add(r);}}
        } return out;
    }
}
