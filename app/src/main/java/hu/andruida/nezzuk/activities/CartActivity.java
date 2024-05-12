package hu.andruida.nezzuk.activities;

import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import hu.andruida.nezzuk.R;
import hu.andruida.nezzuk.authentication.LoginActivity;
import hu.andruida.nezzuk.model.TicketListing;
import hu.andruida.nezzuk.model.adapters.CartAdapter;
import hu.andruida.nezzuk.notifications.NotificationHelper;
import hu.andruida.nezzuk.viewmodel.CartViewModel;

public class CartActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CartActivity";

    private CartViewModel cartViewModel;

    private JobScheduler mJobScheduler;

    private List<TicketListing> mTicketListingsData;


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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(LOG_TAG, "User is not logged in, redirecting to login page");
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        CartAdapter adapter = new CartAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartViewModel.getAllInCart(data -> data.observe(this, listings -> {
            if (listings != null) {
                mTicketListingsData = listings;
                adapter.setTicketListingsData(listings);
                TextView checkoutPrice = findViewById(R.id.checkoutPrice);
                int price = 0;
                for (int i = 0; i < listings.size(); i++) {
                    price += listings.get(i).getPrice()*listings.get(i).getAmountInCart();
                }
                checkoutPrice.setText(getString(R.string.money, price));
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
                checkoutPrice.startAnimation(animation);
            }
        }));

        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        // request permission to send notifications

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setupNotifications();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupNotifications() {

        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Log.i(LOG_TAG, "Notifications are disabled, requesting permission");
            ActivityResultContracts.RequestPermission requestPermission = new ActivityResultContracts.RequestPermission();
            registerForActivityResult(requestPermission, isGranted -> {
                Log.i(LOG_TAG, "Notification permission granted: " + isGranted);
            }).launch("android.permission.POST_NOTIFICATIONS");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTicketListingsData != null && !mTicketListingsData.isEmpty()) {
            sendNotification();
        }

    }

    private void sendNotification() {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.send(getString(R.string.notif_title), getString(R.string.notif_body));
        }
    }
}