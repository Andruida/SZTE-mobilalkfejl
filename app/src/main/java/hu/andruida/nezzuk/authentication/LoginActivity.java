package hu.andruida.nezzuk.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import hu.andruida.nezzuk.activities.MainActivity;
import hu.andruida.nezzuk.R;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;

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

        emailEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

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