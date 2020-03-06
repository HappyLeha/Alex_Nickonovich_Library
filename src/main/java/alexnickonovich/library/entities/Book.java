package alexnickonovich.library.entities;
import lombok.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"title"})
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Comparable<Book> {
    @NonNull private String title;
    private Integer year;
    @NonNull private Optional<String> publishing;
    private LocalDate date;
    @NonNull  private Integer countOfInstances;
    private Blob image;
    @NonNull private Optional<ArrayList<String>> authorsFirstNames;
    @NonNull private Optional<ArrayList<String>> authorsLastNames;
    @NonNull private Optional<ArrayList<String>> readersFirstNames;
    @NonNull private Optional<ArrayList<String>> readersLastNames;
    @Override
    public int compareTo(Book x) {
        return title.compareTo(x.title);
    }
}
