package schooling.com.epizy.someone.schooling;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import schooling.com.epizy.someone.schooling.fragments.home;
import schooling.com.epizy.someone.schooling.fragments.schedule;
import schooling.com.epizy.someone.schooling.fragments.subjects;
import schooling.com.epizy.someone.schooling.fragments.teachers;

public class Home extends AppCompatActivity {
    Toolbar tb; FrameLayout fl; DrawerLayout dl; CoordinatorLayout cl; NavigationView nv;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_home);

        tb = findViewById(R.id.toolbar);
        fl = findViewById(R.id.content_home);
        dl = findViewById(R.id.root_home);
        cl = findViewById(R.id.root_coordinator_home);
        nv = findViewById(R.id.nav_view);
        fm = getSupportFragmentManager();
        try {
            fm.beginTransaction().replace(R.id.content_home, home.class.newInstance()).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); ab.setHomeAsUpIndicator(R.drawable.ic_menu);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fr; Class SelectedFragment = null;
                dl.closeDrawers();
                switch (item.getItemId()){

                    case R.id.menu_home:
                        nv.setCheckedItem(R.id.menu_home);
                        SelectedFragment = home.class;
                        break;

                    case R.id.menu_logout:
                        Home.this.finish();
                        break;

                    case  R.id.menu_profile:
                        startActivity(new Intent(Home.this, Profile.class));
                        break;

                    case R.id.menu_schedule:
                        nv.setCheckedItem(R.id.menu_schedule);
                        SelectedFragment = schedule.class;
                        break;

                    case R.id.menu_subjects:
                        nv.setCheckedItem(R.id.menu_subjects);
                        SelectedFragment = subjects.class;
                        break;
                    case R.id.menu_teachers:
                        nv.setCheckedItem(R.id.menu_teachers);
                        SelectedFragment = teachers.class;
                        break;
                }

                try{
                    fr = (Fragment) SelectedFragment.newInstance();
                    fm.beginTransaction().replace(R.id.content_home, fr).commit();
                    tb.setTitle(item.getTitle());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            dl.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}
