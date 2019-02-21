package schooling.com.epizy.someone.schooling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import schooling.com.epizy.someone.schooling.R;
import spencerstudios.com.bungeelib.Bungee;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_profile);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("preferences", MODE_PRIVATE);
        if (pref.getBoolean("user", false)){
            ((ImageView)findViewById(R.id.image_profile)).setImageURI(Uri.parse(getSharedPreferences("preferences", MODE_PRIVATE).getString("PROFILE", null)));
            ((TextView)findViewById(R.id.name_profile)).setText(pref.getString("name", "Not Defined"));
            ((TextView)findViewById(R.id.major_profile)).setText(pref.getString("major", "Majoring in"));
        }
    }

    public void editProfile(View view) {
        Intent i = new Intent(this, update_profile.class);
        startActivity(i);
        Bungee.fade(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.slideRight(this);
    }
}
