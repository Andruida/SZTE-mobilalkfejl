package hu.andruida.nezzuk.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hu.andruida.nezzuk.R;
import hu.andruida.nezzuk.model.TicketListing;

public class TicketListingAdapter extends RecyclerView.Adapter<TicketListingAdapter.ViewHolder> implements Filterable {

    private ArrayList<TicketListing> mTicketListingsData;
    private ArrayList<TicketListing> mTicketListingsDataAll;
    private Context mContext;
    private int lastPosition = -1;


    public TicketListingAdapter(Context context, ArrayList<TicketListing> ticketListingsData) {
        this.mTicketListingsData = ticketListingsData;
        this.mTicketListingsDataAll = ticketListingsData;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketListing currentTicketListing = mTicketListingsData.get(position);

        holder.bindTo(currentTicketListing);
    }

    @Override
    public int getItemCount() {
        return mTicketListingsData.size();
    }

    @Override
    public Filter getFilter() {
        return ticketFilter;
    }

    private final Filter ticketFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TicketListing> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.count = mTicketListingsDataAll.size();
                results.values = mTicketListingsDataAll;
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (TicketListing item : mTicketListingsDataAll) {
                    if (item.getTitle().toLowerCase().contains(filteredPattern)) {
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mTicketListingsData = (ArrayList<TicketListing>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText;
        private TextView mDescriptionText;
        private TextView mLocationText;
        private TextView mDateText;
        private TextView mTimeText;
        private TextView mPriceText;
        private ImageView mTicketImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDescriptionText = itemView.findViewById(R.id.description);
            mLocationText = itemView.findViewById(R.id.location);
            mDateText = itemView.findViewById(R.id.date);
            mTimeText = itemView.findViewById(R.id.time);
            mPriceText = itemView.findViewById(R.id.price);
            mTicketImage = itemView.findViewById(R.id.itemImage);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(v -> {
                
            });
        }

        public void bindTo(TicketListing currentTicketListing) {

            String description = currentTicketListing.getDescription()
                    .substring(0, Math.min(currentTicketListing.getDescription().length(), 200));
            if (description.length() < currentTicketListing.getDescription().length()) {
                description += "...";
            }

            mTitleText.setText(currentTicketListing.getTitle());
            mDescriptionText.setText(description);
            mLocationText.setText(currentTicketListing.getLocation());
            mDateText.setText(currentTicketListing.getDate());
            mTimeText.setText(currentTicketListing.getTime());
            mPriceText.setText(mContext.getString(R.string.money, currentTicketListing.getPrice()));


            Glide.with(mContext)
                    .load(currentTicketListing.getImageUrl())
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(mTicketImage);
        }
    }
}

