package com.ras.vinylkeeper;

import static com.ras.vinylkeeper.MainActivity.LOGGED_OUT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ras.vinylkeeper.database.VinylRepository;
import com.ras.vinylkeeper.database.entities.Record;
import com.ras.vinylkeeper.databinding.ActivityAddRecordBinding;

public class AddRecordActivity extends AppCompatActivity {

    private ActivityAddRecordBinding binding;

    String artist = "";
    String album = "";
    int year = 0;
    double rating = 0.0;
    int loggedInUserId = -1;

    private VinylRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = VinylRepository.getRepository(getApplication());

        Intent intent = getIntent();
        loggedInUserId = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_USER_ID, LOGGED_OUT);

        binding.addRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecordInformation();
                insertRecord();
                startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });
    }

    private void getRecordInformation() {
        artist = binding.artistEditText.getText().toString();
        album = binding.albumEditText.getText().toString();
        try {
            year = Integer.parseInt(binding.yearEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(MainActivity.TAG, "Error reading value from year");
        }
        try {
            rating = binding.ratingBar.getRating();
        } catch (NumberFormatException e) {
            Log.d(MainActivity.TAG, "Error reading value from rating");
        }
        Log.i(MainActivity.TAG, "Collected:");
        Log.i(MainActivity.TAG, "artist: " + artist);
        Log.i(MainActivity.TAG, "album: " + album);
        Log.i(MainActivity.TAG, "year: " + year);
        Log.i(MainActivity.TAG, "rating: " + rating);

    }

    private void insertRecord() {
        if(artist.isEmpty() || album.isEmpty()) {
            return;
        }
        Record record = new Record(artist, album, year, rating, loggedInUserId);
        repository.insertRecord(record);
    }

    static Intent addRecordIntentFactory(Context context) {
        Intent intent = new Intent(context, AddRecordActivity.class);
        return intent;
    }
}