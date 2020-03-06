package alexnickonovich.library.exceptions;

public class AlreadyExistEntityException extends Exception {
    @Override
    public String getMessage() {
        return "This entity already exist";
    }
}
