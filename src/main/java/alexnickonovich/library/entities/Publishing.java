package alexnickonovich.library.entities;
import alexnickonovich.library.interfaces.ICopier;
import lombok.*;
import org.apache.log4j.Logger;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"title"})
@AllArgsConstructor
public class Publishing implements ICopier<Publishing>,Comparable<Publishing> {
    static final Logger logger=Logger.getLogger(Publishing.class);
    @NonNull private String title;
    private String country;
    private String address;
    private int index;
    private String email;
    @Override
    public void copy(Publishing item) {
        title=item.title;
        country=item.country;
        address=item.address;
        index=item.index;
        email=item.email;
        logger.info("Params in object of Publishing was coppied");
    }
    @Override
    public int compareTo(Publishing x) {
        return title.compareTo(x.title);
    }
}
