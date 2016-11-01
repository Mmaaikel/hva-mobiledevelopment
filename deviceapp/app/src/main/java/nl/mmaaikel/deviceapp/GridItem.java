package nl.mmaaikel.deviceapp;

/**
 * Created by Maikel on 12-09-16.
 */

public class GridItem {
    private int imageResource;
    private String title;

    public GridItem(String title, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }
}
