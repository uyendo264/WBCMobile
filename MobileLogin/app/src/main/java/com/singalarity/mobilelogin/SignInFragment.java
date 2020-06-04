package com.singalarity.mobilelogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SignInFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText urlText = view.findViewById(R.id.serverAddress_editText);

        final EditText usernameText = view.findViewById(R.id.username_editText);
        final EditText passwordText= view.findViewById(R.id.password_editText);

        view.findViewById(R.id.signIn_button).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String url = urlText.getText().toString();
                String username= usernameText.getText().toString();
                String password = passwordText.getText().toString();

                String respone = ((MainActivity) getActivity()).sendData(url,username,password);
                Log.d("TEST", "response: "+ respone);
                SignInFragmentDirections.ActionSignInFragmentToDashboardFragment action = SignInFragmentDirections.actionSignInFragmentToDashboardFragment().setName("hello");

                NavHostFragment.findNavController(SignInFragment.this)
                       .navigate(action);

            }
        });
        view.findViewById(R.id.signUp_button).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                NavHostFragment.findNavController(SignInFragment.this)
                        .navigate(R.id.action_signInFragment_to_signUpFragment);

            }
        });
    }



}
