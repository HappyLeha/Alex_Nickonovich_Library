package alexnickonovich.library.entities;
import alexnickonovich.library.enums.Gender;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"firstName","lastName"})
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Comparable<Author> {
    @NonNull private String firstName;
    @NonNull private String lastName;
    private Integer year;
    private Gender gender;
    private String note;
    @Override
    public int compareTo(Author x) {
        return lastName.compareTo(x.lastName)!=0?lastName.compareTo(x.lastName):firstName.compareTo(x.firstName);
    }
}
