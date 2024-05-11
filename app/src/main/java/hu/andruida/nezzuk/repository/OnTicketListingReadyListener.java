package hu.andruida.nezzuk.repository;

import hu.andruida.nezzuk.model.TicketListing;

public interface OnTicketListingReadyListener {
    void onTicketListingReady(TicketListing ticketListing);
}
