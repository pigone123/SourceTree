package com.example.gal.testing.userData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gal.testing.R;
import com.example.gal.testing.SingleFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by gal on 02/06/2016.
 */
public class EditFragment extends SingleFragment implements TextWatcher {
    private EditText mPassword, mName;
    private String mPass, mDisplayName;
    private Button update;
    private Boolean mValid = true;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        mPassword = (EditText) v.findViewById(R.id.Edit_pass);
        mName = (EditText) v.findViewById(R.id.Edit_name);
        mName.setText(user.getDisplayName());
        update = (Button) v.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }


    public void update() {
        if (mValid) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mName.getText().toString())
                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            user.updatePassword(mPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            Toast.makeText(getActivity(), mName.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent i = ProfileActivity.ProfileActivityIntent(getActivity());
            startActivity(i);

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
        mPass = mPassword.getText().toString();
        mDisplayName = mName.getText().toString();
        validate(mPass, mDisplayName);
    }

    private Boolean validate(String passChecker, String nameChecker) {
        mValid = true;
        String nonWordCharChecker = ("^(\\w*)$");
        if (nameChecker.isEmpty()) {
            mName.setError("name can't be empty");
            mValid = false;
        }
        if (!(nameChecker.matches(nonWordCharChecker))) {
            mName.setError("name can't contain non-word characters");
            mValid = false;
        }
        if (passChecker.isEmpty()) {
            mPassword.setError("password can't be empty");
            mValid = false;
        }
        if (passChecker.length() < 6 || passChecker.length() > 16) {
            mPassword.setError("password must be 6-16 chars");
            mValid = false;
        }
        if (!(passChecker.matches(nonWordCharChecker))) {
            mPassword.setError("password can't contain no-word characters");
            mValid = false;
        }
        return mValid;
    }
}
