package app.it.hueic.nghiencuukhoahochueic.model;

/**
 * Created by kenhoang on 11/01/2018.
 */

public class FileModel {
    private int img;
    private String title;
    private String subTitle;

    public FileModel() {
    }

    public FileModel(int img, String title, String subTitle) {
        this.img = img;
        this.title = title;
        this.subTitle = subTitle;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return subTitle;
    }
}
