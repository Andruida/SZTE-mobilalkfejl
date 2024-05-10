package hu.andruida.nezzuk.model;

public class TicketListing {
    private String title;
    private String description;
    private String location;
    private String date;
    private String time;
    private int price;
    private String imageUrl;

    private int amountLeft;

    public TicketListing() {
    }
    public TicketListing(String title, String description, String location, String date, String time, String price, String amountLeft, String imageUrl) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.price = Integer.parseInt(price);
        this.amountLeft = Integer.parseInt(amountLeft);
        this.imageUrl = imageUrl;
        if (imageUrl.startsWith("http://")) {
            this.imageUrl =  imageUrl.replace("http://", "https://");
        }
    }
    public TicketListing(String title, String description, String location, String date, String time, int price, String imageUrl, int amountLeft) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.price = price;
        this.imageUrl = imageUrl;
        this.amountLeft = amountLeft;
        if (imageUrl.startsWith("http://")) {
            this.imageUrl =  imageUrl.replace("http://", "https://");
        }
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

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getAmountLeft() {
        return amountLeft;
    }
}
