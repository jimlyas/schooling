package schooling.com.epizy.someone.schooling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionMenu;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import schooling.com.epizy.someone.schooling.R;

public class update_profile extends AppCompatActivity {
    EditText name, major;
    private SharedPreferences.Editor prefEditor;
    static String uri_image = "something";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_update_profile);
        name = findViewById(R.id.update_name);
        major = findViewById(R.id.update_major);
        ((FloatingActionMenu)findViewById(R.id.open_cropper)).setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(3,3)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                        .setActivityTitle("Crop Image")
                        .setAutoZoomEnabled(false)
                        .setAllowRotation(false)
                        .setAllowFlipping(false)
                        .start(update_profile.this);
            }
        });

        SharedPreferences pref = getSharedPreferences("preferences", MODE_PRIVATE);
        prefEditor = pref.edit();


        if (pref.getBoolean("user", false)){
            uri_image = pref.getString("PROFILE", null);
            ((CircleImageView)findViewById(R.id.update_profile_image)).setImageURI(Uri.parse(pref.getString("PROFILE", null)));
            name.setText(pref.getString("name", null));
            major.setText(pref.getString("major", null));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ((CircleImageView)findViewById(R.id.update_profile_image)).setImageURI(result.getUri());
            Log.w("IMAGE UPDATED", "true");
            uri_image = result.getUri().toString();
        }else {
            Log.w("RESULT :", "No Image Selected!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_update_profile){
            if (name.getText().toString().length()==0||major.getText().toString().length()==0){
                Toast.makeText(this, "There's empty field!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Soon!", Toast.LENGTH_SHORT).show();
                prefEditor.putBoolean("user", true);
                prefEditor.putString("PROFILE", uri_image);
                prefEditor.putString("name", name.getText().toString());
                prefEditor.putString("major", major.getText().toString());
                prefEditor.commit();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
