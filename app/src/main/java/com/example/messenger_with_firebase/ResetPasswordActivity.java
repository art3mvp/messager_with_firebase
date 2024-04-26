package com.example.messenger_with_firebase;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ResetPasswordActivity extends AppCompatActivity {


    private static final String EXTRA_EMAIL = "email";
    private ResetPasswordViewModel viewModel;
    private Button buttonReset;
    String email;
    private EditText editTextUsernameForReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        email = getIntent().getStringExtra(EXTRA_EMAIL);
        editTextUsernameForReset.setText(email);

        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextUsernameForReset.getText().toString();
                if (emailValidate(email)) {
                    viewModel.resetPassword(email);
                    finish();
                }
            }
        });
    }

    private boolean emailValidate(String email) {
        if (email.isEmpty()) {
            Toast.makeText(
                    ResetPasswordActivity.this,
                    "username field is empty",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }
        return true;
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            errorMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        viewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            R.string.the_reset_link,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    private void initViews() {
        editTextUsernameForReset = findViewById(R.id.editTextUsernameForReset);
        buttonReset = findViewById(R.id.buttonReset);
    }
}