package com.example.model;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class will keep track of each of the individual photos.
 *
 * @author Amit Patel, Hideyo Sakamoto
 *
 */
public class Photo implements Serializable{

	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -1541823478468036214L;

	/**
	 * List of tags for the photo
	 */
	private List<Tag> photoTags;

	/**
	 * The Uri for the photo as a string
	 */
	private String photoUriString;

	/**
	 * Constructor for Photo
	 *
	 * @param photoUriString	The Uri string of the photo
	 */
	public Photo(String photoUriString)
	{
		this.photoUriString = photoUriString;
		photoTags = new ArrayList<Tag>();
	}

	/**
	 * Returns the Uri string of the photo
	 *
	 * @return	Uri stringof the photo
	 */
	public String getPhotoUri()
	{
		return this.photoUriString;
	}

	/**
	 * Creates a new tag from the given parameters and then adds the tag to the Photo's list of tags.
	 * If the tag is a location tag, locationTagsAmount is set to 1, preventing the user from making
	 * anymore location tags for this photo.
	 *
	 * @param tagType	Type of the tag
	 * @param tagValue	Value of the tag
	 */
	public void addTag(String tagType, String tagValue)
	{
		Tag tagToAdd = new Tag(tagType, tagValue);

		this.photoTags.add(tagToAdd);
	}

	/**
	 * This method is called when a user is trying to remove a tag from a photo.
	 * If the tag is a location tag, locationTagsAmount is set to 0, allowing the user to make another
	 * location tag.
	 *
	 * @param tagType	The type of the tag
	 * @param tagValue	The value of the tag
	 */
	public void removeTag(String tagType, String tagValue)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType) &&
					photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue))
			{
				photoTags.remove(i);
				break;
			}
		}
	}

	/**
	 * Return the list of tags of the photo
	 *
	 * @return	Return the list of tags of the photo
	 */
	public List<Tag> getTagList()
	{
		return this.photoTags;
	}

	/**
	 * Checks to see if the new tag already exists for this photo.
	 * There should not be any duplicate tags.
	 *
	 * @param newTagType	Name of the tag's type that is trying to be added
	 * @param newTagValue	Name of the tag's value that is trying to be added
	 * @return	True if the pair already exists, false otherwise
	 */
	public boolean checkIfTagExists(String newTagType, String newTagValue)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(newTagType) &&
					photoTags.get(i).getTagValue().equalsIgnoreCase(newTagValue))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * This method is used when the user is trying to find photos with a certain tag pair.
	 * If the tag matches, the photo should then be added to the search results.
	 *
	 * @param tagType	Name of the tag's type that is being searched for
	 * @param tagValue	Name of the tag's value that is being searched for
	 * @return	True if the tag matches, false otherwise
	 */
	public boolean searchTagsOnePair(String tagType, String tagValue)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType) &&
					photoTags.get(i).getTagValue().toLowerCase().startsWith(tagValue.toLowerCase()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks to see if the photo has both of the tag pairs.
	 * This method will be used when searching for photos that have two user defined tag pairs.
	 *
	 * @param tagType1	Name of the first tag's type that is being searched for
	 * @param tagValue1	Name of the first tag's value that is being searched for
	 * @param tagType2	Name of the second tag's type that is being searched for
	 * @param tagValue2	Name of the second tag's value that is being searched for
	 * @return	True if both of the tag pairs are found, false otherwise
	 */
	public boolean searchTagsTwoPairsAnd(String tagType1, String tagValue1, String tagType2, String tagValue2)
	{
		int matches = 0;

		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType1) &&
					photoTags.get(i).getTagValue().toLowerCase().startsWith(tagValue1.toLowerCase()))
			{
				matches++;
			}
			else if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType2) &&
					photoTags.get(i).getTagValue().toLowerCase().startsWith(tagValue2.toLowerCase()))
			{
				matches++;
			}
		}

		if (matches == 2)
		{
			return true;
		}

		return false;
	}

	/**
	 * Checks to see if the photo has at least one of the tag pairs.
	 * This methid is used when the user is searching for photos with at least one tag pair
	 * between the two tag pairs given by the user.
	 *
	 * @param tagType1	Name of the first tag's type that is being searched for
	 * @param tagValue1	Name of the first tag's value that is being searched for
	 * @param tagType2	Name of the second tag's type that is being searched for
	 * @param tagValue2	Name of the second tag's value that is being searched for
	 * @return	True if at least one of the tag pairs are found, false otherwise
	 */
	public boolean searchTagsTwoPairsOr(String tagType1, String tagValue1, String tagType2, String tagValue2)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType1) &&
					photoTags.get(i).getTagValue().toLowerCase().startsWith(tagValue1.toLowerCase()) ||
					photoTags.get(i).getTagType().equalsIgnoreCase(tagType2) &&
							photoTags.get(i).getTagValue().toLowerCase().startsWith(tagValue2.toLowerCase()))
			{
				return true;
			}
		}

		return false;
	}
}
