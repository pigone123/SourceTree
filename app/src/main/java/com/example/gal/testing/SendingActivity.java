package com.example.gal.testing;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by gal on 02/06/2016.
 */
public class SendingActivity extends SingleFragmentActivity {
    public static Intent SendingActivityIntent(Context context){
        Intent intent = new Intent(context,SendingActivity.class);
        return  intent;
    }

    @Override
    protected Fragment getFragment() {
        return new SendingFragment();
    }
}
