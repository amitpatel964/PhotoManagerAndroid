package com.example.photosandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Photo;
import com.example.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsViewer extends AppCompatActivity {

    // The user class that holds all of the photos
    User user = new User();

    // Photo results
    List<Photo> photoResults = new ArrayList<>();

    // Button
    Button backToSearchButton;

    // GridView
    GridView photoResultsGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_viewer);

        backToSearchButton = findViewById(R.id.backToSearchButton);
        photoResultsGridView = findViewById(R.id.photoResultsGridView);

        try {
            user = User.readApp(SearchResultsViewer.this);
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(SearchResultsViewer.this, "Save file not found, creating new save", Toast.LENGTH_SHORT).show();
            user = new User();
        }

        try {
            user.writeApp(SearchResultsViewer.this);
        } catch (IOException e) {
            Toast.makeText(SearchResultsViewer.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        photoResults.addAll(user.photoSearchResults);

        ImageAdapter imageAdapter = new ImageAdapter(SearchResultsViewer.this);

        imageAdapter.setPhotos(photoResults);
        photoResultsGridView.setAdapter(imageAdapter);

        backToSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}