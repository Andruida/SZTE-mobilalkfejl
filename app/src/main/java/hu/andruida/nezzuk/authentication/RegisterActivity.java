package hu.andruida.nezzuk.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import hu.andruida.nezzuk.activities.MainActivity;
import hu.andruida.nezzuk.R;

public class RegisterActivity extends AppCompatActivity {
    public static final String LOG_TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordAgainEditText = findViewById(R.id.passwordAgainEditText);
        usernameEditText = findViewById(R.id.usernameEditText);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.e(LOG_TAG, "User is already logged in, redirecting to main page");
            startMainActivity();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.register_page_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.registerButton).setOnClickListener(register);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private final View.OnClickListener register = v -> {
        findViewById(R.id.registerButton).setEnabled(false);
        if (!passwordEditText.getText().toString().equals(passwordAgainEditText.getText().toString())) {
            Log.e(LOG_TAG, "Passwords do not match");
            Toast.makeText(this, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
            findViewById(R.id.registerButton).setEnabled(true);
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(LOG_TAG, "Registration successful");
                    Toast.makeText(this, R.string.successful_registration, Toast.LENGTH_SHORT).show();
                    startMainActivity();
                } else {
                    Log.e(LOG_TAG, "Registration failed", task.getException());
                    findViewById(R.id.registerButton).setEnabled(true);
                    Toast.makeText(this, R.string.failed_registration, Toast.LENGTH_SHORT).show();
                }
            });
    };

    private void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

}