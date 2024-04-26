package com.example.messenger_with_firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText editTextPassword;
    private EditText editTextUsername;
    private Button buttonSignIn;
    private TextView textViewReset;
    private TextView textViewSignUp;
    private LoginViewModel viewModel;
    private String user;
    private String password;


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


        initViews();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = editTextPassword.getText().toString();
                user = editTextUsername.getText().toString();
                if (password.isEmpty() || user.isEmpty()) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Empty fields",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    viewModel.signIn(user, password);
                }
            }
        });


        textViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ResetPasswordActivity.newIntent(LoginActivity.this, user);
                startActivity(intent);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RegisterActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }
    private void initViews() {
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewReset = findViewById(R.id.textViewReset);
        textViewSignUp = findViewById(R.id.textViewSignUp);
    }

    private void observeViewModel() {
        viewModel.getError().observe(LoginActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(
                            LoginActivity.this,
                            errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
        viewModel.getUser().observe(LoginActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                   Intent intent =  UsersActivity.newIntent(
                           LoginActivity.this,
                           firebaseUser.getUid()
                   );
                   startActivity(intent);
                   finish();
                }
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

}