package freegrowsoftware.growtracker.objects;

import java.io.Serializable;

public class Plant implements Serializable {
    private int id;
    private String name;
    private String start_date;
    private String image_location;

    public Plant(int id, String name, String start_date, String image_location) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.image_location = image_location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return this.start_date;
    }

    public void setStartDate(String date) {
        this.start_date = date;
    }

    public String getImageLocation() {
        if (this.image_location == null) {
            return "";
        }
        return this.image_location.replace("file:", "");
    }

    public void setImageLocation(String image_location) {
        this.image_location = image_location;
    }

    public int getId() {
        return this.id;
    }
}
