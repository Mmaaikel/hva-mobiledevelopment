package nl.mmaaikel.animalviewer;

/**
 * Created by Maikel on 03-10-16.
 */

public class ListModel {

    private int imageId;
    private String imageText;

    public ListModel(int imageId, String imageText ) {
        this.imageId = imageId;
        this.imageText = imageText;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }
}
