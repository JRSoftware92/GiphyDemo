package com.example.giphydemo.model;

import android.content.Context;

import com.example.giphydemo.R;
import com.example.giphydemo.utils.AppUtils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by jriley on 11/3/16.
 * Builder class for creating a Giphy API Query
 */

public class GiphyQueryBuilder {

    private Context context;

    //API Default = 0
    private int offset = -1;

    private String apiKey, apiHost, apiTarget;
    private String searchTerm;

    public GiphyQueryBuilder(Context context){
        this.context = context;

        this.apiKey = context.getString(R.string.api_key_giphy);
        this.apiHost = context.getString(R.string.api_url_giphy_base);
    }

    /**
     *
     * @return Query String used to send a Giphy Search Query
     */
    public String build(){
        //Build API Destination URL
        String dest = String.format("%s%s?api_key=%s", apiHost, apiTarget, apiKey);
        StringBuilder sb = new StringBuilder(dest);

        //Append Search Terms
        sb.append('&');
        sb.append(searchTerm);

        //Append Offset Argument
        //Uses US Locale by default
        if(offset > -1)
            sb.append(String.format(Locale.US, "&offset=%d", offset));

        return sb.toString();
    }

    /**
     * Appends encoded search terms to the search query
     */
    public GiphyQueryBuilder search(String term) throws UnsupportedEncodingException {
        this.apiTarget = context.getString(R.string.api_url_giphy_search);
        this.searchTerm = String.format("q=%s", AppUtils.encodeString(term));
        return this;
    }

    /**
     * Specifies an offset for the results of the search query
     */
    public GiphyQueryBuilder offset(int offset){
        this.offset = offset;
        return this;
    }
}
