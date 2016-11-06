package com.example.giphydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giphydemo.R;
import com.example.giphydemo.interfaces.ImageListListener;
import com.example.giphydemo.model.GiphyImage;
import com.example.giphydemo.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jriley on 11/3/16.
 * RecyclerView Adapter for displaying a List of images
 */

public class ImageRecycleAdapter
        extends RecyclerView.Adapter<ImageRecycleAdapter.ViewHolder> {

    private Context context;

    private ImageListListener listener;

    private ArrayList<GiphyImage> images;

    private LayoutInflater inflater;

    public ImageRecycleAdapter(Context context, ArrayList<GiphyImage> images){
        super();
        this.context = context;
        this.images = images;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_image, parent, false);

        //Instantiates the ViewHolder with the item view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Sets Image and Binds Listener to ViewHolder
        final GiphyImage image = images.get(position);
        holder.setImage(image);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelected(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(images == null)
            return 0;
        return images.size();
    }

    /**
     * Adds an Array of Images to the List of Images
     */
    public void addAll(GiphyImage[] imageArr){
        if(imageArr == null || imageArr.length < 1)
            return;
        int lastIndex = images.size() - 1;
        images.addAll(Arrays.asList(imageArr));
        notifyItemInserted(lastIndex);
    }

    /**
     * Removes all Images from the List of Images
     */
    public void clear(){
        images.clear();
        notifyDataSetChanged();
    }

    public void setListener(ImageListListener listener){
        this.listener = listener;
    }

    private void onImageSelected(GiphyImage image){
        //Passes the Image to the Listener
        if(image != null && listener != null)
            listener.onImageSelected(image);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textSource;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_list_item);
            textSource = (TextView) itemView.findViewById(R.id.textview_source);
        }

        public void setImage(GiphyImage image){
            if(imageView != null && image != null) {
                setTextSource(image.getSource());

                //Loads the thumbnail image into the ImageView
                AppUtils.loadGIFThumbnail(context, image, imageView);
            }
        }

        /**
         * Sets the Source Text of the Image in the CardView
         * Only Displays the first 10 characters of the link (no http/https)
         */
        void setTextSource(String src){
            if(src != null && src.length() > 0 && textSource != null) {
                //Removes protocol specifications from the Source URL
                String newStr = src.replaceAll("http://|https://", "");

                //If the length is too large, display partial url wih ellipsis
                int length = newStr.length();
                if(length > 10)
                    newStr = newStr.substring(0, 10) + "\u2026";

                textSource.setText(String.format("Source: %s", newStr));
            }
        }

        /**
         * Sets the On Click Listener for the ImageView
         */
        void setOnClickListener(View.OnClickListener onClickListener){
            if(imageView != null)
                imageView.setOnClickListener(onClickListener);
        }
    }
}
