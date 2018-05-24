package schooling.com.epizy.someone.schooling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Objects;
import schooling.com.epizy.someone.schooling.fragments.home;
import schooling.com.epizy.someone.schooling.fragments.open_source;
import schooling.com.epizy.someone.schooling.fragments.schedule;
import schooling.com.epizy.someone.schooling.fragments.subjects;
import schooling.com.epizy.someone.schooling.fragments.teachers;

public class Home extends AppCompatActivity {
    Toolbar tb; FrameLayout fl; DrawerLayout dl; CoordinatorLayout cl; NavigationView nv;
    FragmentManager fm; ActionBarDrawerToggle ActionBarDrawer; Snackbar Closing;
    FloatingActionButton fab;
    private static home HomeFragment = new home();
    private static schedule ScheduleFragment = new schedule();
    private static subjects SubjectFragment = new subjects();
    private static teachers TeacherFragment = new teachers();
    private static open_source SourceFragment = new open_source();
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
        fab = findViewById(R.id.fab_main);
        initSnackBar();

        fm.beginTransaction().replace(R.id.content_home, HomeFragment).commit(); fab.hide();

        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true); ab.setHomeAsUpIndicator(R.drawable.ic_menu);

        View header = nv.getHeaderView(0);
        TextView semester = header.findViewById(R.id.semester);
        semester.setText(Html.fromHtml("6<sup>th</sup> semester  "));

        onClickNavigationView();
        settingDrawerLayout();
        FloatingClicked();
    }

    private void FloatingClicked() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tb.getTitle().toString()){
                    case "Schedule": startActivityForResult(new Intent(Home.this, add_schedule.class), 2);
                        break;
                    case "Subjects": startActivityForResult(new Intent(Home.this, add_subject.class), 11);
                        break;
                    case "Teachers": TeacherFragment.mdb.show();
                        break;
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void initSnackBar() {
        Closing = Snackbar.make(findViewById(R.id.root_coordinator_home), "Quit the app?", Snackbar.LENGTH_SHORT)
                .setAction("YES!", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Closing.dismiss();
                Snackbar.make(findViewById(R.id.root_coordinator_home), "You can't, use the logout menu to close the app", Snackbar.LENGTH_LONG).show();
            }
        });
        Closing.setActionTextColor(Color.YELLOW);
        Closing.getView().setBackgroundColor(Color.LTGRAY);
        ((TextView)Closing.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(R.color.colorPrimary);
    }

    private void settingDrawerLayout() {
        ActionBarDrawer = setupActionBarDrawer();
        dl.addDrawerListener(ActionBarDrawer);
    }

    private ActionBarDrawerToggle setupActionBarDrawer() {
        return new ActionBarDrawerToggle(this, dl, tb, R.string.drawer_open, R.string.drawer_close);
    }

    private void onClickNavigationView() {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dl.closeDrawers();
                switch (item.getItemId()){

                    case R.id.menu_home:
                        nv.setCheckedItem(R.id.menu_home); fab.hide();
                        fm.beginTransaction().replace(R.id.content_home, HomeFragment).commit();
                        break;

                    case R.id.menu_logout:
                        Home.this.finish();
                        break;

                    case  R.id.menu_profile:
                        startActivity(new Intent(Home.this, Profile.class)); fab.show();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;

                    case R.id.menu_schedule:
                        nv.setCheckedItem(R.id.menu_schedule); fab.show();
                        fm.beginTransaction().replace(R.id.content_home, ScheduleFragment).commit();
                        break;

                    case R.id.menu_subjects:
                        nv.setCheckedItem(R.id.menu_subjects); fab.show();
                        fm.beginTransaction().replace(R.id.content_home, SubjectFragment).commit();
                        break;

                    case R.id.menu_teachers:
                        nv.setCheckedItem(R.id.menu_teachers); fab.show();
                        fm.beginTransaction().replace(R.id.content_home, TeacherFragment).commit();
                        break;

                    case  R.id.menu_code:
                        nv.setCheckedItem(R.id.menu_teachers); fab.hide();
                        fm.beginTransaction().replace(R.id.content_home, SourceFragment).commit();
                        break;
                }

                tb.setTitle(item.getTitle());
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        ActionBarDrawer.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        ActionBarDrawer.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(ActionBarDrawer.onOptionsItemSelected(item)){
            dl.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!Closing.getView().isShown()){
            Closing.show();
        }else{
            Log.w("From Home Activity :", "SnackBar is shown");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==11&&resultCode==RESULT_OK){
            SubjectFragment.onActivityResult(requestCode, resultCode, data);
        }else if(requestCode==2&&resultCode==RESULT_OK){
            ScheduleFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("RequestCode", String.valueOf(requestCode));
        super.startActivityForResult(intent, requestCode);
    }
}
