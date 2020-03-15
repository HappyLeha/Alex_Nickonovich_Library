package alexnickonovich.library.entities;
import alexnickonovich.library.interfaces.JsonSerializable;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"title"})
@NoArgsConstructor
@AllArgsConstructor
public class Publishing implements Comparable<Publishing>, JsonSerializable {
    private String country;
    @NonNull private String address;
    private Integer index;
    private String email;
    @Override
    public int compareTo(Publishing x) {
        return address.compareTo(x.address);
    }
}
