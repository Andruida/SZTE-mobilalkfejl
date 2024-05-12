package hu.andruida.nezzuk.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hu.andruida.nezzuk.model.TicketListing;
import hu.andruida.nezzuk.repository.CartRepository;
import hu.andruida.nezzuk.repository.OnAllTicketListingsReadyListener;
import hu.andruida.nezzuk.repository.OnTicketListingReadyListener;

public class CartViewModel extends AndroidViewModel {

    private final CartRepository repository;
    private final Application application;
    private LiveData<List<TicketListing>> allInCart;

    public CartViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.repository = new CartRepository(application, data ->{
            synchronized (this) {
                if (allInCart == null) {
                    allInCart = data;
                }
            }
        });
    }

    // well that's dumb
    public void getAllInCart(OnAllTicketListingsReadyListener listener) {
        if (allInCart != null) {
            listener.onAllTicketListingsReady(allInCart);
        } else {
            new CartRepository(application, data -> {
                synchronized (this) {
                    if (allInCart == null) {
                        allInCart = data;
                        listener.onAllTicketListingsReady(data);
                        return;
                    }
                    if (data.getValue() == null) {
                        listener.onAllTicketListingsReady(data);
                        return;
                    }
                    if (allInCart.getValue() == null) {
                        allInCart = data;
                    }
                    assert allInCart.getValue() != null;
                    allInCart.getValue().clear();
                    allInCart.getValue().addAll(data.getValue());
                    allInCart.notifyAll();

                    listener.onAllTicketListingsReady(data);
                }
            });

        }
    }

    public void insert(TicketListing ticketListing) {
        repository.insert(ticketListing);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

    public void updateAmountInCart(int id, int amount, int amountLeft) {
        repository.updateAmountInCart(id, amount, amountLeft);
    }

    public void getTicketById(int id, OnTicketListingReadyListener listener) {
        repository.getTicketById(id, listener);
    }


}
