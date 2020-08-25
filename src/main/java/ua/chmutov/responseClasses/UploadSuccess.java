package ua.chmutov.responseClasses;

public class UploadSuccess implements ResponseInterface{
    private long ID;

    public UploadSuccess(long id ){
        ID = id;
    }

    public String getID() {
        return "ID"+ID;
    }
}
