package com.jack.jackmart.model;
import java.math.BigDecimal; import java.time.LocalDateTime;
public class Order { private Long id,buyerId; private String status; private BigDecimal totalAmount; private LocalDateTime createdAt;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getBuyerId(){return buyerId;} public void setBuyerId(Long v){buyerId=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;} public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}