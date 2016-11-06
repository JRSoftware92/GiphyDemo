package com.example.giphydemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giphydemo.R;
import com.example.giphydemo.adapter.ImageRecycleAdapter;
import com.example.giphydemo.interfaces.ImageListListener;
import com.example.giphydemo.model.GiphyImage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageListListener} interface
 * to handle interaction events.
 * Use the {@link StaggeredImageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaggeredImageListFragment extends Fragment {

    private static final String ARG_IMAGES = "com.example.images";
    private static final String ARG_NUM_COLUMNS = "com.example.columns";

    private ImageRecycleAdapter adapter;
    private ImageListListener listener;

    private int numColumns;
    private ArrayList<GiphyImage> images;

    public StaggeredImageListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param images Array of Images to be displayed
     * @return A new instance of fragment StaggeredImageListFragment.
     */
    public static StaggeredImageListFragment newInstance(GiphyImage[] images) {
        StaggeredImageListFragment fragment = new StaggeredImageListFragment();
        if(images == null || images.length < 1)
            return fragment;

        Bundle args = new Bundle();
        args.putParcelableArray(ARG_IMAGES, images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImageListListener) {
            listener = (ImageListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ImageListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            GiphyImage[] imageArr = (GiphyImage[]) args.getParcelableArray(ARG_IMAGES);
            if(imageArr != null)
                images = new ArrayList<>(Arrays.asList(imageArr));
            else
                images = new ArrayList<>();
            numColumns = args.getInt(ARG_NUM_COLUMNS, 3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_image_list, container, false);
        Context context = rootView.getContext();
        RecyclerView imageRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_list_images);

        if(imageRecyclerView != null && images != null) {
            RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
            adapter = new ImageRecycleAdapter(context, images);
            adapter.setListener(listener);

            imageRecyclerView.setAdapter(adapter);
            imageRecyclerView.setLayoutManager(layoutManager);
            imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //If RecyclerView has been scrolled to the bottom - Request more Data
                    if(!recyclerView.canScrollVertically(1))
                        listener.onListScrolled(images.size());
                }
            });
        }

        return rootView;
    }

    /**
     * Clears the contents of the image list
     */
    public void clearList(){
        this.images.clear();
        if(adapter != null)
            adapter.clear();
    }

    /**
     * Adds an Array of GiphyImage objects to the current list
     */
    public void updateList(GiphyImage[] images) {
        if(images == null || images.length < 1)
            return;

        this.images.addAll(Arrays.asList(images));
        if(adapter != null)
            adapter.addAll(images);
    }
}
