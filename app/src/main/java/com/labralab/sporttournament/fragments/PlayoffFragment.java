package com.labralab.sporttournament.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.labralab.sporttournament.adapters.GameInPlayoffAdapter;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Playoff;
import com.labralab.sporttournament.models.Tournament;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 14.10.2017.
 */

public class PlayoffFragment extends Fragment {

    RecyclerView firstRecyclerView;
    RecyclerView secondRecyclerView;
    RecyclerView lastRecyclerView;

    GameInPlayoffAdapter firstAdapter;
    GameInPlayoffAdapter secondAdapter;
    GameInPlayoffAdapter lastAdapter;

    RecyclerView.LayoutManager firstManager;
    RecyclerView.LayoutManager secondManager;
    RecyclerView.LayoutManager lastManager;

    List<Game> firstTur;
    List<Game> secondTur;
    List<Game> lastTur;

    Display display;

    int teamInPlayoff;

    View view;

    Playoff playoff;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        playoff = Tournament.getInstance().getPlayoff();
        teamInPlayoff = playoff.getTeamInPlayoff();
        view = inflater.inflate(R.layout.fragment_playoff, container, false);

        return view;

    }//onCreate

    @Override
    public void onStart() {
        super.onStart();

        display = getActivity().getWindowManager().getDefaultDisplay();

        playoff = Tournament.getInstance().getPlayoff();

        ImageView logo = (ImageView) getActivity().findViewById(R.id.playoff_logo);

        if(Tournament.getInstance().getType().equals("Футбол")){
            logo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.f1f));
        }else {
            logo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.b1b));
        }

        firstTur = new ArrayList<>();
        secondTur = new ArrayList<>();
        lastTur = new ArrayList<>();


        firstRVStart();

        switch (teamInPlayoff) {
            case 8:
                secondRVStart();
                lastRVStart();
                break;
            case 4:
                lastRVStart();
                break;

        }


    }

    private void firstRVStart() {

        firstTur = playoff.getPlayoffFirstTurGames();

        firstRecyclerView = (RecyclerView) getActivity().findViewById(R.id.first_tur_list);
        firstManager = new LinearLayoutManager(getContext());
        firstAdapter = new GameInPlayoffAdapter(firstTur, 1, teamInPlayoff, display);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(firstAdapter);
    }

    private void secondRVStart() {

        secondTur = playoff.getPlayoffSecondTurGames();

        secondRecyclerView = (RecyclerView) getActivity().findViewById(R.id.second_tur_list);
        secondManager = new LinearLayoutManager(getContext());
        secondAdapter = new GameInPlayoffAdapter(secondTur, 2, teamInPlayoff, display);
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(secondAdapter);
    }

    private void lastRVStart() {

        lastTur = playoff.getPlayoffLastTurGames();

        lastRecyclerView = (RecyclerView) getActivity().findViewById(R.id.last_tur_list);
        lastManager = new LinearLayoutManager(getContext());
        lastAdapter = new GameInPlayoffAdapter(lastTur, 3, teamInPlayoff, display);
        lastRecyclerView.setLayoutManager(lastManager);
        lastRecyclerView.setAdapter(lastAdapter);
    }
}