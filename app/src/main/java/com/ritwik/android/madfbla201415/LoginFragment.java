package com.ritwik.android.madfbla201415;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.client.*;

import org.shaded.apache.commons.codec.digest.DigestUtils;

public class LoginFragment extends Fragment {

    private Button mSignInButton;
    private TextView mSignUpButtonView;
    private EditText mLoginField, mPassField;
    private Firebase ref;

    //Constants
    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";
    private static final String LOG_TAG = "LoginPhase";

    public LoginFragment() {
        //empty constructor refer to onCreateView
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginField = (EditText) rootView.findViewById(R.id.login_username);
        mPassField = (EditText) rootView.findViewById(R.id.login_password);
        mSignInButton = (Button) rootView.findViewById(R.id.sign_in_button);
        mSignUpButtonView = (TextView) rootView.findViewById(R.id.sign_up_buttonview);

        Firebase.setAndroidContext(getActivity());
        ref = new Firebase(URL_FIREBASE);

        //creating anonymous inner classes for click events
        mSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ref.authWithPassword(mLoginField.getText().toString(), mPassField.getText().toString() , new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                       //Auth Successful
                       //TODO: Move to HomeScreen
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                       //Auth Error
                       //TODO: Display somehow :  firebaseError.getMessage();
                    }
                });


                //Read about Explicit Intents at
                // http://developer.android.com/guide/components/intents-filters.html
                //used to start activities (like this) and send data across activities

                Intent homepageIntent = new Intent(getActivity(), HomepageActivity.class);
                startActivity(homepageIntent);

            }

        });

        mSignUpButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Switching fragments (View) inside LoginActivity using support fragment manager
                //replacing fragment container (the activity's root view) with new SignupFragment
                //addToBackStack is called so that user can press back to go back to LoginFragment

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new SignupFragment())
                        .addToBackStack(null).commit();

            }

        });

        /*
        For reference
        Testing adding values with distinct key pairs
        Map<String, Object> mapp = new HashMap<String, Object>();
        mapp.put("runtimeMessage3", "more data");
        mapp.put("runtimeMessage4", "more data");
        ref.child("announcements").child("current").updateChildren(mapp);
        */

        return rootView;
    }
}
