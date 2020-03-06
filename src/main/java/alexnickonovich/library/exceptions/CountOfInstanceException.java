package alexnickonovich.library.exceptions;

public class CountOfInstanceException extends Exception {
    @Override
    public String getMessage() {
        return "There are more Instances than their count";
    }
}
