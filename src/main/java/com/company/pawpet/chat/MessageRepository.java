package com.company.pawpet.chat;
import com.company.pawpet.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Long senderId1, Long receiverId1, Long senderId2, Long receiverId2);


    @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END " +
            "FROM Message m " +
            "WHERE m.receiverId = :userId OR m.senderId = :userId")
    List<Long> findDistinctUserIdsInvolvedWithDoctor(@Param("userId") Long doctorId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM message WHERE pet_id = :petId", nativeQuery = true)
    void deleteMessagesByPetId(@Param("petId") int petId);





}