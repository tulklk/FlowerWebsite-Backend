package com.SWP391_G5_EventFlowerExchange.LoginAPI.repository;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.EventFlowerPosting;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IEventFlowerPostingRepository extends JpaRepository<EventFlowerPosting, Integer> {
     void deleteByUser_userID(int userId);
     // Tìm kiếm theo title hoặc description có chứa từ khóa (không phân biệt chữ hoa/thường)
     @Query("SELECT e FROM EventFlowerPosting e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))")
     List<EventFlowerPosting> searchByTitle(@Param("title") String title);
     // Tìm kiếm theo khoảng giá
     @Query("SELECT e FROM EventFlowerPosting e WHERE e.price BETWEEN :minPrice AND :maxPrice")
     List<EventFlowerPosting> searchByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
     List<EventFlowerPosting> findByUser(User user);
     // Sắp xếp theo createdAt giảm dần (từ mới đến cũ)
     List<EventFlowerPosting> findAllByOrderByCreatedAtDesc();
     List<EventFlowerPosting> findByUser_UserID(int userID);
     List<EventFlowerPosting> findByStatusNot(String status, Sort sort);
}
