package com.example.model;

import android.content.Context;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle the information for an individual user.
 * This class will contain a user's username and their albums.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class User implements Serializable{
	
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -3748439693527033733L;
	
	/**
	 * Keeps track of all of the albums a user has.
	 */
	private List<Album> albumList;
	
	/**
	 * The list of tag types for the user. Initially, there is only location and person,
	 * but the user is able to define their own types.
	 */
	private List<String> tagTypes;
	
	/**
	 * Keeps track of the album the user is currently looking at
	 */
	public Album currentAlbum;

	/**
	 * After a search is done, the search results are saved so that user can create
	 * an album from it if they choose to.
	 */
	public List<Photo> photoSearchResults;
	
	/**
	 * constructor makes an instance of user
	 */
	public User() {
		albumList = new ArrayList<Album>();
		tagTypes = new ArrayList<String>();
		tagTypes.add("Location");
		tagTypes.add("Person");
		currentAlbum = null;
		photoSearchResults = new ArrayList<Photo>();
	}
	
	/**
	 * This method is used to check if an album name already taken by another album for a user.
	 * All album names should be unique for a user.
	 * 
	 * @param albumNameToCheck	Name that is being checked
	 * @return	True if there is another album with the same name for the user, false otherwise
	 */
	public boolean doesAlbumNameExist(String albumNameToCheck)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			if (albumList.get(i).getAlbumName().equalsIgnoreCase(albumNameToCheck))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the album that the user is trying to access
	 * 
	 * @param name	Name of the album
	 * @return	The album the user is trying to access
	 */
	public Album getAlbum(String name)
	{
		Album albumToReturn = null;
		
		for (int i = 0; i < albumList.size(); i++)
		{
			if (albumList.get(i).getAlbumName().equalsIgnoreCase(name))
			{
				albumToReturn = albumList.get(i);
			}
		}
		
		return albumToReturn;
	}
	
	/**
	 * This method allows a user to remove an album from their list of albums
	 * 
	 * @param albumNameToRemove	Name of album to remove from list
	 */
	public void removeAlbum(String albumNameToRemove)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			if (albumList.get(i).getAlbumName().equalsIgnoreCase(albumNameToRemove))
			{
				albumList.remove(i);
				break;
			}
		}
	}

	/**
	 * Gets the list of albums for the user
	 *
	 * @return	The list of albums
	 */
	public List<Album> getListOfAlbums()
	{
		return this.albumList;
	}

	/**
	 * This method will be used when the user makes any changes in their program
	 * This will allow us to save changes the user made between sessions.
	 *
	 * @param context    Instance of the User class, which handles all of the data for the user
	 * @throws IOException    Any exceptions encountered
	 */
	public void writeApp(Context context) throws IOException
	{
		FileOutputStream object = context.openFileOutput("user.dat", Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(object);
		oos.writeObject(this);
		oos.close();
		object.close();
	}

	/**
	 * This method will be used to read the save data for the user..
	 * This will allow us to keep any changes in between sessions.
	 *
	 * @return	The instance of User, which holds the user
	 * @throws IOException	Any exceptions encounters
	 * @throws ClassNotFoundException	If the class is not found
	 */
	public static User readApp(Context context) throws IOException, ClassNotFoundException
	{
		FileInputStream object = context.openFileInput("user.dat");
		ObjectInputStream ois = new ObjectInputStream(object);
		User user = (User) ois.readObject();
		return user;
	}
}
