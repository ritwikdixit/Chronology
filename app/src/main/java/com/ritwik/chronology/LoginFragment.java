package com.ritwik.chronology;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.ritwik.chronology.Push.PushReceiver;


public class LoginFragment extends Fragment {

    private Button mSignInButton;
    private TextView mSignUpButtonView;
    private EditText mLoginField, mPassField;
    private Firebase ref;
    private boolean isSuccess = false;
    private Fragment mContent;

    //Constants
    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";
    private static final String LOG_TAG = "LoginPhase";


    //empty constructor refer to onCreateView
    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize the server
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        Firebase.setAndroidContext(getActivity());
        DataHolder.setRef(new Firebase(URL_FIREBASE));
        ref = DataHolder.getRef();

        mContent = this;

        mLoginField = (EditText) rootView.findViewById(R.id.login_username);
        mPassField = (EditText) rootView.findViewById(R.id.login_password);
        mSignInButton = (Button) rootView.findViewById(R.id.sign_in_button);
        mSignUpButtonView = (TextView) rootView.findViewById(R.id.sign_up_buttonview);

        //creating anonymous inner classes for click events
        mSignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ref.authWithPassword(mLoginField.getText().toString(),
                        mPassField.getText().toString() , new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {

                        //Intents are used to start activities (like this) and send data across activities
                        Intent intent = getActivity().getIntent();
                        Intent homepageIntent = new Intent(getActivity(), HomepageActivity.class);

                        //if started by a notification
                        if (intent != null && intent.getBooleanExtra(PushReceiver.PUSH_REDIRECT_KEY, false)) {
                            homepageIntent.putExtra(PushReceiver.PUSH_REDIRECT_KEY, true);
                            homepageIntent.putExtra(PushReceiver.PUSH_DETAILS_KEY,
                                    intent.getStringExtra(PushReceiver.PUSH_DETAILS_KEY));
                            homepageIntent.putExtra(PushReceiver.PUSH_MSG_KEY,
                                    intent.getStringExtra(PushReceiver.PUSH_MSG_KEY));
                        }

                        homepageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homepageIntent);
                        getActivity().finish();

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
                        .setCustomAnimations(R.anim.neg_left_right, R.anim.left_to_right)
                        .replace(R.id.fragment_main_container, new SignupFragment())
                        .commit();

            }
        });


        return rootView;
    }
}
