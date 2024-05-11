package hu.andruida.nezzuk.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import hu.andruida.nezzuk.model.TicketListing;

@Dao
public interface CartDao {
    @Insert
    void insert(TicketListing ticketListing);

    @Query("DELETE FROM ticket_listings")
    void deleteAll();

    @Query("SELECT * FROM ticket_listings")
    LiveData<List<TicketListing>> getAllInCart();

    @Query("SELECT * FROM ticket_listings WHERE id = :id")
    TicketListing getTicketById(int id);

    @Query("DELETE FROM ticket_listings WHERE id = :id")
    void deleteById(int id);

    @Query("UPDATE ticket_listings SET amountInCart = :amount, amountLeft = :amountLeft WHERE id = :id")
    void updateAmountInCart(int id, int amount, int amountLeft);
}
