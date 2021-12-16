package com.example.photosandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Album;
import com.example.model.Photo;
import com.example.model.User;

import java.io.IOException;
import java.util.List;

public class PhotoListViewer extends AppCompatActivity {

    // Title for the window
    TextView PhotoListTitle;

    // Request code
    final int REQUEST_CODE = 50;

    // Keeps track of the save data
    User user = new User();

    // Current album being looked at
    Album currentAlbum;

    // Photos of the album
    List<Photo> listOfPhotos;

    // Buttons
    Button addPhotoButton;
    Button deletePhotoButton;
    Button backToAlbumListButton;
    Button movePhotoButton;
    Button displayPhotoButton;

    // EditText for moving photo to another album
    EditText albumToMoveToEditText;

    // GridView of photos
    GridView photosGridView;

    // ImageAdapter that will be used for the gridview
    ImageAdapter imageAdapter = new ImageAdapter(PhotoListViewer.this);

    // Keeps track of the position selected in the gridview
    // It is -1 by default when nothing is selected
    int gridviewPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list_viewer);

        PhotoListTitle = findViewById(R.id.PhotoListTitle);
        addPhotoButton = findViewById(R.id.addPhotoButton);
        photosGridView = findViewById(R.id.photosGridView);
        deletePhotoButton = findViewById(R.id.deletePhotoButton);
        backToAlbumListButton = findViewById(R.id.backToAlbumListButton);
        movePhotoButton = findViewById(R.id.movePhotoButton);
        displayPhotoButton = findViewById(R.id.displayPhotoButton);
        albumToMoveToEditText = findViewById(R.id.albumToMoveToEditText);

        // Read the save data
        load();
        save();

        // Get album name
        Bundle bundle = getIntent().getExtras();
        String currentAlbumName = bundle.getString("albumName");

        // Set title of the screen
        PhotoListTitle.setText(currentAlbumName + " Photos");

        // Get album and get photos of the album
        currentAlbum = user.getAlbum(currentAlbumName);
        listOfPhotos = currentAlbum.getPhotos();

        // Populate gridview
        populateGridView();

        // Add button is clicked
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // Delete button in clicked
        deletePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridviewPosition == -1)
                {
                    Toast.makeText(PhotoListViewer.this, "Please click on a photo first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uriOfPhotoToDelete = listOfPhotos.get(gridviewPosition).getPhotoUri();
                currentAlbum.removePhoto(uriOfPhotoToDelete);

                save();

                gridviewPosition = -1;
                populateGridView();
            }
        });

        // Back to album list button is clicked
        backToAlbumListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();

                Intent intent = new Intent(PhotoListViewer.this, AlbumListViewer.class);
                startActivity(intent);

                finish();
            }
        });

        // Display photo button is pressed
        displayPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridviewPosition == -1)
                {
                    Toast.makeText(PhotoListViewer.this, "Please click on a photo first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("albumName", currentAlbumName);
                String photoUri = listOfPhotos.get(gridviewPosition).getPhotoUri();
                bundle.putString("photoUri", photoUri);
                bundle.putInt("position", gridviewPosition);
                Intent intent = new Intent(PhotoListViewer.this, DisplayPhotoViewer.class);
                intent.putExtras(bundle);
                startActivity(intent);

                gridviewPosition = -1;
            }
        });

        // Move photo button is pressed
        movePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridviewPosition == -1)
                {
                    Toast.makeText(PhotoListViewer.this, "Please click on a photo first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Photo photoToMove = listOfPhotos.get(gridviewPosition);

                String albumName = albumToMoveToEditText.getText().toString();

                if (!user.doesAlbumNameExist(albumName))
                {
                    Toast.makeText(PhotoListViewer.this, "Album name does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                Album albumToMoveTo = user.getAlbum(albumName);

                if (albumToMoveTo.isPhotoUriPresent(photoToMove.getPhotoUri()))
                {
                    Toast.makeText(PhotoListViewer.this, "Album already has this photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                albumToMoveTo.addPhoto(photoToMove);
                currentAlbum.removePhoto(photoToMove.getPhotoUri());

                Toast.makeText(PhotoListViewer.this, "Photo moved", Toast.LENGTH_SHORT).show();

                save();

                gridviewPosition = -1;

                populateGridView();
            }
        });

        // A photo in the gridview is clicked
        photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridviewPosition = position;
            }
        });
    }

    /**
     * This method is called when adding a photo and checks to make sure there are no problems when adding a photo
     *
     * @param requestCode   Should be 50
     * @param resultCode    Checks if the result is ok
     * @param photoData     The photo that is being added
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent photoData) {

        super.onActivityResult(requestCode, resultCode, photoData);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            Uri photoUri = photoData.getData();

            // Used to check which version of the build is running
            // This is so Uri can be accessed in the long term
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
            { getContentResolver().takePersistableUriPermission
                    (photoUri, Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION); }

            // Check if photo is already present in the album
            if (currentAlbum.isPhotoUriPresent(photoUri.toString()))
            {
                Toast.makeText(PhotoListViewer.this, "Photo already in album", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Photo photoToAdd = new Photo(photoUri.toString());
                currentAlbum.addPhoto(photoToAdd);

                Toast.makeText(PhotoListViewer.this, "Photo added", Toast.LENGTH_SHORT).show();

                save();
            }
        }
        else
        {
            Toast.makeText(PhotoListViewer.this, "Photo Not Added", Toast.LENGTH_SHORT).show();
        }

        gridviewPosition = -1;
        populateGridView();
    }

    /**
     * This method will be called to populate the gridview with the images of the album
     */
    public void populateGridView()
    {
        imageAdapter.setPhotos(listOfPhotos);
        photosGridView.setAdapter(imageAdapter);
    }

    /**
     * Loads the user's data
     */
    private void load()
    {
        try {
            user = User.readApp(PhotoListViewer.this);
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(PhotoListViewer.this, "Save file not found, creating new save", Toast.LENGTH_SHORT).show();
            user = new User();
        }
    }

    /**
     * Saves the user's data
     */
    private void save()
    {
        try {
            user.writeApp(PhotoListViewer.this);
        } catch (IOException e) {
            Toast.makeText(PhotoListViewer.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}