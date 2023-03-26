package com.aheproject.order_service.repostories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aheproject.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
