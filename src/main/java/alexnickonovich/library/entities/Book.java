package alexnickonovich.library.entities;
import alexnickonovich.library.interfaces.JsonSerializable;
import alexnickonovich.library.json.LocalDateDeserializer;
import alexnickonovich.library.json.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class Book implements Comparable<Book>, JsonSerializable {
    @NonNull private String title;
    private Integer year;
    @NonNull private Optional<String> publishing;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
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
