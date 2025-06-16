package com.company.pawpet.Repository;

import com.company.pawpet.Model.Pet;
import com.company.pawpet.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post,Integer> {

    @Query(value = "SELECT * FROM post WHERE app_user_id = :appUserId", nativeQuery = true)
    List<Post> findPostsByUserId(@Param("appUserId") int appUserId);

    @Query(value = "SELECT * FROM post ORDER BY created_at DESC", nativeQuery = true)
    List<Post> findAllOrderByCreatedAtDesc();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post_images WHERE post_post_id IN (SELECT post_id FROM post WHERE pet_id = :petId)", nativeQuery = true)
    void deletePostImagesByPetId(@Param("petId") int petId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE pet_id = :petId", nativeQuery = true)
    void deletePostsByPetId(@Param("petId") int petId);

    @Query(value = "select count(*) from post where app_user_id = :userId",nativeQuery = true)
    Integer findNumberOfPosts(@Param("userId") int userId);

    @Query(value = "select count(*) from post ",nativeQuery = true)
    Integer numberOfPosts();


}
