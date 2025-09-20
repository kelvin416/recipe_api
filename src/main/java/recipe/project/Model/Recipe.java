package recipe.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name of the recipe is required to make the recipe.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "UserName is required")
    @Column(nullable = false)
    private String userName;

    @NotNull(message = "Minutes to make is required.")
    @Column(nullable = false)
    private Integer minutesToMake;

    @Column(nullable = false)
    @NotNull(message = "Difficulty rating required")
    @Min(value = 0, message = "Rating must be between 0 and 10")
    @Max(value = 10, message = "Rating must be between 0 and 10")
    private Integer difficultyRating;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", nullable = false)
    @NotEmpty(message = "At least one ingredient is required")
    private Collection<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", nullable = false)
    @NotEmpty(message = "At least one ingredient is required")
    private Collection<Step> steps = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", nullable = false)
    private List<Review> reviews = new ArrayList<>();

    @Transient
    @JsonIgnore
    private URI locationURI;

    public void setDifficultyRating(int difficultyRating){
        if (difficultyRating < 0 || difficultyRating > 10){
            throw new IllegalStateException("Difficulty rating must be between 0 and 10");
        }

        this.difficultyRating = difficultyRating;
    }

    public void validate() throws IllegalStateException{
        if (ingredients.size() == 0){
            throw new IllegalStateException("You need at least one ingredient for your recipe");
        } else if (steps.size() == 0){
            throw new IllegalStateException("You need at least one step for your recipe");
        }
    }

    public void generateLocationURI(){
        try {
            locationURI = new URI(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/recipes/")
                            .path(String.valueOf(id))
                            .toUriString()
            );
        } catch (URISyntaxException e){

        }
    }

    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        } else {
            double sum = 0;
            for (Review review : reviews) {
                sum += review.getRating();
            }

            return Math.round((sum / reviews.size()) * 10.0) / 10.0;
        }

    }
}
