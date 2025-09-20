package recipe.project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.project.Exceptions.NoSuchRecipeException;
import recipe.project.Model.Recipe;
import recipe.project.Repository.RecipeRepo;
import recipe.project.Service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping
    public ResponseEntity<?> createNewRecipe(@RequestBody Recipe recipe){

        try {
            Recipe insertedRecipe = recipeService.createNewRecipe(recipe);
            return ResponseEntity.created(insertedRecipe.getLocationURI()).body(insertedRecipe);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable("id") Long id){
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecipes(){
        try {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getRecipeByName(@PathVariable("name") String name){
        try {
            List<Recipe> matchingRecipes = recipeService.getRecipeByName(name);
            return ResponseEntity.ok(matchingRecipes);
        } catch (NoSuchRecipeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable("id") Long id){
        try {
            Recipe deleteRecipe = recipeService.deleteRecipeById(id);
            return ResponseEntity.ok("The recipe with ID " + deleteRecipe.getId() +
                    " and name " + deleteRecipe.getName() + " was deleted!");
        } catch (NoSuchRecipeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping()
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe updateRecipe){
        try {
            Recipe returneUpdatedRecipe = recipeService.updateRecipe(updateRecipe, true);
            return ResponseEntity.ok(returneUpdatedRecipe);
        } catch (NoSuchRecipeException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/average-rating")
    public ResponseEntity<?> getAverageRating(@PathVariable("id") Long id){
        try {
            double averageRating = recipeService.getAverageRatingForRecipe(id);
            return ResponseEntity.ok("The average rating of recipe with Id "+ id + " is: " + averageRating);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("min-rating/{minimumRating}")
    public ResponseEntity<?> getRecipesByMinimumAverageRating(@PathVariable("minimumRating") Double minimumRating){
        try {
            List<Recipe> filterRecipes = recipeService.getRecipesMinimumAverageRating(minimumRating);
            return ResponseEntity.ok(filterRecipes);
        } catch (NoSuchRecipeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipes found with average rating >= " + minimumRating);
        }
    }

    @GetMapping("/search/{userName}")
    public ResponseEntity<?> getRecipesByUserName(@PathVariable("userName") String userName){
        try {
            List<Recipe> userRecipe = recipeService.getRecipeByName(userName);
            return ResponseEntity.ok(userRecipe);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
