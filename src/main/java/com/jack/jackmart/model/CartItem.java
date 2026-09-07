package com.jack.jackmart.model;
import java.math.BigDecimal;
public class CartItem {
 private Long id,userId,productId; private String productName; private int quantity; private BigDecimal unitPrice;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;} public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
 public String getProductName(){return productName;} public void setProductName(String v){productName=v;} public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
 public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;} public BigDecimal getSubtotal(){return unitPrice.multiply(BigDecimal.valueOf(quantity));}
}