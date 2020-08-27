package ua.chmutov.responseClasses;

import java.util.Arrays;

public class TagsContainer {
    private String[] tags;
    private String wildcard;

    public TagsContainer() {
    }

    public TagsContainer(String[] tags,String wildcard) {
        this.tags = tags;
        this.wildcard = wildcard;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\"must\":[");
        if(wildcard!=null){
            str.append("{\"wildcard\": {\"name\":\"*"+ wildcard +"*\"}}");
            if(tags!=null)
                str.append(",");
        }
        if(tags!=null){
            for (int i = 0;i < tags.length;i++) {
                str.append("{\"match\": {\"tags\":\""+ tags[i] +"\"}}");
                if (i+1 < tags.length)
                    str.append(",");
            }
        }

        str.append("]");
        return str.toString();
    }
}
