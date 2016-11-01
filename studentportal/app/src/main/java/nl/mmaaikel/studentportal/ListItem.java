package nl.mmaaikel.studentportal;

/**
 * Created by Maikel on 12-09-16.
 */

public class ListItem {
    private String url;
    private String title;

    public ListItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
