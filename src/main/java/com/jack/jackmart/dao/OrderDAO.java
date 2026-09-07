package com.jack.jackmart.dao;
import com.jack.jackmart.model.Order; import com.jack.jackmart.model.CartItem; import java.util.List;
public interface OrderDAO { long create(long buyerId, List<CartItem> items)throws Exception; List<Order> findByBuyer(long buyerId)throws Exception; }