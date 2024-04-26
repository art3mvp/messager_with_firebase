package com.example.messenger_with_firebase;

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

import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNewUsername;
    private String email;
    private EditText editTextNewPassword;
    private String password;
    private EditText editTextPasswordConfirm;
    private EditText editTextName;
    private String name;
    private String surname;
    private int age;
    private EditText editTextAge;
    private EditText editTextSurname;
    private String passwordConfirm;
    private Button buttonSignUp;
    private RegisterViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        initViews();
        observeViewModel();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextNewUsername.getText().toString().trim();
                password = editTextNewPassword.getText().toString().trim();
                passwordConfirm = editTextPasswordConfirm.getText().toString().trim();
                name = editTextName.getText().toString().trim();
                if (!ageValidate(editTextAge.getText().toString())) {
                    return;
                }
                age = Integer.parseInt(editTextAge.getText().toString().trim());
                surname = editTextSurname.getText().toString().trim();
                if (dataValidate(email, password, passwordConfirm, name, surname, age)) {
                    viewModel.signUp(email, password, name, surname, age);
                }
            }
        });
    }


    private boolean ageValidate(String age) {
        if (age.isEmpty()) {
            Toast.makeText(
                    RegisterActivity.this,
                    "empty field",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean dataValidate(
            String user,
            String password,
            String passwordConfirm,
            String name,
            String surname,
            int age
    ) {
        if (
                user.isEmpty() ||
                passwordConfirm.isEmpty() ||
                password.isEmpty() ||
                name.isEmpty() ||
                surname.isEmpty()
        ) {
            Toast.makeText(
                    RegisterActivity.this,
                    "Empty fields",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(
                    RegisterActivity.this,
                    "passwords don't match",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        } else {
            return true;
        }
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(
                            RegisterActivity.this,
                            errorMessage,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(
                            RegisterActivity.this,
                            firebaseUser.getUid()
                    );
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void initViews() {
        editTextNewUsername = findViewById(R.id.editTextNewUsername);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextSurname = findViewById(R.id.editTextSurname);
    }
    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}