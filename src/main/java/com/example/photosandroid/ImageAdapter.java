package com.example.photosandroid;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;

    private List<Photo> photos = new ArrayList<>();

    /**
     * Constructor
     * @param context   The context
     */
    public ImageAdapter(Context context)
    {
        this.context = context;
    }

    /**
     * Set the List of photos
     *
     * @param photos    list of photos
     */
    public void setPhotos(List<Photo> photos)
    {
        this.photos = photos;
    }

    /**
     * Gets size of list of photos
     *
     * @return  Size of list of photos
     */
    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView != null)
        {
            imageView = (ImageView) convertView;
        }
        else
        {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300,300));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4,4,4,4);
        }

        imageView.setImageURI(Uri.parse(photos.get(position).getPhotoUri()));

        return imageView;
    }
}
