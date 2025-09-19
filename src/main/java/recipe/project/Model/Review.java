package recipe.project.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @NotNull
    private String userName;
    private int rating;

    @NotNull
    private String description;

    public void setRating(int rating){
        if (rating < 0 || rating > 10){
            throw new IllegalStateException("Rating must be between 0 and 10");
        }

        this.rating = rating;
    }
}
