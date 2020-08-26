package ua.chmutov.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;


@Document(indexName = "file-storage", indexStoreType = "default" )
public class MyFile {

    @Id
    private long id;
    private String name;
    private int size;
    @Field(type = Keyword)
    private String[] tags;

    public MyFile(long id, String name, int size, String[] tags) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.tags = tags;
    }
    public MyFile() {

    }
    public MyFile(String name, int size) {

        this.name = name;
        this.size = size;
    }
    public MyFile(long id,String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
