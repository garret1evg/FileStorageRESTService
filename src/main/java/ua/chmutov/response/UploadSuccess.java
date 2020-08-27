package ua.chmutov.response;

public class UploadSuccess implements ResponseInterface{
    private long id;

    public UploadSuccess() {
    }

    public UploadSuccess(long id ){
        this.id = id;
    }

    public long getId(){return id;}
}
