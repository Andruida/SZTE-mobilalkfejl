package hu.andruida.nezzuk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;

import hu.andruida.nezzuk.R;
import hu.andruida.nezzuk.authentication.LoginActivity;
import hu.andruida.nezzuk.model.TicketListing;
import hu.andruida.nezzuk.model.adapters.TicketListingAdapter;
import hu.andruida.nezzuk.viewmodel.CartViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private TicketListingAdapter mAdapter;
    private ArrayList<TicketListing> mTicketListings;

    private FirebaseFirestore mFirestore;
    private CollectionReference mTicketListingsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(LOG_TAG, "User is not logged in, redirecting to login page");
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

//        findViewById(R.id.logoutButton).setOnClickListener(v -> {
//            mAuth.signOut();
//            Intent i = new Intent(this, LoginActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTicketListings = new ArrayList<>();

        mAdapter = new TicketListingAdapter(this, mTicketListings);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();

        mTicketListingsRef = mFirestore.collection("ticket_listings");

        mTicketListingsRef
                .orderBy("date", Query.Direction.ASCENDING)
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mTicketListings.clear();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        TicketListing ticketListing = queryDocumentSnapshots.getDocuments().get(i).toObject(TicketListing.class);
                        mTicketListings.add(ticketListing);
                        mAdapter.notifyItemInserted(i);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "Error getting ticket listings", e);
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.ticket_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchView == null) {
            Log.e(LOG_TAG, "SearchView is null");
            return false;
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return true;
        } else if (itemId == R.id.cart) {
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(LOG_TAG, "onPrepareOptionsMenu");
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        if (rootView == null) {
            Log.e(LOG_TAG, "rootView is null");
            return super.onPrepareOptionsMenu(menu);
        }
        FrameLayout redCircle = rootView.findViewById(R.id.view_alert_red_circle);
        TextView countTextView = rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(v -> onOptionsItemSelected(alertMenuItem));
        CartViewModel cartViewModel = new CartViewModel(getApplication());

        Log.i(LOG_TAG, "Setting up cart icon");
        cartViewModel.getAllInCart(data -> data.observe(this, ticketListings -> {
            Log.i(LOG_TAG, "Updating cart icon");
            int cartItems = ticketListings.size();
            if (0 < cartItems) {
                countTextView.setText(String.valueOf(cartItems));
            } else {
                countTextView.setText("");
            }

            redCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);
        }));

        return super.onPrepareOptionsMenu(menu);
    }

}