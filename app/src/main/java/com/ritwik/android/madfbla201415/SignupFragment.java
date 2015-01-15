package com.ritwik.android.madfbla201415;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.*;

//class for signing up

public class SignupFragment extends Fragment {

    private EditText mFullName, mEmail, mPhoneNumber,
            mUsername, mPassword, mRepeatPassword;
    private Button mSignUpButton;
    private TextView mSignInButtonView;
    private Firebase ref;

    //for convenience for anyFieldIsNull()
    private EditText[] allFields;

    //constants
    private static final String LOG_TAG = "LoginPhase";
    private static final String URL_FIREBASE = "https://chronology.firebaseio.com";


    public SignupFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        mFullName = (EditText) rootView.findViewById(R.id.signup_full_name);
        mEmail = (EditText) rootView.findViewById(R.id.signup_email);
        mPhoneNumber = (EditText) rootView.findViewById(R.id.signup_phone);
        mPassword = (EditText) rootView.findViewById(R.id.signup_password);
        mRepeatPassword = (EditText) rootView.findViewById(R.id.signup_repeat_password);
        mSignUpButton = (Button) rootView.findViewById(R.id.sign_up_button);
        mSignInButtonView = (TextView) rootView.findViewById(R.id.sign_in_buttonview);

        allFields = new EditText[] {
                mFullName, mEmail, mPhoneNumber, mUsername, mPassword, mRepeatPassword
        };

        ref = DataHolder.getRef();

        //creating anonymous inner classes for click events
        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //check if password fields are equal and no field is empty
                if (mPassword.getText().toString().equals(mRepeatPassword.getText().toString())
                        && !anyFieldIsNull()) {

                    createUserFromCurrentData();
                    getActivity().finish();
                } else {

                    Toast.makeText(getActivity().getApplicationContext(), "Fields must not be empty, " +
                                    "Passwords must match.",
                            Toast.LENGTH_SHORT).show();

                    mPassword.setText(null);
                    mRepeatPassword.setText(null);

                }
            }

        });

        mSignInButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.right_to_left, R.anim.neg_right_left)
                        .replace(R.id.fragment_main_container, new LoginFragment())
                        .commit();
            }

        });



        return rootView;
    }

    public void createUserFromCurrentData() {

        ref.createUser(mEmail.getText().toString(),
                mPassword.getText().toString(), new Firebase.ResultHandler() {

            @Override
            public void onSuccess() {

                ref.authWithPassword(mEmail.getText().toString(),
                        mPassword.getText().toString() , new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {

                        ref.child("users").child(authData.getUid()).child("full_name")
                                .setValue(mFullName.getText().toString());

                        Toast.makeText(getActivity().getApplicationContext(), "Success, Account Created!",
                                Toast.LENGTH_SHORT).show();

                        Intent homepageIntent = new Intent(getActivity(), HomepageActivity.class);
                        homepageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homepageIntent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        //Auth Error
                        Toast.makeText(getActivity().getApplicationContext(),
                                firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(FirebaseError firebaseError) {

                Toast.makeText(getActivity().getApplicationContext(),
                        firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean anyFieldIsNull() {

        for (EditText field : allFields) {
            if (field.getText().length() == 0)
                return true;
        }

        return false;
    }

}
