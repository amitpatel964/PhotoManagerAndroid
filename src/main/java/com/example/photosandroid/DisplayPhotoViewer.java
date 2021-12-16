package com.example.photosandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.Album;
import com.example.model.Photo;
import com.example.model.Tag;
import com.example.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayPhotoViewer extends AppCompatActivity {

    // Keeps track of the save data
    User user = new User();

    // Current album being looked at
    Album currentAlbum;

    // Current photo being looked at
    Photo currentPhoto;

    // ImageView that needs to be set
    ImageView displayOfPhoto;

    // Buttons
    Button backToPhotoListButton;
    Button addTagButton;
    Button removeTagButton;
    Button slideshowDisplayButton;

    // ListView of tags
    ListView tagsListView;

    // Spinner for tag types
    Spinner tagTypeSpinner;

    // EditText for tag value
    EditText tagValueEditText;

    // Position of photo in album
    int position;

    // Position in the tags listview
    int tagListViewPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo_viewer);

        displayOfPhoto = findViewById(R.id.displayImageView);
        backToPhotoListButton = findViewById(R.id.backToPhotoListButton);
        addTagButton = findViewById(R.id.addTagButton);
        removeTagButton = findViewById(R.id.displayPhotoButton);
        tagsListView = findViewById(R.id.tagsListView);
        tagTypeSpinner = findViewById(R.id.tagTypeSpinner);
        tagValueEditText = findViewById(R.id.tagValueEditText);
        slideshowDisplayButton = findViewById(R.id.slideshowDisplayButton);

        String[] items = new String[]{"Location", "Person"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        tagTypeSpinner.setAdapter(adapter);

        // Read the save data
        load();
        save();

        Bundle bundle = getIntent().getExtras();
        String currentAlbumName = bundle.getString("albumName");
        String currentPhotoUri = (String) bundle.get("photoUri");
        position = bundle.getInt("position");

        currentAlbum = user.getAlbum(currentAlbumName);
        currentPhoto = currentAlbum.getPhotoByUri(currentPhotoUri);

        displayOfPhoto.setImageURI(Uri.parse(currentPhotoUri));

        refreshTagsListview();

        // Add tag button is clicked
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tagType = tagTypeSpinner.getSelectedItem().toString();
                String tagValue = tagValueEditText.getText().toString();

                if (tagValue.trim().equals(""))
                {
                    return;
                }

                if (currentPhoto.checkIfTagExists(tagType, tagValue))
                {
                    Toast.makeText(DisplayPhotoViewer.this, "Tag already exists for this photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentPhoto.addTag(tagType, tagValue);
                Toast.makeText(DisplayPhotoViewer.this, "Tag added", Toast.LENGTH_SHORT).show();

                tagValueEditText.setText("");

                save();

                refreshTagsListview();
            }
        });

        // Remove tag button is clicked
        removeTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tagListViewPosition == -1)
                {
                    Toast.makeText(DisplayPhotoViewer.this, "Please click on a tag first", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentPhoto.getTagList().remove(tagListViewPosition);
                Toast.makeText(DisplayPhotoViewer.this, "Tag removed", Toast.LENGTH_SHORT).show();

                save();

                tagListViewPosition = -1;

                refreshTagsListview();
            }
        });

        // Back button is clicked
        backToPhotoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("albumName", currentAlbumName);
                Intent intent = new Intent(DisplayPhotoViewer.this, PhotoListViewer.class);
                intent.putExtras(bundle);
                startActivity(intent);

                save();
                finish();
            }
        });

        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tagListViewPosition = position;
            }
        });

        // Slideshow button is clicked
        slideshowDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("albumName", currentAlbumName);
                bundle.putInt("position", position);
                Intent intent = new Intent(DisplayPhotoViewer.this, SlideshowViewer.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * Refreshes the listview of tags for the current photo
     */
    public void refreshTagsListview()
    {
        List<String> tagStrings = new ArrayList<>();

        List<Tag> tags = currentPhoto.getTagList();

        // Get the album names to put into the listview
        for(int i = 0; i < tags.size(); i++)
        {
            tagStrings.add(tags.get(i).toString());
        }

        // Build the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(DisplayPhotoViewer.this, android.R.layout.simple_list_item_1, tagStrings);
        tagsListView = findViewById(R.id.tagsListView);
        tagsListView.setAdapter(adapter);
    }

    /**
     * Loads the user's data
     */
    private void load()
    {
        try {
            user = User.readApp(DisplayPhotoViewer.this);
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(DisplayPhotoViewer.this, "Save file not found, creating new save", Toast.LENGTH_SHORT).show();
            user = new User();
        }
    }

    /**
     * Saves the user's data
     */
    private void save()
    {
        try {
            user.writeApp(DisplayPhotoViewer.this);
        } catch (IOException e) {
            Toast.makeText(DisplayPhotoViewer.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}