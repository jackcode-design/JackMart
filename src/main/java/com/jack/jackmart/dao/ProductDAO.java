package com.jack.jackmart.dao;
import com.jack.jackmart.model.Product; import java.util.List;
public interface ProductDAO { List<Product> search(String q,String category) throws Exception; Product findById(long id) throws Exception; }