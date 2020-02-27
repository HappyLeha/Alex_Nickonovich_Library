package alexnickonovich.library.entities;

import alexnickonovich.library.interfaces.ICopier;
import lombok.*;
import org.apache.log4j.Logger;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.Optional;
@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"firstName","lastName"})
public class Reader implements ICopier<Reader>,Comparable<Reader> {
    static final Logger logger=Logger.getLogger(Reader.class);
    @NonNull private String firstName;
    @NonNull private String lastName;
    private int year;
    @NonNull private String email;
    private String phone;
    private Blob image;
    private Optional<Rent> rent;
    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public class Rent {
        private String book;
        private final LocalDate startDate;
        private LocalDate endDate;
        public Reader getReader() {
            return Reader.this;
        }

    }
    public Reader(@NonNull String firstName,@NonNull String lastName,int year,@NonNull String email,String phone,Blob image) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.year=year;
        this.email=email;
        this.phone=phone;
        this.image=image;
        rent=Optional.empty();
    }
    @Override
    public void copy(Reader item) {
        firstName=item.firstName;
        lastName=item.lastName;
        year=item.year;
        email=item.email;
        phone=item.phone;
        image=item.image;
        logger.info("Params in object of Reader was coppied");
    }
    @Override
    public int compareTo(Reader x) {
        return lastName.compareTo(x.lastName)!=0?lastName.compareTo(x.lastName):firstName.compareTo(x.firstName);
    }

}
