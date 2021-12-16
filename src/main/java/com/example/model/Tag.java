package com.example.model;

import java.io.Serializable;

/**
 * This class will handle the tags values for each picture.
 * A picture may have more than one tag, but it may not have any duplicate tags where 
 * both parts of the tag are the same as another tag.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class Tag implements Serializable{

	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = 5300775339737090893L;

	/**
	 * The type of tag, such as location
	 */
	private String tagType;
	
	/**
	 * The value of tag, such as the name of a person
	 */
	private String tagValue;
	
	/**
	 * Constructor for the Tag class. Takes in two parameters, the type of the tag
	 * and the value of the tag
	 * 
	 * @param type	Type of the tag, such as location
	 * @param value	Value of the tag, such as the name of a person
	 */
	public Tag(String type, String value)
	{
		this.tagType = type;
		this.tagValue = value;
	}
	
	/**
	 * Return the type of the tag
	 * 
	 * @return	Returns the type of a tag as a string
	 */
	public String getTagType()
	{
		return this.tagType;
	}
	
	/**
	 * Return the value of the tag
	 * 
	 * @return	Returns the value of the tag as a string
	 */
	public String getTagValue()
	{
		return this.tagValue;
	}
	
	/**
	 * Turns the tag into a string to allow it to be displayed on the UI
	 * 
	 * @return	A string consisting of tagType and tagValue with a few spaces and a dash separating them
	 */
	public String toString()
	{
		return tagType + " - " + tagValue;
	}
}
