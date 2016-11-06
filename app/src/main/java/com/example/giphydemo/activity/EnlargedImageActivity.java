package com.example.giphydemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.giphydemo.R;
import com.example.giphydemo.fragment.EnlargedImageFragment;
import com.example.giphydemo.model.GiphyImage;

/**
 * Activity for displaying an EnlargedImageFragment given an image URL
 */
public class EnlargedImageActivity extends AppCompatActivity {

    public static final String ARG_IMAGE = "com.example.image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged_image);

        //Retrieves the Image URL
        Intent intent = getIntent();
        GiphyImage image = intent.getParcelableExtra(ARG_IMAGE);

        //Instantiates the Image Fragment
        EnlargedImageFragment fragment = EnlargedImageFragment.newInstance(image);

        //Adds the Fragment to the FrameLayout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_fragment, fragment);
        fragmentTransaction.commit();
    }
}
