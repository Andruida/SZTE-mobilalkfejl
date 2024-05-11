package hu.andruida.nezzuk.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.andruida.nezzuk.R;
import hu.andruida.nezzuk.activities.CartActivity;
import hu.andruida.nezzuk.model.TicketListing;
import hu.andruida.nezzuk.viewmodel.CartViewModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    private List<TicketListing> mTicketListingsData;
    private final LayoutInflater mInflater;
    private Context mContext;


    public CartAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mTicketListingsData = new ArrayList<>();
    }

    public void setTicketListingsData(List<TicketListing> ticketListingsData) {
        mTicketListingsData = ticketListingsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TicketListing currentTicketListing = mTicketListingsData.get(position);

        holder.bindTo(currentTicketListing);
    }

    @Override
    public int getItemCount() {
        return mTicketListingsData.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTitle;
        private final TextView price;
        private final TextView amount;
        private final TextView date;
        private final TextView time;
        private final TextView location;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            price = itemView.findViewById(R.id.price);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
        }

        public void bindTo(TicketListing currentTicketListing) {
            itemTitle.setText(currentTicketListing.getTitle());
            price.setText(mContext.getString(R.string.money, currentTicketListing.getPrice()));
            amount.setText(String.valueOf(currentTicketListing.getAmountInCart()));
            date.setText(String.valueOf(currentTicketListing.getDate()));
            time.setText(String.valueOf(currentTicketListing.getTime()));
            location.setText(String.valueOf(currentTicketListing.getLocation()));

            itemView.findViewById(R.id.remove_from_cart).setOnClickListener(v -> {
                CartViewModel cartViewModel = new ViewModelProvider((CartActivity) mContext).get(CartViewModel.class);
                cartViewModel.deleteById(currentTicketListing.getLocalId());
            });

        }
    }
}
