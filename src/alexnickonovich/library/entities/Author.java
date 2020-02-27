package alexnickonovich.library.entities;
import alexnickonovich.library.enums.Gender;
import alexnickonovich.library.interfaces.ICopier;
import lombok.*;
import org.apache.log4j.Logger;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"firstName","lastName"})
@AllArgsConstructor
public class Author implements ICopier<Author>,Comparable<Author> {
    private static final Logger logger=Logger.getLogger(Author.class);
    @NonNull private String firstName;
    @NonNull private String lastName;
    private int year;
    private Gender gender;
    @Override
    public void copy(Author item) {
        firstName=item.firstName;
        lastName=item.lastName;
        gender=item.gender;
        year=item.year;
        logger.info("Params in object of Author coppied");
    }
    @Override
    public int compareTo(Author x) {
        return lastName.compareTo(x.lastName)!=0?lastName.compareTo(x.lastName):firstName.compareTo(x.firstName);
    }
}
