package com.SWP391_G5_EventFlowerExchange.LoginAPI.repository;



import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Order;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByStatus(String status);
    List<Order> findByUser(User user);
}

