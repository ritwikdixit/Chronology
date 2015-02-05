package com.ritwik.android.madfbla201415;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by joshuazhou on 2015-02-01.
 */
public class HelpScreenSlidePageFragment extends Fragment {

    public static final String ARG_FEATURE = "feature";
    public static final String ARG_TEXT = "text";

    private TextView mFeature;
    private TextView mText;

    public HelpScreenSlidePageFragment() {

    }

    public static Fragment instance(String feature, String text) {
        Fragment fragment = new HelpScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString(HelpScreenSlidePageFragment.ARG_FEATURE, feature);
        args.putString(HelpScreenSlidePageFragment.ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_help, container, false);
        // attach to root?

        mFeature = (TextView)rootView.findViewById(R.id.feature_name);
        mFeature.setText(getArguments().getString(ARG_FEATURE));

        mText = (TextView)rootView.findViewById(R.id.help_name);
        mText.setText(getArguments().getString(ARG_TEXT));

        return rootView;
    }
}
