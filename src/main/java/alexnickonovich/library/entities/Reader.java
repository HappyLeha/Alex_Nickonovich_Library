package alexnickonovich.library.entities;
import lombok.*;
import org.apache.log4j.Logger;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Optional;
@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"firstName","lastName"})
@NoArgsConstructor
@AllArgsConstructor
public class Reader implements Comparable<Reader> {
    @NonNull private String firstName;
    @NonNull private String lastName;
    private Integer year;
    private String email;
    private String phone;
    private Blob image;
    @NonNull private Optional<Rent> rent;
    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public class Rent {
        @NonNull private String book;
        private LocalDate startDate;
        private LocalDate endDate;
        public Reader getReader() {
            return Reader.this;
        }
    }
    public void setRent(@NonNull  String book, LocalDate startDate, LocalDate endDate) {
        this.rent=Optional.ofNullable(this.new Rent(book,startDate,endDate));
    }
    @Override
    public int compareTo(Reader x) {
        return lastName.compareTo(x.lastName)!=0?lastName.compareTo(x.lastName):firstName.compareTo(x.firstName);
    }
}
