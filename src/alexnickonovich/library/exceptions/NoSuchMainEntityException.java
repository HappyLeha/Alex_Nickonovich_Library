package alexnickonovich.library.exceptions;

public class NoSuchMainEntityException extends RuntimeException {
    @Override
    public String getMessage() {
        return "There isn't such element in main table";
    }
}
