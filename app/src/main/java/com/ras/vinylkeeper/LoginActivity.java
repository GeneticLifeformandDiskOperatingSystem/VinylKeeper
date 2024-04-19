package com.ras.vinylkeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.ras.vinylkeeper.database.VinylRepository;
import com.ras.vinylkeeper.database.entities.User;
import com.ras.vinylkeeper.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private VinylRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = VinylRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.usernameEditText.getText().toString();

        if(username.isEmpty()) {
            Log.d(MainActivity.TAG, "Username may not be blank");
            toastMaker("Username may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if(user != null) {
                String password = binding.passwordEditText.getText().toString();
                if(password.equals(user.getPassword())) {
                    startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(), user.getId()));
                } else {
                    toastMaker("Invalid password");
                    Log.d(MainActivity.TAG, "Invalid password");
                    binding.passwordEditText.setSelection(0, password.length());
                }
            } else {
                toastMaker(String.format("%s is not a valid username", username));
                binding.usernameEditText.setSelection(0,username.length());
            }
        });
    }

    private void toastMaker(String message) {
        Log.d(MainActivity.TAG, "Toasting");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}