package recipe.project.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipe.project.Exceptions.NoSuchRecipeException;
import recipe.project.Exceptions.NoSuchReviewException;
import recipe.project.Model.Recipe;
import recipe.project.Repository.RecipeRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    RecipeRepo recipeRepo;

    @Transactional
    public Recipe createNewRecipe(Recipe recipe) throws IllegalStateException{
        recipe.validate();
        recipe = recipeRepo.save(recipe);
        recipe.generateLocationURI();
        return recipe;
    }

    public Recipe getRecipeById(Long id) throws NoSuchRecipeException {
        Optional<Recipe> recipeOptional = recipeRepo.findById(id);

        if (recipeOptional.isEmpty()){
            throw new NoSuchRecipeException("No recipe with ID " + id + " could be found");
        }

        Recipe recipe = recipeOptional.get();
        recipe.generateLocationURI();
        return recipe;
    }

    public List<Recipe> getRecipeByName(String name) throws NoSuchRecipeException{
        List<Recipe> matchingRecipes = recipeRepo.findByNameContainingIgnoreCase(name);

        if (matchingRecipes.isEmpty()){
            throw new NoSuchRecipeException("No recipes could be found with that name.");
        }

        return matchingRecipes;
    }

    public List<Recipe> getAllRecipes() throws NoSuchRecipeException{
        List<Recipe> recipes = recipeRepo.findAll();

        if (recipes.isEmpty()){
            throw new NoSuchRecipeException("There are no recipes: ( fell free to add one)");
        }

        return recipes;
    }

    public double getAverageRatingForRecipe(Long recipeId) throws NoSuchRecipeException{
        Recipe recipe = getRecipeById(recipeId);
        return recipe.getAverageRating();
    }

    public List<Recipe> getRecipesMinimumAverageRating(double minimumRating) throws NoSuchRecipeException{
        List<Recipe> allRecipes = recipeRepo.findAll();

        return allRecipes.stream().filter(recipe -> recipe.getAverageRating() >= minimumRating).toList();
    }

    public List<Recipe> searchRecipeByNameAndDifficulty(String name, int maximumDifficulty) throws NoSuchRecipeException{
        List<Recipe> recipes = recipeRepo.findAll();
        return recipes.stream()
                .filter(recipe -> recipe.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(recipe -> recipe.getDifficultyRating() < maximumDifficulty)
                .toList();
    }

    public List<Recipe> getRecipesByUserName(String userName){
        List<Recipe> recipes = recipeRepo.findByNameContainingIgnoreCase(userName);

        if (recipes.isEmpty()){
            throw new NoSuchRecipeException("No recipes found for user: " + userName);
        }

        return recipes;
    }

    @Transactional
    public Recipe deleteRecipeById(Long id) throws NoSuchRecipeException{
        try {
            Recipe recipe = getRecipeById(id);
            recipeRepo.deleteById(id);
            return recipe;
        } catch (NoSuchRecipeException e){
            throw new NoSuchRecipeException(e.getMessage() + " Could not delete.");
        }
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe, boolean forceIdCheck) throws NoSuchRecipeException{
        try {
            if (forceIdCheck){
                getRecipeById(recipe.getId());
            }

            recipe.validate();
            Recipe saveRecipe = recipeRepo.save(recipe);
            saveRecipe.generateLocationURI();
            return saveRecipe;
        } catch (NoSuchRecipeException e){
            throw new NoSuchRecipeException("The recipe you passed in did not have an ID found" +
                    " in the database. Double check that it is correct. Or maybe you meant to" +
                    "POST a recipe not PATCH one.");
        }
    }

}
