package com.example.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle the album itself, which consists of the album name and
 * a list of pictures of the album. We will be able to add and remove pictures.
 *
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class Album implements Serializable{

	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -7545450350976397396L;

	/**
	 * This list will contain all of the photos in the album
	 */
	private List<Photo> photosOfAlbum;

	/**
	 * Name of the album
	 */
	private String albumName;

	/**
	 * Keeps tracks of all of the image uris, or image directories, for all photos within the album.
	 * All uris should be unique within an album.
	 */
	private List<String> allPhotoUrisForAlbum = new ArrayList<>();

	/**
	 * Constructor to create a new album
	 *
	 * @param albumName	The name of the album
	 */
	public Album(String albumName)
	{
		this.albumName = albumName;
		photosOfAlbum = new ArrayList<>();
	}

	/**
	 * Takes an index and returns the photo at the index
	 * @param index the index of photo in album
	 * @return photo at the given index
	 */
	public Photo getPhoto(int index){
		return this.photosOfAlbum.get(index);
	}

	public Photo getPhotoByUri(String photoUriString)
	{
		for (int i = 0; i < photosOfAlbum.size(); i++)
		{
			if (photosOfAlbum.get(i).getPhotoUri().equals(photoUriString))
			{
				return photosOfAlbum.get(i);
			}
		}

		return null;
	}

	/**
	 * Changes the name of the album
	 *
	 * @param newAlbumName	The name of the album to change to
	 */
	public void changeAlbumName(String newAlbumName)
	{
		this.albumName = newAlbumName;
	}

	/**
	 * Returns the name of the album
	 *
	 * @return	Returns the name of the album
	 */
	public String getAlbumName()
	{
		return this.albumName;
	}

	/**
	 * Returns the size of the current album
	 *
	 * @return	The size of the album as an int
	 */
	public int getAlbumSize()
	{
		return photosOfAlbum.size();
	}

	/**
	 * Adds a photo to the album
	 *
	 * @param photoToAdd	The picture that will be added to the album
	 */
	public void addPhoto(Photo photoToAdd)
	{
		photosOfAlbum.add(photoToAdd);
		addPhotoUri(photoToAdd.getPhotoUri());
	}

	/**
	 * Removes a picture from the album
	 *
	 * @param photoUriToRemove	Uri of the picture to remove
	 */
	public void removePhoto(String photoUriToRemove)
	{
		for (int i = 0; i < photosOfAlbum.size(); i++)
		{
			if (photosOfAlbum.get(i).getPhotoUri().equals(photoUriToRemove))
			{
				photosOfAlbum.remove(i);
				removePhotoUri(photoUriToRemove);
				break;
			}
		}
	}

	/**
	 * Returns the pictures of the album
	 *
	 * @return	Pictures of the album
	 */
	public List<Photo> getPhotos()
	{
		return this.photosOfAlbum;
	}

	/**
	 * Returns certain pieces of info of the album as a string
	 *
	 * @return A string
	 */
	public String toString()
	{
		if (this.getAlbumSize() == 0)
		{
			return "Name: " + this.albumName;
		}

		return "Name: " + this.albumName;
	}

	/**
	 * Adds the Uri for the photo to the list of Uris.
	 * All Uris need to be unique within an album.
	 *
	 * @param photoUri	Name of the photo Uri that is going to be added
	 */
	public void addPhotoUri(String photoUri)
	{
		allPhotoUrisForAlbum.add(photoUri);
	}

	/**
	 * Checks to see if a photo Uri is present in the list of Uris.
	 * All Uris need to be unique within an album.
	 *
	 * @param photoUri	New potential photo Uri
	 * @return	True if the Uri is used by another photo, false otherwise
	 */
	public boolean isPhotoUriPresent(String photoUri)
	{
		for (String uri: allPhotoUrisForAlbum)
		{
			if (uri.equals(photoUri))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Removes a photo's Uri from the list of Uris if a photo is removed from an album.
	 *
	 * @param photoUri	Name of the photo Uri that is going to be removed
	 */
	public void removePhotoUri(String photoUri)
	{
		for (int i = 0; i < allPhotoUrisForAlbum.size(); i++)
		{
			if (allPhotoUrisForAlbum.get(i).equals(photoUri))
			{
				allPhotoUrisForAlbum.remove(i);
				break;
			}
		}
	}
}
