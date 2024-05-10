package hu.andruida.nezzuk.model;

public class TicketListing {
    private String title;
    private String description;
    private String location;
    private String date;
    private String time;
    private String price;
    private int imageResource;

    private int amountLeft;

    public TicketListing(String title, String description, String location, String date, String time, String price, int imageResource, int amountLeft) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.price = price;
        this.imageResource = imageResource;
        this.amountLeft = amountLeft;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getAmountLeft() {
        return amountLeft;
    }
}
