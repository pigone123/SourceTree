package com.example.gal.testing.userData;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.gal.testing.SingleFragmentActivity;

/**
 * Created by gal on 24/05/2016.
 */
public class ProfileActivity extends SingleFragmentActivity {

    public static Intent ProfileActivityIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        return new ProfileFragment();
    }
}

