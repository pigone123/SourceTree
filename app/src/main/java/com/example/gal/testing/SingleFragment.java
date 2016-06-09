package com.example.gal.testing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.gal.testing.registration.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public abstract class SingleFragment extends Fragment {
    private Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_tool_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_log_out:
                logOut(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void logOut(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        mIntent = LoginActivity.LoginActivityIntent(activity);
        activity.startActivity(mIntent);
        activity.finish();
    }


   /* public void contactUs(Activity activity){
        mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setData(Uri.parse("mailto:"));
        String [] ourMails = {"danirg10000@gmail.com"};
        mIntent.putExtra(Intent.EXTRA_EMAIL,ourMails);
        mIntent.setType("message/rfc822");
        PackageManager pm = activity.getPackageManager();
        if (pm.resolveActivity(mIntent,PackageManager.MATCH_DEFAULT_ONLY) == null){
            Toast.makeText(activity, "you don't have responding app for mail", Toast.LENGTH_LONG).show();
        } else{
            activity.startActivity(mIntent);
        }
    }*/
}
