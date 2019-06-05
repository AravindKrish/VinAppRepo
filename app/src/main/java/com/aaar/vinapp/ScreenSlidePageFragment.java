package com.aaar.vinapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener{
    int mNum;
    private Button buttonGetStarted;

    static ScreenSlidePageFragment newInstance(int num, Context context) {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();



        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        ImageView tutorialImageView = rootView.findViewById(R.id.tutorialImageView);
        buttonGetStarted = (Button) rootView.findViewById(R.id.button_getstarted);
        buttonGetStarted.setOnClickListener(this);
        switch (mNum) {
            case 0:
                tutorialImageView.setImageResource(R.drawable.tutorialpage1);
                break;
            case 1:
                tutorialImageView.setImageResource(R.drawable.tutorialpage2);
                break;
            case 2:
                tutorialImageView.setImageResource(R.drawable.tutorialpage3);
                break;
            case 3:
                tutorialImageView.setImageResource(R.drawable.tutorialpage4);
                break;
            case 4:
                tutorialImageView.setImageResource(R.drawable.tutorialpage5);
                break;
            case 5:
                tutorialImageView.setImageResource(R.drawable.tutorialpage6);


        }

        return rootView;
    }


    @Override
    public void onClick(View view) {
ScreenSlidePagerActivity spActivity = (ScreenSlidePagerActivity) getActivity();
spActivity.onGetStartedButtonClick();
    }
}
