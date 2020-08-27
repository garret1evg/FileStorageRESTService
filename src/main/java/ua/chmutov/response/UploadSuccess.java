package ua.chmutov.response;
/**
 * класс для отправки id файла после добавления
 */
public class UploadSuccess implements ResponseInterface{
    private long id;

    public UploadSuccess() {
    }

    public UploadSuccess(long id ){
        this.id = id;
    }

    public long getId(){return id;}
}
