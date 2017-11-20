package com.example.roncherian.homework07;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by roncherian on 18/11/17.
 */

public class MenuBarActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_bar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    FirebaseAuth.getInstance().signOut();
                }
                emptySharedPreferences();
                Intent logoutIntent = new Intent();
                logoutIntent.putExtra("logout",true);
                setResult(RESULT_OK,logoutIntent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void emptySharedPreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS, MODE_PRIVATE).edit();
        editor.remove("userId");
        editor.remove("username");
        editor.remove("isGoogleUser");
        editor.commit();
    }
}
