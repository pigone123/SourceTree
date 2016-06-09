package com.example.gal.testing.registration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.gal.testing.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {
    public static Intent LoginActivityIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        return new LoginFragment();
    }
}
