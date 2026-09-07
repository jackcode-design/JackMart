package com.jack.jackmart.dao;

import com.jack.jackmart.model.Review;
import java.util.List;

public interface ReviewDAO {
    boolean canReview(long userId, long productId) throws Exception;
    boolean exists(long userId, long productId) throws Exception;
    long create(Review review) throws Exception;
    List<Review> findByProduct(long productId) throws Exception;
}
