package alexnickonovich.library.exceptions;

public class AlreadyExistEntityException extends RuntimeException {
    @Override
    public String getMessage() {
        return "This entity already exist";
    }
}
