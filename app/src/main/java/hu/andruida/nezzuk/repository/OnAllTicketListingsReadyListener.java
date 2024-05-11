package hu.andruida.nezzuk.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import hu.andruida.nezzuk.model.TicketListing;

public interface OnAllTicketListingsReadyListener {
    void onAllTicketListingsReady(LiveData<List<TicketListing>> allTicketListings);
}
