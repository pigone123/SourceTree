package com.example.gal.testing.userData;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gal.testing.R;
import com.example.gal.testing.SendingActivity;
import com.example.gal.testing.SingleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gal on 02/06/2016.
 */
public class ProfileFragment extends SingleFragment implements View.OnClickListener {
    private TextView email, uName;
    private FirebaseUser user;
    private Resources resources;
    private Button edit, send;
    private Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getActivity().getResources();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        email = (TextView) v.findViewById(R.id.email);
        uName = (TextView) v.findViewById(R.id.name);
        edit = (Button) v.findViewById(R.id.edit);
        send = (Button) v.findViewById(R.id.send);
        uName.setText(resources.getString(R.string.display_name) + ": " + user.getDisplayName());
        email.setText(resources.getString(R.string.mail) + ": " + user.getEmail());
        edit.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                i = EditActivity.EditActivityIntent(getActivity());
                startActivity(i);
                break;
            case R.id.send:
                i = SendingActivity.SendingActivityIntent(getActivity());
                startActivity(i);
                break;
        }
    }
}

