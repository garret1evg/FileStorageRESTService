package ua.chmutov.response;
/**
 * class extends DB query
 */
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
        StringBuilder str = new StringBuilder("\"must\":[");
        if(wildcard!=null){
            str.append("{\"wildcard\": {\"name\":\"*")
                    .append(wildcard)
                    .append("*\"}}");
            if(tags!=null)
                str.append(",");
        }
        if(tags!=null){
            for (int i = 0;i < tags.length;i++) {
                str.append("{\"match\": {\"tags\":\"")
                        .append(tags[i])
                        .append("\"}}");
                if (i+1 < tags.length)
                    str.append(",");
            }
        }

        str.append("]");
        return str.toString();
    }
}
