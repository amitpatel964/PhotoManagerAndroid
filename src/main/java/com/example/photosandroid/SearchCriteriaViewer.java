package com.example.photosandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.Album;
import com.example.model.Photo;
import com.example.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaViewer extends AppCompatActivity {

    // The user class that holds all of the photos
    User user = new User();

    // List of albums
    List<Album> listOfAlbums;

    // Buttons
    Button searchOneTagButton, searchTwoTagAndButton, searchTwoTagOrButton, returnToAlbumListButton;

    // Spinners
    Spinner oneTagTypeSpinner, twoTagType1Spinner, twoTagType2Spinner;

    // EditTexts
    EditText oneTagValueEditText, twoTagValue1EditText, twoTagValue2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_criteria_viewer);

        searchOneTagButton = findViewById(R.id.searchOneTagButton);
        searchTwoTagAndButton = findViewById(R.id.searchTwoTagAndButton);
        searchTwoTagOrButton = findViewById(R.id.searchTwoTagOrButton);
        returnToAlbumListButton = findViewById(R.id.returnToAlbumListButton);
        oneTagTypeSpinner = findViewById(R.id.oneTagTypeSpinner);
        twoTagType1Spinner = findViewById(R.id.twoTagType1Spinner);
        twoTagType2Spinner = findViewById(R.id.twoTagType2Spinner);
        oneTagValueEditText = findViewById(R.id.oneTagValueEditText);
        twoTagValue1EditText = findViewById(R.id.twoTagValue1EditText);
        twoTagValue2EditText = findViewById(R.id.twoTagValue2EditText);

        load();
        save();

        listOfAlbums = user.getListOfAlbums();

        String[] items = new String[]{"Location", "Person"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        oneTagTypeSpinner.setAdapter(adapter);
        twoTagType1Spinner.setAdapter(adapter);
        twoTagType2Spinner.setAdapter(adapter);

        // Search one tag button is clicked
        searchOneTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagType = oneTagTypeSpinner.getSelectedItem().toString();
                String tagValue = oneTagValueEditText.getText().toString();
                List<Photo> photoResults = new ArrayList<>();
                List<String> photoUriResults = new ArrayList<>();

                if (tagValue.trim().equals(""))
                {
                    return;
                }

                for (int i = 0; i < listOfAlbums.size(); i++)
                {
                    Album currentAlbum = listOfAlbums.get(i);
                    List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();

                    for (int j = 0; j < currentAlbumPhotos.size(); j++)
                    {
                        Photo currentPhoto = currentAlbumPhotos.get(j);
                        if (currentPhoto.searchTagsOnePair(tagType,tagValue) && !photoUriResults.contains(currentPhoto.getPhotoUri()))
                        {
                            photoResults.add(currentPhoto);
                            photoUriResults.add(currentPhoto.getPhotoUri());
                        }
                    }
                }

                if (photoResults.size() == 0)
                {
                    Toast.makeText(SearchCriteriaViewer.this, "No photos found", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.photoSearchResults.clear();
                user.photoSearchResults.addAll(photoResults);

                save();

                Intent intent = new Intent(SearchCriteriaViewer.this, SearchResultsViewer.class);
                startActivity(intent);
            }
        });

        // Two tags and search button clicked
        searchTwoTagAndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagType1 = twoTagType1Spinner.getSelectedItem().toString();
                String tagValue1 = twoTagValue1EditText.getText().toString();
                String tagType2 = twoTagType2Spinner.getSelectedItem().toString();
                String tagValue2 = twoTagValue2EditText.getText().toString();
                List<Photo> photoResults = new ArrayList<>();
                List<String> photoUriResults = new ArrayList<>();

                if (tagValue1.trim().equals("") || tagValue2.trim().equals(""))
                {
                    Toast.makeText(SearchCriteriaViewer.this, "Please fill both tag value fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < listOfAlbums.size(); i++)
                {
                    Album currentAlbum = listOfAlbums.get(i);
                    List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();

                    for (int j = 0; j < currentAlbumPhotos.size(); j++)
                    {
                        Photo currentPhoto = currentAlbumPhotos.get(j);
                        if (currentPhoto.searchTagsTwoPairsAnd(tagType1, tagValue1, tagType2, tagValue2)
                                && !photoUriResults.contains(currentPhoto.getPhotoUri()))
                        {
                            photoResults.add(currentPhoto);
                            photoUriResults.add(currentPhoto.getPhotoUri());
                        }
                    }
                }

                if (photoResults.size() == 0)
                {
                    Toast.makeText(SearchCriteriaViewer.this, "No photos found", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.photoSearchResults.clear();
                user.photoSearchResults.addAll(photoResults);

                save();

                Intent intent = new Intent(SearchCriteriaViewer.this, SearchResultsViewer.class);
                startActivity(intent);
            }
        });

        //Two tags or search button clicked
        searchTwoTagOrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagType1 = twoTagType1Spinner.getSelectedItem().toString();
                String tagValue1 = twoTagValue1EditText.getText().toString();
                String tagType2 = twoTagType2Spinner.getSelectedItem().toString();
                String tagValue2 = twoTagValue2EditText.getText().toString();
                List<Photo> photoResults = new ArrayList<>();
                List<String> photoUriResults = new ArrayList<>();

                if (tagValue1.trim().equals("") || tagValue2.trim().equals(""))
                {
                    Toast.makeText(SearchCriteriaViewer.this, "Please fill both tag value fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < listOfAlbums.size(); i++)
                {
                    Album currentAlbum = listOfAlbums.get(i);
                    List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();

                    for (int j = 0; j < currentAlbumPhotos.size(); j++)
                    {
                        Photo currentPhoto = currentAlbumPhotos.get(j);
                        if (currentPhoto.searchTagsTwoPairsOr(tagType1, tagValue1, tagType2, tagValue2)
                                && !photoUriResults.contains(currentPhoto.getPhotoUri()))
                        {
                            photoResults.add(currentPhoto);
                            photoUriResults.add(currentPhoto.getPhotoUri());
                        }
                    }
                }

                if (photoResults.size() == 0)
                {
                    Toast.makeText(SearchCriteriaViewer.this, "No photos found", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.photoSearchResults.clear();
                user.photoSearchResults.addAll(photoResults);

                save();

                Intent intent = new Intent(SearchCriteriaViewer.this, SearchResultsViewer.class);
                startActivity(intent);
            }
        });

        // Back button is clicked
        returnToAlbumListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Loads the user's data
     */
    private void load()
    {
        try {
            user = User.readApp(SearchCriteriaViewer.this);
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(SearchCriteriaViewer.this, "Save file not found, creating new save", Toast.LENGTH_SHORT).show();
            user = new User();
        }
    }

    /**
     * This function is called whenever we want to save the data
     */
    private void save()
    {
        try {
            user.writeApp(SearchCriteriaViewer.this);
        } catch (IOException e) {
            Toast.makeText(SearchCriteriaViewer.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}