package recipe.project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipe.project.Exceptions.NoSuchRecipeException;
import recipe.project.Exceptions.NoSuchReviewException;
import recipe.project.Model.Recipe;
import recipe.project.Model.Review;
import recipe.project.Repository.RecipeRepo;
import recipe.project.Repository.ReviewRepo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    RecipeService recipeService;

    public Review getReviewById(Long id) throws NoSuchReviewException{
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isEmpty()){
            throw new NoSuchReviewException("The review with ID " + id + " could not be found");
        }
        return review.get();
    }

    public List<Review> getReviewByRecipeId(Long recipeId)throws NoSuchRecipeException, NoSuchReviewException {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        List<Review> reviews = recipe.getReviews();

        if (reviews.isEmpty()){
            throw new NoSuchReviewException("There are no reviews for this recipe");
        }

        return reviews;
    }

    public List<Review> getReviewByUserName(String userName) throws NoSuchReviewException{
        List<Review> reviews = reviewRepo.findByUserName(userName);

        if (reviews.isEmpty()){
            throw new NoSuchReviewException("No reviews could be found for username" + userName);
        }

        return reviews;
    }

    public Recipe postNewReview(Review review, Long id){
        Recipe recipe = recipeService.getRecipeById(id);
        recipe.getReviews().add(review);
        recipeService.updateRecipe(recipe, false);
        return recipe;

    }

    public Review deleteReviewById(Long id) throws NoSuchReviewException {
        Review review = getReviewById(id);
        if (null == review) {
            throw new NoSuchReviewException("The review you are trying to delete does not exist");
        }

        reviewRepo.deleteById(id);
        return review;
    }

    public  Review updateReviewById(Review reviewToUpdate) throws NoSuchReviewException{
        try {
            Review review = getReviewById(reviewToUpdate.getId());
        } catch (NoSuchReviewException e){
            throw new NoSuchReviewException("The review you are trying to update is not available." +
                    "Maybe you meant to create one. If not, please double check the id you passed in.");
        }

        reviewRepo.save(reviewToUpdate);
        return reviewToUpdate;
    }
}
