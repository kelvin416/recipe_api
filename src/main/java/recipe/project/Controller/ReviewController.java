package recipe.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.project.Exceptions.NoSuchRecipeException;
import recipe.project.Exceptions.NoSuchReviewException;
import recipe.project.Model.Recipe;
import recipe.project.Model.Review;
import recipe.project.Service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable("id") Long id){
        try {
            Review retrieveReview = reviewService.getReviewById(id);
            return ResponseEntity.ok(retrieveReview);
        } catch (NoSuchReviewException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recipe/{reviewId}")
    public ResponseEntity<?> getReviewByRecipeId(@PathVariable("reviewId") Long recipeId){
        try {
            List<Review> reviews = reviewService.getReviewByRecipeId(recipeId);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getReviewByUserName(@PathVariable("username") String username){
        try {
            List<Review> reviews = reviewService.getReviewByUserName(username);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchReviewException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<?> postNewReview(@RequestBody Review review, @PathVariable("recipeId") Long recipeId){
        try {
            Recipe insertRecipe = reviewService.postNewReview(review, recipeId);
            return ResponseEntity.created(insertRecipe.getLocationURI()).body(insertRecipe);
        } catch (NoSuchRecipeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewById(@PathVariable("id") Long id){
        try {
            Review returnDeletedReview = reviewService.deleteReviewById(id);
            return ResponseEntity.ok(returnDeletedReview);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateReviewById(@RequestBody Review reviewToUpdate){
        try {
            Review updatedReview = reviewService.updateReviewById(reviewToUpdate);
            return ResponseEntity.ok(updatedReview);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
