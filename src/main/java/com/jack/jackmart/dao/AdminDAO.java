package com.jack.jackmart.dao;

import com.jack.jackmart.model.Order;
import com.jack.jackmart.model.Product;
import com.jack.jackmart.model.User;
import java.util.List;

public interface AdminDAO {
    List<User> findAllUsers() throws Exception;
    List<Order> findAllOrders() throws Exception;
    List<Product> findAllProducts() throws Exception;
    boolean deleteProduct(long productId) throws Exception;
}
