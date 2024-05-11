package hu.andruida.nezzuk.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hu.andruida.nezzuk.dao.CartDao;
import hu.andruida.nezzuk.database.CartRoomDatabase;
import hu.andruida.nezzuk.model.TicketListing;

public class CartRepository {
    private final CartDao cartDao;
    private LiveData<List<TicketListing>> allInCart;

    public CartRepository(Application application, OnAllTicketListingsReadyListener listener) {
        CartRoomDatabase db = CartRoomDatabase.getInstance(application);
        cartDao = db.cartDao();
        new GetAllInCartTask(cartDao, data -> {
            allInCart = data;
            listener.onAllTicketListingsReady(data);
        }).execute();
    }

    public LiveData<List<TicketListing>> getAllInCart() {
        return allInCart;
    }

    public void insert(TicketListing ticketListing) {
        new InsertTask(cartDao).execute(ticketListing);
    }

    public void deleteAll() {
        new DeleteAllTask(cartDao).execute();
    }

    public void deleteById(int id) {
        new DeleteByIdTask(cartDao).execute(id);
    }

    public void updateAmountInCart(int id, int amount, int amountLeft) {
        new UpdateAmountInCartTask(cartDao).execute(id, amount, amountLeft);
    }

    public void getTicketById(int id, OnTicketListingReadyListener listener) {
        AsyncTask<Integer,Void,TicketListing> task = new GetTicketByIdTask(cartDao, listener).execute(id);
    }



    private static class InsertTask extends AsyncTask<TicketListing, Void, Void> {
        private CartDao cartDao;

        public InsertTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(TicketListing... ticketListings) {
            cartDao.insert(ticketListings[0]);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private CartDao cartDao;

        public DeleteAllTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.deleteAll();
            return null;
        }
    }

    private static class DeleteByIdTask extends AsyncTask<Integer, Void, Void> {
        private CartDao cartDao;

        public DeleteByIdTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            cartDao.deleteById(integers[0]);
            return null;
        }
    }

    private static class UpdateAmountInCartTask extends AsyncTask<Integer, Void, Void> {
        private CartDao cartDao;

        public UpdateAmountInCartTask(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            cartDao.updateAmountInCart(integers[0], integers[1], integers[2]);
            return null;
        }
    }

    private static class GetTicketByIdTask extends AsyncTask<Integer, Void, TicketListing> {
        private CartDao cartDao;
        private OnTicketListingReadyListener listener;

        public GetTicketByIdTask(CartDao cartDao, OnTicketListingReadyListener listener) {
            this.cartDao = cartDao;
            this.listener = listener;
        }

        @Override
        protected TicketListing doInBackground(Integer... integers) {
            return cartDao.getTicketById(integers[0]);
        }

        @Override
        protected void onPostExecute(TicketListing ticketListing) {
            listener.onTicketListingReady(ticketListing);
        }
    }

    private static class GetAllInCartTask extends AsyncTask<Void, Void, LiveData<List<TicketListing>>> {
        private CartDao cartDao;
        private OnAllTicketListingsReadyListener listener;

        public GetAllInCartTask(CartDao cartDao, OnAllTicketListingsReadyListener listener) {
            this.cartDao = cartDao;
            this.listener = listener;
        }

        @Override
        protected LiveData<List<TicketListing>> doInBackground(Void... voids) {
            return cartDao.getAllInCart();
        }

        @Override
        protected void onPostExecute(LiveData<List<TicketListing>> listLiveData) {
            listener.onAllTicketListingsReady(listLiveData);

        }
    }




}
