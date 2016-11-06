package com.example.giphydemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giphydemo.R;
import com.example.giphydemo.model.GiphyImage;
import com.example.giphydemo.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnlargedImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnlargedImageFragment extends Fragment {

    private static final String ARG_IMAGE = "com.example.image";

    private GiphyImage image;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param image GiphyImage obj of the Image to be displayed
     * @return A new instance of fragment EnlargedImageFragment.
     */
    public static EnlargedImageFragment newInstance(GiphyImage image) {
        EnlargedImageFragment fragment = new EnlargedImageFragment();
        if(image == null)
            return fragment;

        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    public EnlargedImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null)
            image = args.getParcelable(ARG_IMAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_enlarged_image, container, false);
        Context context = rootView.getContext();
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview_enlarged_image);
        TextView textSource = (TextView) rootView.findViewById(R.id.textview_source);

        //Validate the GiphyImage object
        if(image != null){
            //Sets the Source Text for the Image
            String src = image.getSource();
            if(textSource != null && src != null && src.length() > 0)
                textSource.setText(String.format("Source: %s", src));

            //Load the Image into the ImageView
            if(imageView != null)
                AppUtils.loadImage(context, image, imageView);
        }


        return rootView;
    }
}
