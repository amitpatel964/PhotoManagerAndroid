package com.example.photosandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.Album;
import com.example.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumListViewer extends AppCompatActivity {

    // The user class that holds the list of albums
    User user = new User();
    List<Album> listOfAlbums;

    // Buttons, input field and listview
    Button addAlbumButton, renameAlbumButton, deleteAlbumButton, openAlbumButton, searchButton;
    EditText albumNameInput;
    ListView albumListView;

    // What will be shown in the listview
    List<String> albumNames = new ArrayList<>();

    // Keeps track of the position of the listview the user selects
    int listviewPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list_viewer);

        addAlbumButton = findViewById(R.id.addAlbumButton);
        renameAlbumButton = findViewById(R.id.renameAlbumButton);
        deleteAlbumButton = findViewById(R.id.deleteAlbumButton);
        openAlbumButton = findViewById(R.id.openAlbumButton);
        albumNameInput = findViewById(R.id.albumNameInput);
        searchButton = findViewById(R.id.searchButton);

        load();
        save();

        listOfAlbums = user.getListOfAlbums();
        refreshAlbumListView();

        // Add button is clicked
        addAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = albumNameInput.getText().toString();

                // Check if album name already exists
                if (user.doesAlbumNameExist(input))
                {
                    Toast.makeText(AlbumListViewer.this, "Album name already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add album
                if (!input.trim().equals(""))
                {
                    Album albumToAdd = new Album(input);
                    listOfAlbums.add(albumToAdd);
                    Toast.makeText(AlbumListViewer.this, "Album added", Toast.LENGTH_SHORT).show();
                    listviewPosition = -1;
                }

                save();
                albumNameInput.setText("");
                refreshAlbumListView();
            }
        });

        // Rename button is clicked
        renameAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = albumNameInput.getText().toString();

                // Check if listview was clicked first
                if (listviewPosition == -1)
                {
                    Toast.makeText(AlbumListViewer.this, "Please select an album in the list first", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if album name already exists
                if (user.doesAlbumNameExist(input))
                {
                    Toast.makeText(AlbumListViewer.this, "Album name already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Rename album
                if (!input.trim().equals(""))
                {
                    listOfAlbums.get(listviewPosition).changeAlbumName(input);
                    Toast.makeText(AlbumListViewer.this, "Album name changed", Toast.LENGTH_SHORT).show();
                    listviewPosition = -1;
                }

                save();
                albumNameInput.setText("");
                refreshAlbumListView();
            }
        });

        // An item in the album listview in selected
        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listviewPosition = position;
                albumNameInput.setText(listOfAlbums.get(position).getAlbumName());
            }
        });

        // Delete button is clicked
        deleteAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = albumNameInput.getText().toString();

                // Check if album name exists
                if (!user.doesAlbumNameExist(input))
                {
                    Toast.makeText(AlbumListViewer.this, "Album name does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Delete album
                if (!input.trim().equals(""))
                {
                    user.removeAlbum(input);
                    Toast.makeText(AlbumListViewer.this, "Album deleted", Toast.LENGTH_SHORT).show();
                    listviewPosition = -1;
                }

                save();
                albumNameInput.setText("");
                refreshAlbumListView();
            }
        });

        // Open button is clicked
        openAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = albumNameInput.getText().toString();

                // Check if album name exists
                if (!user.doesAlbumNameExist(input))
                {
                    Toast.makeText(AlbumListViewer.this, "Album name does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Open album
                if (!input.trim().equals(""))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("albumName", input);
                    Intent intent = new Intent(AlbumListViewer.this, PhotoListViewer.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        // Search button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumListViewer.this, SearchCriteriaViewer.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method will be called to refresh the listview of album names
     */
    private void refreshAlbumListView()
    {
        // Clear the albumNames List first
        albumNames.clear();

        // Get the album names to put into the listview
        for(int i = 0; i < listOfAlbums.size(); i++)
        {
            albumNames.add(listOfAlbums.get(i).toString());
        }

        // Build the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AlbumListViewer.this, android.R.layout.simple_list_item_1, albumNames);
        albumListView = findViewById(R.id.albumListView);
        albumListView.setAdapter(adapter);
    }

    /**
     * Loads the user's data
     */
    private void load()
    {
        try {
            user = User.readApp(AlbumListViewer.this);
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(AlbumListViewer.this, "Save file not found, creating new save", Toast.LENGTH_SHORT).show();
            user = new User();
        }
    }

    /**
     * Saves the user's data
     */
    private void save()
    {
        try {
            user.writeApp(AlbumListViewer.this);
        } catch (IOException e) {
            Toast.makeText(AlbumListViewer.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}