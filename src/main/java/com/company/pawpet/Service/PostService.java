package com.company.pawpet.Service;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Model.Post;
import com.company.pawpet.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    AppUserService appUserService;

    public int addLostFoundPost(int userId,Post post){
        Post newpost = new Post();
        AppUser appUser = appUserService.getUserById(userId).orElseThrow();
        newpost.setPetId(post.getPetId());
        newpost.setContent(post.getContent());
        newpost.setImages(post.getImages());
        newpost.setAppUser(appUser);
        newpost.setCreatedAt(LocalDateTime.now());
        newpost.setType("Lost-Found");

        postRepository.save(newpost);
        return newpost.getPostId();
    }

    public List<Post> getAllPosts(){
        List<Post> posts = postRepository.findAllOrderByCreatedAtDesc();
        return posts;
    }

    public void deletePost(int id){
        postRepository.deleteById(id);
    }

    public List<Post> getUserPosts(int id){
        return postRepository.findPostsByUserId(id);
    }

    public Post getPostById(int id){
        return postRepository.findById(id).orElseThrow();
    }

    public Post updatePost(int id, Post post){
        Post newPost = postRepository.findById(id).orElseThrow();
        newPost.setImages(post.getImages());
        newPost.setContent(post.getContent());
        postRepository.save(newPost);
        return newPost;
    }

    public int addForAdoptionPost(int userId,Post post){
        Post newpost = new Post();
        AppUser appUser = appUserService.getUserById(userId).orElseThrow();
        newpost.setContent(post.getContent());
        newpost.setImages(post.getImages());
        newpost.setAppUser(appUser);
        newpost.setPetId(post.getPetId());
        newpost.setCreatedAt(LocalDateTime.now());
        newpost.setType("For Adoption");

        postRepository.save(newpost);
        return newpost.getPostId();
    }

    public int addPost(int userId,Post post){
        Post newpost = new Post();
        AppUser appUser = appUserService.getUserById(userId).orElseThrow();
        newpost.setContent(post.getContent());
        newpost.setImages(post.getImages());
        newpost.setAppUser(appUser);
        newpost.setCreatedAt(LocalDateTime.now());
        newpost.setType(post.getType());

        postRepository.save(newpost);
        return newpost.getPostId();
    }

    public int getNumberOfPostsForUser(int id){
        return postRepository.findNumberOfPosts(id);
    }


}
