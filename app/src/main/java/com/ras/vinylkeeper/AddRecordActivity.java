package com.ras.vinylkeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ras.vinylkeeper.databinding.ActivityAddRecordBinding;

public class AddRecordActivity extends AppCompatActivity {

    private ActivityAddRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    static Intent addRecordIntentFactory(Context context) {
        return new Intent(context, AddRecordActivity.class);
    }
}