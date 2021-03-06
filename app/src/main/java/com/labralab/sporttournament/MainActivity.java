package com.labralab.sporttournament;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.labralab.sporttournament.fragments.TournamentFragment;
import com.labralab.sporttournament.fragments.SplashFragment;
import com.labralab.sporttournament.fragments.StartFragment;

public class MainActivity extends AppCompatActivity {


    private  StartFragment startFragment;
    private  TournamentFragment tournamentFragment;
    private  Toolbar toolbar;
    private  FragmentManager fragmentManager;
    SharedPreferences sPref;
    private static final String SPLASH = "splash";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //New SharedPreference for splash checkBox state
        sPref = getPreferences(MODE_PRIVATE);

        //Connect to toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Creating instances of fragments
        startFragment = new StartFragment();
        tournamentFragment = new TournamentFragment();

        //Connect to Support Fragment Manage
        fragmentManager = getSupportFragmentManager();

        //if splash checkBox is ...
        if (sPref.getBoolean(SPLASH, true)) {

            //Run splash fragment
            SplashFragment splashFragment = new SplashFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, splashFragment)
                    .commit();
        } else {

            //Run Start Fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.container, startFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);

        //Switches the checkbox from the saved settings
        MenuItem spleshItem = menu.findItem(R.id.action_splash);
        spleshItem.setChecked(sPref.getBoolean(SPLASH, false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_splash) {

            //Saving checkBox state to SharedPreferences
            item.setChecked(!item.isChecked());
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean(SPLASH, item.isChecked());
            ed.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //For getting startFragment
    public StartFragment getStartFragment() {
        return startFragment;
    }
    //For getting tournamentFragment
    public TournamentFragment getTournamentFragment() {
        return tournamentFragment;
    }
    //For getting toolbar
    public Toolbar getToolbar() {
        return toolbar;
    }
}