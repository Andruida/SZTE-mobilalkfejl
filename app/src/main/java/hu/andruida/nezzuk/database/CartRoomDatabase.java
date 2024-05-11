package hu.andruida.nezzuk.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import hu.andruida.nezzuk.dao.CartDao;
import hu.andruida.nezzuk.model.TicketListing;

@Database(entities = {TicketListing.class}, version = 1, exportSchema = false)
public abstract class CartRoomDatabase extends RoomDatabase {
    public abstract CartDao cartDao();
    private static volatile CartRoomDatabase INSTANCE;

    public static CartRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (CartRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CartRoomDatabase.class, "cart_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
