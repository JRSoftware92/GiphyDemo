package com.example.giphydemo.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;

import com.example.giphydemo.R;
import com.example.giphydemo.fragment.StaggeredImageListFragment;
import com.example.giphydemo.interfaces.ImageListListener;
import com.example.giphydemo.model.GiphyImage;
import com.example.giphydemo.service.GiphySearchService;
import com.example.giphydemo.utils.AppUtils;

/**
 * Activity for displaying a StaggeredImageListFragment with the results of a Giphy Search Query
 */
public class SearchActivity extends AppCompatActivity implements ImageListListener {

    private FragmentManager fragmentManager;

    private LocalBroadcastManager broadcastManager;

    private ProgressDialog loadingDialog;

    private SearchView searchView;

    private String lastSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fragmentManager = getSupportFragmentManager();
        broadcastManager = LocalBroadcastManager.getInstance(this);

        //Registers the Broadcast Receiver for the Search Service
        IntentFilter filter = new IntentFilter();
        filter.addAction(GiphySearchService.BROADCAST_RESULTS);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        //Initialize Views
        Button searchButton = (Button) findViewById(R.id.button_search);
        searchView = (SearchView) findViewById(R.id.searchview_images);

        //Initialize Search Button on Click Listener
        if(searchButton != null)
            searchButton.setOnClickListener(searchClickListener);

        //Initialize Loading Dialog
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading Search Results\u2026");
        loadingDialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onImageSelected(GiphyImage image) {
        //Sends the Selected Image Data to an EnlargedImageActivity
        Intent i = new Intent(this, EnlargedImageActivity.class);
        i.putExtra(EnlargedImageActivity.ARG_IMAGE, image);

        startActivity(i);
    }

    @Override
    public void onListScrolled(int itemsShown) {
        //Sends a Search Request for the next set of results for the most recent search
        sendSearchRequest(lastSearch, itemsShown);
    }

    /**
     * Returns the Fragment object which is currently being displayed in the FrameLayout
     */
    protected Fragment getFragment(){
        return fragmentManager.findFragmentById(R.id.frame_fragment);
    }

    /**
     * Sets the fragment in the Fragment Frame
     */
    protected void setFragment(Fragment fragment){
        //Initializes the Transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Replaces and deletes the old fragment
        fragmentTransaction.replace(R.id.frame_fragment, fragment);
        fragmentTransaction.addToBackStack(null);

        //Commits the Changes
        fragmentTransaction.commit();
    }

    /**
     * Displays an array of giphy images via the StaggeredImageListFragment class
     * @param images - Array of GiphyImage objects to be displayed
     */
    protected void displayImages(GiphyImage[] images){
        if(images != null) {
            Fragment fragment = getFragment();
            if(fragment != null && fragment instanceof StaggeredImageListFragment)
                ((StaggeredImageListFragment) fragment).updateList(images);
            else
                setFragment(StaggeredImageListFragment.newInstance(images));
        }
    }

    /**
     * Submits a search request to the Giphy API if the terms are valid and haven't already
     * been searched.
     * @param searchTerms - String entry to be submitted as a search request
     * @param offset - Integer offset of results requested.
     */
    protected void sendSearchRequest(String searchTerms, int offset){
        //Validates Search Request
        if(searchTerms == null || searchTerms.length() < 1){
            return;
        }

        //Guards against Negative values
        if(offset < 0)
            offset = 0;

        //Trim whitespace and restrict to lower case entries
        searchTerms = searchTerms.trim().toLowerCase();

        //Remembers the search
        lastSearch = searchTerms;

        //If New Search - Clears the contents of the list from the previous search
        if(offset == 0) {
            Fragment fragment = getFragment();
            if (fragment != null && fragment instanceof StaggeredImageListFragment)
                ((StaggeredImageListFragment) fragment).clearList();
        }

        //Display Loading Dialog while search results are being sent
        loadingDialog.show();

        //Sends the Search Intent to the GiphySearchService
        Intent i = new Intent(this, GiphySearchService.class);
        i.putExtra(GiphySearchService.ARG_SEARCH_TERMS, searchTerms);
        i.putExtra(GiphySearchService.ARG_OFFSET, offset);
        startService(i);
    }

    /**
     * Handles Giphy Search Service Responses
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(GiphySearchService.BROADCAST_RESULTS)) {
                int resultCode = intent.getIntExtra(GiphySearchService.RESULT_CODE,
                        GiphySearchService.EVENT_RESPONSE_EXCEPTION);

                //Check the result code to determine how to handle the intent
                switch(resultCode){
                    case GiphySearchService.EVENT_REQUEST_SUCCESSFUL:
                        Bundle extras = intent.getExtras();
                        GiphyImage[] images = (GiphyImage[]) extras.getParcelableArray(GiphySearchService.RESULT_IMAGES);

                        //Dismiss loading dialog and display images
                        loadingDialog.dismiss();
                        displayImages(images);
                        break;
                    case GiphySearchService.EVENT_REQUEST_FAILED:
                        AppUtils.showToastLong(context, "Request Failed.");
                        break;
                    case GiphySearchService.EVENT_NETWORK_UNAVAILABLE:
                        AppUtils.showToastShort(context, "Network is currently unavailable.");
                        break;
                    case GiphySearchService.EVENT_RESPONSE_UNSUCCESSFUL:
                        AppUtils.showToastLong(context, "Response Unsuccessful.");
                        break;
                    case GiphySearchService.EVENT_RESPONSE_EXCEPTION:
                    default:
                        AppUtils.showToastLong(context, "Error has occurred with response.");
                        break;
                }
            }
        }
    };

    /**
     * Sends a Search Request to the GiphySearchService on the Search Button Click Event
     */
    private final View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(searchView != null) {
                CharSequence text = searchView.getQuery();
                if (text != null)
                    sendSearchRequest(text.toString(), 0);
            }
        }
    };
}
