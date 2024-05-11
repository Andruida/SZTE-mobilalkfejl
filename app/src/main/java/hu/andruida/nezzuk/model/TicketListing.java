package hu.andruida.nezzuk.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ticket_listings")
public class TicketListing {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int localId;
    private String title;
    private String description;
    private String location;
    private String date;
    private String time;
    private int price;
    private String imageUrl;

    private int amountLeft;
    private int amountInCart = 0;

    public TicketListing() {
    }
    @Ignore
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
    @Ignore
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

    public int getLocalId() {
        return localId;
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

    public int getAmountInCart() {
        return amountInCart;
    }

    public void setAmountInCart(int amountInCart) {
        this.amountInCart = amountInCart;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAmountLeft(int amountLeft) {
        this.amountLeft = amountLeft;
    }
}
