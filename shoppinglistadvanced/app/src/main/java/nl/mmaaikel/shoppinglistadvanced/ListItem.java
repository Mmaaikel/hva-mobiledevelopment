package nl.mmaaikel.shoppinglistadvanced;

/**
 * Created by Maikel on 09-09-16.
 */

public class ListItem {
    private String title;
    private String description;
    private String longdescription;
    private int imageResource;

    //Constructor ("SETter method")
    public ListItem(String title, String description, String longdescription, int imageResource) {
        this.title = title;
        this.description = description;
        this.longdescription = longdescription;
        this.imageResource = imageResource;
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLongdescription() {
        return longdescription;
    }

    public int getImageResource() {
        return imageResource;
    }
}