package recipe.project.Exceptions;

public class NoSuchRecipeException extends RuntimeException {
    public NoSuchRecipeException(String message) {
        super(message);
    }

    public NoSuchRecipeException(){}
}
