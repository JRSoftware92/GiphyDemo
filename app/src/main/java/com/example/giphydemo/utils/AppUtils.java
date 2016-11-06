package com.example.giphydemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.giphydemo.model.Dimension;
import com.example.giphydemo.model.GiphyImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by jriley on 11/3/16.
 * Utility class for common methods used in the application
 */

public class AppUtils {

    /**
     * Displays a short Toast on screen using the message provided
     */
    public static void showToastShort(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a long Toast on screen using the message provided
     */
    public static void showToastLong(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @return True if a network connection can be established, false otherwise.
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Encodes String for URL Queries
     */
    public static String encodeString(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }

    /**
     * Loads a GIF into the requested ImageView based on the provided GiphyImage object
     */
    public static void loadImage(Context context, GiphyImage image, ImageView imageView){
        Dimension dimen = image.getSizeFull();
        Glide.with(context)
                .load(image.getUrl())
                .fitCenter()
                .override(dimen.width, dimen.height)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * Loads a small GIF into the requested ImageView based on the provided GiphyImage object
     */
    public static void loadGIFThumbnail(Context context, GiphyImage image, ImageView imageView){
        Dimension dimen = image.getSizeThumbnail();
        Glide.with(context)
                .load(image.getThumbnailUrl())
                .asBitmap()
                .fitCenter()
                .override(dimen.width, dimen.height)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    /**
     * Extracts a GiphyImage object from a JSON representation of the image
     */
    private static GiphyImage getGiphyObjectFromSearch(JSONObject imageObject) throws JSONException {
        JSONObject images = imageObject.getJSONObject("images");
        JSONObject original = images.getJSONObject("original");
        JSONObject thumbnail = images.getJSONObject("downsized_still");
        int width = original.getInt("width");
        int height = original.getInt("height");
        int widthSmall = thumbnail.getInt("width");
        int heightSmall = thumbnail.getInt("height");

        GiphyImage output = new GiphyImage();
        output.setUrl(original.getString("url"));
        output.setThumbnailUrl(thumbnail.getString("url"));
        output.setSource(imageObject.getString("source"));
        output.setSizeFull(new Dimension(width, height));
        output.setSizeThumbnail(new Dimension(widthSmall, heightSmall));

        return output;
    }

    /**
     * Extracts an array of GiphyImage objects from a JSON representation of the dataset
     */
    public static GiphyImage[] getGiphyObjectsFromSearch(String jsonOutput) throws JSONException {
        JSONObject result = new JSONObject(jsonOutput);
        JSONArray data = result.getJSONArray("data");

        if(data == null)
            return null;

        ArrayList<GiphyImage> images = new ArrayList<>();
        int length = data.length();
        GiphyImage image;
        Object entry;
        for(int i = 0; i < length; i++){

            entry = data.get(i);
            if(entry instanceof JSONObject){        //Found an Image
                image = getGiphyObjectFromSearch((JSONObject) entry);
                images.add(image);      //Adds the image to the output list
            }
        }

        GiphyImage[] output = new GiphyImage[images.size()];
        output = images.toArray(output);

        return output;
    }
}
