package com.example.photosandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Album;
import com.example.model.Photo;
import com.example.model.Tag;
import com.example.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlideshowViewer extends AppCompatActivity {

    // Keeps track of the save data
    User user = new User();

    // Current album being looked at
    Album currentAlbum;

    // Current photo being looked at
    List<Photo> photosOfAlbum;

    // Buttons
    Button nextPhotoButton;
    Button previousPhotoButton;
    Button returnButton;

    // ImageView
    ImageView displayOfPhotoSlideshow;

    // TextView Title
    TextView slideshowTitle;

    // ListView for tags
    ListView tagsListViewSlideshow;

    // Index to keep track of where the user is in the album
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_viewer);

        nextPhotoButton = findViewById(R.id.nextPhotoButton);
        previousPhotoButton = findViewById(R.id.previousPhotoButton);
        returnButton = findViewById(R.id.returnButton);
        displayOfPhotoSlideshow = findViewById(R.id.displayOfPhotoSlideshow);
        slideshowTitle = findViewById(R.id.slideshowTitle);
        tagsListViewSlideshow = findViewById(R.id.tagsSlideshowListView);

        // Read the save data
        try {
            user = User.readApp(SlideshowViewer.this);
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(SlideshowViewer.this, "Save file not found, creating new save", Toast.LENGTH_SHORT).show();
            user = new User();
        }

        try {
            user.writeApp(SlideshowViewer.this);
        } catch (IOException e) {
            Toast.makeText(SlideshowViewer.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        Bundle bundle = getIntent().getExtras();
        String albumName = bundle.getString("albumName");

        index = bundle.getInt("position");

        currentAlbum = user.getAlbum(albumName);
        photosOfAlbum = currentAlbum.getPhotos();

        slideshowTitle.setText(albumName + " Slideshow");

        displayOfPhotoSlideshow.setImageURI(Uri.parse(photosOfAlbum.get(index).getPhotoUri()));

        refreshTagsListview();

        if (photosOfAlbum.size() == 1)
        {
            previousPhotoButton.setEnabled(false);
            nextPhotoButton.setEnabled(false);
        }
        else if (index == 0)
        {
            previousPhotoButton.setEnabled(false);
        }
        else if (index == photosOfAlbum.size() - 1)
        {
            nextPhotoButton.setEnabled(false);
        }

        // Next button is clicked
        nextPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                previousPhotoButton.setEnabled(true);

                if (index == photosOfAlbum.size() - 1)
                {
                    nextPhotoButton.setEnabled(false);
                }

                displayOfPhotoSlideshow.setImageURI(Uri.parse(photosOfAlbum.get(index).getPhotoUri()));

                refreshTagsListview();
            }
        });

        // Previous button is clicked
        previousPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                nextPhotoButton.setEnabled(true);

                if (index == 0)
                {
                    previousPhotoButton.setEnabled(false);
                }

                displayOfPhotoSlideshow.setImageURI(Uri.parse(photosOfAlbum.get(index).getPhotoUri()));

                refreshTagsListview();
            }
        });

        // Return button is clicked
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Refreshes the listview of tags for the current photo
     */
    public void refreshTagsListview()
    {
        List<String> tagStrings = new ArrayList<>();

        List<Tag> tags = photosOfAlbum.get(index).getTagList();

        // Get the album names to put into the listview
        for(int i = 0; i < tags.size(); i++)
        {
            tagStrings.add(tags.get(i).toString());
        }

        // Build the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SlideshowViewer.this, android.R.layout.simple_list_item_1, tagStrings);
        tagsListViewSlideshow.setAdapter(adapter);
    }
}