package alexnickonovich.library.exceptions;

public class RestrictException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Attempt to violate data integrity";
    }
}
