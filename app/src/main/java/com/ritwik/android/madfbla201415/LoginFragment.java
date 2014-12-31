package com.ritwik.android.madfbla201415;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private boolean isSuccess = false;

    //Constants
    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";
    private static final String LOG_TAG = "LoginPhase";

    public LoginFragment() {
        //empty constructor refer to onCreateView
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Firebase.setAndroidContext(getActivity());
        DataHolder.setRef(new Firebase(URL_FIREBASE));
        ref = DataHolder.getRef();

        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                //if user is authenticated
                if (authData != null) {
                    Intent homepageIntent = new Intent(getActivity(), HomepageActivity.class);
                    startActivity(homepageIntent);
                //if user is not authenticated, display login
                } else {
                    mLoginField = (EditText) rootView.findViewById(R.id.login_username);
                    mPassField = (EditText) rootView.findViewById(R.id.login_password);
                    mSignInButton = (Button) rootView.findViewById(R.id.sign_in_button);
                    mSignUpButtonView = (TextView) rootView.findViewById(R.id.sign_up_buttonview);



                    //creating anonymous inner classes for click events
                    mSignInButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            ref.authWithPassword(mLoginField.getText().toString(), mPassField.getText().toString() , new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    //Read about Explicit Intents at
                                    //http://developer.android.com/guide/components/intents-filters.html
                                    //used to start activities (like this) and send data across activities
                                    Intent homepageIntent = new Intent(getActivity(), HomepageActivity.class);
                                    startActivity(homepageIntent);
                                }
                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    //Create alert with firebaseError.getMessage()
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                    builder1.setMessage(firebaseError.getMessage());
                                    builder1.setCancelable(true);
                                    builder1.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            });
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
                }
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
