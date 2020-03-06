package alexnickonovich.library.exceptions;

public class RestrictException extends Exception {
    @Override
    public String getMessage() {
        return "Attempt to violate data integrity";
    }
}
