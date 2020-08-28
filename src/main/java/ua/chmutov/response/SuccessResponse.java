package ua.chmutov.response;

public class SuccessResponse implements ResponseInterface{
    private boolean success;

    public SuccessResponse() {
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
