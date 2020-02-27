package alexnickonovich.library.exceptions;

public class CountOfInstancesException extends RuntimeException {
    @Override
    public String getMessage() {
        return "All the books are rented";
    }
}
