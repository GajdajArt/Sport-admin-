package com.labralab.sporttournament.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by pc on 31.10.2017.
 */

public class SplashFragment extends Fragment {

    MainActivity mainActivity;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().hide();

        SplashTask splashTask = new SplashTask();
        splashTask.execute();
        return rootView;
    }

    class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Saves the missions when users restart
            if (getActivity() != null) {

                // Allows to apply to the FragmentManager
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, mainActivity.getStartFragment())
                        .commit();
            }

            return null;
        }
    }
}
