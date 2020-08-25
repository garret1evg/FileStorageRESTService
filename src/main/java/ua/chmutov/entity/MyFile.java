package ua.chmutov.entity;

public class MyFile {
    private  long id;
    private String name;
    private int size;
    private String[] tags;

    public MyFile(long id, String name, int size, String[] tags) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.tags = tags;
    }

    public MyFile(String name, int size) {

        this.name = name;
        this.size = size;
    }
}
