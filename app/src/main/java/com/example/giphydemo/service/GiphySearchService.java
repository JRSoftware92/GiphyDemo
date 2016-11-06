package com.example.giphydemo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.giphydemo.model.GiphyImage;
import com.example.giphydemo.model.GiphyQueryBuilder;
import com.example.giphydemo.utils.AppUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by jriley on 11/3/16.
 * Service for submitting a query to the Giphy API and processing the results
 */

public class GiphySearchService extends IntentService {

    private static final String SERVICE_TAG = "com.example.giphydemo.service";

    public static final int EVENT_REQUEST_SUCCESSFUL = 0;
    public static final int EVENT_REQUEST_FAILED = -1;
    public static final int EVENT_RESPONSE_UNSUCCESSFUL = -2;
    public static final int EVENT_NETWORK_UNAVAILABLE = -3;
    public static final int EVENT_RESPONSE_EXCEPTION = -4;

    public static final String ARG_SEARCH_TERMS = "com.example.giphySearchTerms";
    public static final String ARG_OFFSET = "com.example.giphySearchOffset";
    public static final String BROADCAST_RESULTS = "com.example.giphyservice.receiver";
    public static final String RESULT_IMAGES = "com.example.giphyService.results";
    public static final String RESULT_CODE = "com.example.giphyService.resultcode";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GiphySearchService() {
        super(SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null){
            try {
                String search = extras.getString(ARG_SEARCH_TERMS);
                int offset = extras.getInt(ARG_OFFSET);
                String query = new GiphyQueryBuilder(this)
                        .search(search)
                        .offset(offset)
                        .build();
                handleSearchQuery(query);
            }
            catch(Exception e){
                Toast.makeText(this, "Unsupported URL Encoding used.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void publishResults(GiphyImage[] images, int resultCode) {
        Intent intent = new Intent(BROADCAST_RESULTS);
        intent.putExtra(RESULT_IMAGES, images);
        intent.putExtra(RESULT_CODE, resultCode);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void handleSearchQuery(String query){
        final Context context = getApplicationContext();

        if(AppUtils.isNetworkAvailable(context)){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(query).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    publishResults(null, EVENT_REQUEST_FAILED);     //Request Failed
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {      //Publish Successful Results
                            GiphyImage[] images = AppUtils.getGiphyObjectsFromSearch(jsonData);
                            publishResults(images, EVENT_REQUEST_SUCCESSFUL);
                        }
                        else {      //Unsuccessful Response
                            publishResults(null, EVENT_RESPONSE_UNSUCCESSFUL);
                        }
                    }
                    catch(Exception e){     //Response Exception
                        publishResults(null, EVENT_RESPONSE_EXCEPTION);
                    }
                }
            });
        }
        else{       //Network Unavailable
            publishResults(null, EVENT_NETWORK_UNAVAILABLE);
        }
    }
}
