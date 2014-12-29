package com.ritwik.android.madfbla201415;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//class for signing up

public class SignupFragment extends Fragment {

    private EditText mFullName, mEmail,
            mUsername, mPassword, mRepeatPassword;
    private Button mSignUpButton;
    private TextView mSignInButtonView;

    //for convenience for anyFieldIsNull()
    private EditText[] allFields;

    //constants
    private static final String LOG_TAG = "LoginPhase";

    public SignupFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        mFullName = (EditText) rootView.findViewById(R.id.signup_full_name);
        mEmail = (EditText) rootView.findViewById(R.id.signup_email);
        mUsername = (EditText) rootView.findViewById(R.id.signup_username);
        mPassword = (EditText) rootView.findViewById(R.id.signup_password);
        mRepeatPassword = (EditText) rootView.findViewById(R.id.signup_repeat_password);
        mSignUpButton = (Button) rootView.findViewById(R.id.sign_up_button);
        mSignInButtonView = (TextView) rootView.findViewById(R.id.sign_in_buttonview);

        allFields = new EditText[] {
                mFullName, mEmail, mUsername, mPassword, mRepeatPassword
        };

        //creating anonymous inner classes for click events
        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO: Firebase check if distinct username, add User

                //check if password fields are equal and no field is empty
                if (mPassword.getText().toString().equals(mRepeatPassword.getText().toString())
                        && !anyFieldIsNull()) {

                    Toast.makeText(getActivity().getApplicationContext(), "Success, Account Created!",
                            Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main_container, new LoginFragment())
                            .commit();
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
                        .replace(R.id.fragment_main_container, new LoginFragment())
                        .addToBackStack(null).commit();
            }

        });



        return rootView;
    }

    private boolean anyFieldIsNull() {

        for (EditText field : allFields) {
            if (field.getText().length() == 0)
                return true;
        }

        return false;
    }

}
