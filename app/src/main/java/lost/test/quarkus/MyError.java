package lost.test.quarkus;

public class MyError extends RuntimeException {
    public MyError(String message) {
        super(message);
    }

    public MyError(String message,
                   Throwable cause) {
        super(message, cause);
    }
}
