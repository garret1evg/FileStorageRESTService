package ua.chmutov.responseClasses;

public class ErrorResponse implements ResponseInterface {
    private boolean success;
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
        success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
