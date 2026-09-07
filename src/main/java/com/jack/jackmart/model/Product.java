package com.jack.jackmart.model;
import java.math.BigDecimal; import java.time.LocalDateTime;
public class Product {
 private Long id,sellerId; private String name,description,category,imageUrl; private BigDecimal price; private int stockQty; private LocalDateTime createdAt;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getSellerId(){return sellerId;} public void setSellerId(Long v){sellerId=v;}
 public String getName(){return name;} public void setName(String v){name=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;} public int getStockQty(){return stockQty;} public void setStockQty(int v){stockQty=v;}
 public String getCategory(){return category;} public void setCategory(String v){category=v;} public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}