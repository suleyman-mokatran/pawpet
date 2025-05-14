package com.company.pawpet.Service;

import com.company.pawpet.Model.Review;
import com.company.pawpet.Repository.ReviewRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    AppUserService appUserService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    ProductService productService;

    @Autowired
    ServiceService serviceService;

    public Review addDoctorReview(int userId, int doctorId , Review review){
        Review newReview = new Review();
        newReview.setAppUser(appUserService.getUserById(userId).orElseThrow());
        newReview.setDoctor(doctorService.findById(doctorId).orElseThrow());
        newReview.setRating(review.getRating());
        newReview.setComment(review.getComment());
        newReview.setDate(LocalDateTime.now());
        reviewRepository.save(newReview);
        return newReview;
    }

    public Review addProductReview(int userId, int productId , Review review){
        Review newReview = new Review();
        newReview.setAppUser(appUserService.getUserById(userId).orElseThrow());
        newReview.setProduct(productService.getProductById(productId).orElseThrow());
        newReview.setRating(review.getRating());
        newReview.setComment(review.getComment());
        newReview.setDate(LocalDateTime.now());
        reviewRepository.save(newReview);
        return newReview;
    }

    public Review addServiceReview(int userId, int serviceId , Review review){
        Review newReview = new Review();
        newReview.setAppUser(appUserService.getUserById(userId).orElseThrow());
        newReview.setService(serviceService.getServiceById(serviceId));
        newReview.setRating(review.getRating());
        newReview.setComment(review.getComment());
        newReview.setDate(LocalDateTime.now());
        reviewRepository.save(newReview);
        return newReview;
    }

    public List<Review> getDoctorReviews(int doctorId){
        return reviewRepository.findByDoctorId(doctorId);
    }

    public List<Review> getProductReviews(int productId){
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getServiceReviews(int serviceId){
        return reviewRepository.findByServiceId(serviceId);
    }

    public Review getReviewById(int id){
        return reviewRepository.findById(id).orElseThrow();
    }

    public Review editReview(int reviewId, Review review){
        Review updatedReview = reviewRepository.findById(reviewId).orElseThrow();
       updatedReview.setRating(review.getRating());
        updatedReview.setComment(review.getComment());
        reviewRepository.save(updatedReview);
        return updatedReview;
    }

    public void deleteReview(int reviewId){
        reviewRepository.deleteById(reviewId);
    }

    public int doctorRatingAverage(int id){
        List<Integer> ratings = reviewRepository.findRatingByDoctorId(id);
        int sum = 0;
        int total=0;
        for(int rating : ratings){
            sum +=rating;
            total++;
        }
        return (sum/total);
    }

    public int productRatingAverage(int id){
        List<Integer> ratings = reviewRepository.findRatingByProductId(id);
        int sum = 0;
        int total=0;
        for(int rating : ratings){
            sum +=rating;
            total++;
        }
        return (sum/total);
    }

    public int serviceRatingAverage(int id){
        List<Integer> ratings = reviewRepository.findRatingByProductId(id);
        int sum = 0;
        int total=0;
        for(int rating : ratings){
            sum +=rating;
            total++;
        }
        return (sum/total);
    }
}
