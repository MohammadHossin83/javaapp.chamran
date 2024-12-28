package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameInput.setError("نام کاربری نمی‌تواند خالی باشد");
                    return;
                }

                if (password.isEmpty()) {
                    passwordInput.setError("رمز عبور نمی‌تواند خالی باشد");
                    return;
                }
                if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    if (!username.equals(USERNAME)) {
                        Toast.makeText(LoginActivity.this, "نام کاربری اشتباه است", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "رمز عبور اشتباه است", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
