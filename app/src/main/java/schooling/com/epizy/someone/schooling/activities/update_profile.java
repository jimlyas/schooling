package schooling.com.epizy.someone.schooling.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ((CircleImageView)findViewById(R.id.update_profile_image)).setImageURI(result.getUri());
        }else {

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
            Toast.makeText(this, "Soon!", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
