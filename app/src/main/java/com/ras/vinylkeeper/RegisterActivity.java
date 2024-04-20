package com.ras.vinylkeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.ras.vinylkeeper.database.VinylRepository;
import com.ras.vinylkeeper.database.entities.User;
import com.ras.vinylkeeper.databinding.ActivityRegisterBinding;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private VinylRepository repository;

    private String username;
    private String passwordOne;
    private String passwordTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = VinylRepository.getRepository(getApplication());

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndCreateUser();
            }
        });
    }

    private void validateAndCreateUser() {
        username = binding.usernameEditText.getText().toString();
        passwordOne = binding.passwordEditText.getText().toString();
        passwordTwo = binding.confirmPasswordEditText.getText().toString();
        AtomicBoolean userExists = new AtomicBoolean(false);

        if(username.isEmpty()) {
            Log.d(MainActivity.TAG, "Username may not be blank");
            toastMaker("Username may not be blank");
            binding.usernameEditText.requestFocus();
            return;
        }
        if(passwordOne.isEmpty() || passwordTwo.isEmpty()) {
            Log.d(MainActivity.TAG, "Password may not be blank");
            toastMaker("Password may not be blank");
            binding.passwordEditText.requestFocus();
            return;
        }

        if(!passwordOne.equals(passwordTwo)) {
            Log.d(MainActivity.TAG, "Passwords do not match");
            toastMaker("Passwords do not match");
            binding.passwordEditText.setText("");
            binding.confirmPasswordEditText.setText("");
            binding.passwordEditText.requestFocus();
            return;
        }
        LiveData<List<User>> usersObserver = repository.getAllUserNames();
        usersObserver.observe(this, users -> {
            for (User u : users) {
                if (username.equalsIgnoreCase(u.getUsername())) {
                    userExists.set(true);
                    toastMaker("User already exists");
                    Log.d(MainActivity.TAG, String.format("Found existing user %s in the database%n", u.getUsername()));
                    binding.usernameEditText.requestFocus();
                    binding.usernameEditText.setSelection(0, username.length());
                    binding.passwordEditText.setText("");
                    binding.confirmPasswordEditText.setText("");
                    break;
                }
            }
            if(!userExists.get()) {
                User user = new User(username, passwordOne);
                repository.insertUser(user);
                toastMaker("User created");
                usersObserver.removeObservers(this);
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent registerActivityIntentFactory(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}