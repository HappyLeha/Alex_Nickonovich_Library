package alexnickonovich.library.entities;
import alexnickonovich.library.interfaces.JsonSerializable;
import alexnickonovich.library.json.LocalDateDeserializer;
import alexnickonovich.library.json.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Optional;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"firstName","lastName"})
@NoArgsConstructor
@AllArgsConstructor
public class Reader implements Comparable<Reader>, JsonSerializable {
    @NonNull private String firstName;
    @NonNull private String lastName;
    private Integer year;
    private String email;
    private String phone;
    private Blob image;
    @NonNull @Setter(AccessLevel.NONE) private Optional<Rent> rent;
    @Getter
    @ToString(exclude = {"reader"})
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Rent {
        @JsonIgnore
        @NonNull private Reader reader;
        @NonNull private String book;
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private LocalDate startDate;
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private LocalDate endDate;
    }
    public void setRent(@NonNull  String book, LocalDate startDate, LocalDate endDate) {
        this.rent=Optional.ofNullable(new Rent(this,book,startDate,endDate));
    }
    @Override
    public int compareTo(Reader x) {
        return lastName.compareTo(x.lastName)!=0?lastName.compareTo(x.lastName):firstName.compareTo(x.firstName);
    }
}
