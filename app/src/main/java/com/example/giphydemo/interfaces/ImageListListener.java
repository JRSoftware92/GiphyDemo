package com.example.giphydemo.interfaces;

import com.example.giphydemo.model.GiphyImage;

/**
 * Created by jriley on 11/3/16.
 * Interface for handling image selections
 */

public interface ImageListListener {
    /**
     * Called when a GiphyImage is selected from a list
     * @param image - GiphyImage object being selected
     */
    void onImageSelected(GiphyImage image);

    /**
     * Called when the user scrolls the list requesting more data
     */
    void onListScrolled(int itemsShown);
}
