package com.example.gal.testing.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gal.testing.userData.ProfileActivity;
import com.example.gal.testing.R;
import com.example.gal.testing.SingleFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gal on 02/06/2016.
 */
public class LoginFragment extends SingleFragment implements View.OnClickListener {
    private Intent i;
    private EditText mEmail, mPassword;
    private Button mRegister, mLogin;
    private Boolean mValid=true;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        mEmail = (EditText) v.findViewById(R.id.lEmail);
        mPassword = (EditText) v.findViewById(R.id.lPass);
        mLogin = (Button) v.findViewById(R.id.login);
        mRegister = (Button) v.findViewById(R.id.register);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    public void mAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    i = ProfileActivity.ProfileActivityIntent(getActivity());
                    startActivity(i);
                    Log.d("login", "onAuthStateChanged:signed_in:" + user.getUid());
                }

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.register:
                i = RegisterActivity.RegisterActivityIntent(getActivity());
                startActivity(i);
                break;
        }
    }

    public void login() {
        if(mValid) {
            mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("login", "signInWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.e("login", "signInWithEmail", task.getException());
                                Toast.makeText(getActivity(), "login failed: "+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
