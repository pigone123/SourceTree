package com.example.gal.testing.registration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.gal.testing.SingleFragmentActivity;

/**
 * Created by gal on 24/05/2016.
 */
public class RegisterActivity extends SingleFragmentActivity {
    public static Intent RegisterActivityIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        return new RegisterFragment();
    }
}
