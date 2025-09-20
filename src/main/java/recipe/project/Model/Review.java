package recipe.project.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "UserName is required.")
    private String userName;

    @Min(value = 0, message = "Rating Must be between 0 and 10")
    @Max(value = 10, message = "Rating must be between 0 and 10")
    private int rating;

    @NotBlank(message = "Review description is required")
    private String description;

    public void setRating(int rating){
        if (rating < 0 || rating > 10){
            throw new IllegalStateException("Rating must be between 0 and 10");
        }

        this.rating = rating;
    }
}
