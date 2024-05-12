package hu.andruida.nezzuk.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;

import hu.andruida.nezzuk.activities.MainActivity;
import hu.andruida.nezzuk.R;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPref = getSharedPreferences("hu.andruida.nezzuk", MODE_PRIVATE);

        emailEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        emailEditText.setText(sharedPref.getString("email", ""));

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.e(LOG_TAG, "User is already logged in, redirecting to main page");
            startMainActivity();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.login_page_name);
        }
        Log.i(LOG_TAG, "Action bar:" + getSupportActionBar());


        findViewById(R.id.registerButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.loginButton).setOnClickListener(login);
        findViewById(R.id.anonLoginButton).setOnClickListener(loginAsAnon);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ticket_listings")
                .whereLessThanOrEqualTo("price", 10000)
                .aggregate(AggregateField.sum("amountLeft"))
                .get(AggregateSource.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AggregateQuerySnapshot snapshot = task.getResult();
                        TextView stat = findViewById(R.id.statText);
                        Long tickets = (Long) snapshot.get(AggregateField.sum("amountLeft"));
                        stat.setText(getString(R.string.ticket_stat, tickets));
                    } else {
                        Log.e(LOG_TAG, "Failed to get sum of amountLeft", task.getException());
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.apply();
    }

    private void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private final View.OnClickListener login = v -> {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) return;
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startMainActivity();
                } else {
                    Log.e(LOG_TAG, "User login failed", task.getException());
                    Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            });
    };

    private final View.OnClickListener loginAsAnon = v -> {
        mAuth.signInAnonymously()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startMainActivity();
                } else {
                    Log.e(LOG_TAG, "Anonymous login failed", task.getException());
                    Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            });
    };



}