package com.example.gal.testing.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

/**
 * Created by gal on 02/06/2016.
 */
public class RegisterFragment extends SingleFragment implements TextWatcher {
    private EditText mEmail, mPassword, mVerifyPass;
    private Button signIn;
    private FirebaseAuth mAuth;
    private String mMail, mPass;
    private Boolean mValid = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        mEmail = (EditText) v.findViewById(R.id.email);
        mPassword = (EditText) v.findViewById(R.id.password);
        mVerifyPass = (EditText) v.findViewById(R.id.verify_password);
        mEmail.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        mVerifyPass.addTextChangedListener(this);
        signIn = (Button) v.findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void signIn() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while creating your account...");
        progressDialog.show();
        mMail = mEmail.getText().toString();
        mPass = mPassword.getText().toString();
        if (mValid) {

            mAuth.createUserWithEmailAndPassword(mMail, mPass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            Log.d("success", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            Intent i = ProfileActivity.ProfileActivityIntent(getActivity());
                            startActivity(i);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), " " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mMail = mEmail.getText().toString();
        mPass = mPassword.getText().toString();
        mValid = validate(mPass, mMail);
    }

    private Boolean validate(String passChecker, String mailChecker) {
        mValid = true;
        String nonWordCharChecker = ("^(\\w*)$");
        final String verifyPass = mVerifyPass.getText().toString();
        if (TextUtils.isEmpty(mailChecker)) {
            mEmail.setError("email can't be empty");
            mValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mailChecker).matches()) {
            mEmail.setError("invalid email address");
            mValid = false;
        }
        if ((passChecker.length() < 6 || passChecker.length() > 16)) {
            mPassword.setError("password must be 6-16 chars");
            mValid = false;
        }
        if (!(passChecker.matches(nonWordCharChecker))) {
            mPassword.setError("password can't contain non-word characters");
            mValid = false;
        }
        if (!(passChecker.equals(verifyPass))) {
            mVerifyPass.setError("passwords don't match");
            mValid = false;
        }
        return mValid;
    }
}
