package alexnickonovich.library.entities;
import alexnickonovich.library.interfaces.ICopier;
import lombok.*;
import org.apache.log4j.Logger;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeSet;
@Setter
@Getter
@ToString
@EqualsAndHashCode(of={"title"})
public class Book implements ICopier<Book>,Comparable<Book> {
    static final Logger logger=Logger.getLogger(Book.class);
    @NonNull private String title;
    @NonNull private int year;
    private Optional<String> publishing;
    private LocalDate date;
    private int countOfInstances;
    private Blob image;
    private ArrayList<String> authorsFirstNames;
    private ArrayList<String> authorsLastNames;

    public Book (@NonNull String title,@NonNull int year,String publishing,LocalDate date,int countOfInstances,Blob image,ArrayList<String> authorsFirstNames,ArrayList<String> authorsLastNames) {
        this.title=title;
        this.year=year;
        this.publishing=Optional.ofNullable(publishing);
        this.date=date;
        this.countOfInstances=countOfInstances;
        this.image=image;
        this.authorsFirstNames=authorsFirstNames==null?new ArrayList<>():new ArrayList<>(authorsFirstNames);
        this.authorsLastNames=authorsLastNames==null?new ArrayList<>():new ArrayList<>(authorsLastNames);
    }

    @Override
    public void copy(Book item) {

        title=item.title;
        year=item.year;
        publishing=item.publishing;
        date=item.date;
        image=item.image;
        authorsFirstNames=item.authorsFirstNames;
        authorsLastNames=item.authorsLastNames;
        logger.info("Params in object of Book was coppied");
    }
    @Override
    public int compareTo(Book x) {
        return title.compareTo(x.title);
    }

}
