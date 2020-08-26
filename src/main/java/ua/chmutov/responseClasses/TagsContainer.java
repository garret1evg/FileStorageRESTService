package ua.chmutov.responseClasses;

import java.util.Arrays;

public class TagsContainer {
    String[] tags;

    public TagsContainer() {
    }

    public TagsContainer(String[] tags) {
        this.tags = tags;
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
