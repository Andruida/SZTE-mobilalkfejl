package hu.andruida.nezzuk.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hu.andruida.nezzuk.R;
import hu.andruida.nezzuk.model.adapters.CartAdapter;
import hu.andruida.nezzuk.viewmodel.CartViewModel;

public class CartActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CartActivity";

    private CartViewModel cartViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.cart_page_name);
        }

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        CartAdapter adapter = new CartAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartViewModel.getAllInCart(data -> data.observe(this, adapter::setTicketListingsData));


    }
}