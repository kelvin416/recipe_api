package recipe.project.Exceptions;

public class NoSuchReviewException extends Exception{

    public NoSuchReviewException(String message){
        super(message);
    }

    public NoSuchReviewException(){}
}
