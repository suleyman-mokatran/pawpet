package com.company.pawpet.Repository;

import com.company.pawpet.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(value = "SELECT * FROM notifications WHERE app_user_id = :userId ORDER BY created_at DESC", nativeQuery = true)
    List<Notification> findNotificationsByUserId(@Param("userId") int userId);

    @Query(value = "SELECT COUNT(*) FROM notifications WHERE app_user_id = :userId AND is_read = false", nativeQuery = true)
    int countUnreadByUserId(@Param("userId") int userId);

}

