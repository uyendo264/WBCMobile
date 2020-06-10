package com.singalarity.mobilelogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SignInFragment extends Fragment {
    private SharedViewModel viewModel;

    EditText usernameText;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText urlText = view.findViewById(R.id.serverAddress_editText);

         usernameText= view.findViewById(R.id.username_editText);
        final EditText passwordText= view.findViewById(R.id.password_editText);

        view.findViewById(R.id.signIn_button).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String url = urlText.getText().toString();
                String username= usernameText.getText().toString();
                String password = passwordText.getText().toString();

                String respone = ((MainActivity) getActivity()).sendData(url,username,password);
                Log.d("TEST", "response: "+ respone);


                NavHostFragment.findNavController(SignInFragment.this)
                       .navigate(R.id.action_signInFragment_to_dashboardFragment);

                viewModel.setUserName(username);


            }
        });
        view.findViewById(R.id.signUp_button).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                NavHostFragment.findNavController(SignInFragment.this)
                        .navigate(R.id.action_signInFragment_to_signUpFragment);

            }
        });
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                usernameText.setText(s);
            }
        });
    }

}
