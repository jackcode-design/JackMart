package com.jack.jackmart.dao;

import com.jack.jackmart.model.Order;
import com.jack.jackmart.model.Product;
import java.util.List;

public interface SellerDAO {
    long createProduct(Product product) throws Exception;
    boolean updateProduct(Product product, long sellerId) throws Exception;
    boolean deleteProduct(long productId, long sellerId) throws Exception;
    List<Product> findProductsBySeller(long sellerId) throws Exception;
    List<Order> findIncomingOrders(long sellerId) throws Exception;
    boolean updateOrderStatus(long orderId, long sellerId, String newStatus) throws Exception;
}
