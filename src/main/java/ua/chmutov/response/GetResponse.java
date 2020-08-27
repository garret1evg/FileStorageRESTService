package ua.chmutov.response;

import ua.chmutov.entity.MyFile;

public class GetResponse implements ResponseInterface {
    private int total;
    private MyFile[] page;

    public GetResponse() {
    }

    public GetResponse(int total, MyFile[] page) {
        this.total = total;
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public MyFile[] getPage() {
        return page;
    }

    public void setPage(MyFile[] page) {
        this.page = page;
    }
}
