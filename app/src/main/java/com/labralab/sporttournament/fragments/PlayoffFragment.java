package com.labralab.sporttournament.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.labralab.sporttournament.TeamActivity;
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

    TeamActivity teamActivity;

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

    boolean rvTouch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playoff, container, false);
        return view;

    }//onCreate

    @Override
    public void onStart() {
        super.onStart();

        if (Tournament.getInstance().getIsPlayoffFlag()) {

            teamActivity = (TeamActivity) getActivity();
            display = teamActivity.display;
            int displayHeight = display.getHeight();

            playoff = Tournament.getInstance().getPlayoff();
            teamInPlayoff = playoff.getTeamInPlayoff();

            ImageView logo = (ImageView) getActivity().findViewById(R.id.playoff_logo);

            ViewGroup.LayoutParams params = logo.getLayoutParams();
            params.height = displayHeight / 8;
            params.width = displayHeight / 8;
            logo.setLayoutParams(params);

            if (Tournament.getInstance().getType().equals("Футбол")) {
                logo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.f1f));
            } else {
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
    }

    private void firstRVStart() {

        firstTur = playoff.getPlayoffFirstTurGames();

        firstRecyclerView = (RecyclerView) getActivity().findViewById(R.id.first_tur_list);
        firstManager = new LinearLayoutManager(getContext());
        firstAdapter = new GameInPlayoffAdapter(firstTur, 1, teamInPlayoff, display);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(firstAdapter);

        firstRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouch(motionEvent);
                return false;
            }
        });

        firstRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                moveTabLayout(firstRecyclerView, dy);
            }
        });
    }

    private void secondRVStart() {

        secondTur = playoff.getPlayoffSecondTurGames();

        secondRecyclerView = (RecyclerView) getActivity().findViewById(R.id.second_tur_list);
        secondManager = new LinearLayoutManager(getContext());
        secondAdapter = new GameInPlayoffAdapter(secondTur, 2, teamInPlayoff, display);
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(secondAdapter);

        secondRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouch(motionEvent);
                return false;
            }
        });

        secondRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                moveTabLayout(secondRecyclerView, dy);
            }
        });
    }

    private void lastRVStart() {

        lastTur = playoff.getPlayoffLastTurGames();

        lastRecyclerView = (RecyclerView) getActivity().findViewById(R.id.last_tur_list);
        lastManager = new LinearLayoutManager(getContext());
        lastAdapter = new GameInPlayoffAdapter(lastTur, 3, teamInPlayoff, display);
        lastRecyclerView.setLayoutManager(lastManager);
        lastRecyclerView.setAdapter(lastAdapter);

        lastRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouch(motionEvent);
                return false;
            }
        });

        lastRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                moveTabLayout(lastRecyclerView, dy);
            }
        });
    }

    private void isTouch(MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            rvTouch = true;
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            rvTouch = false;
        }
    }

    private void moveTabLayout(RecyclerView recyclerView, int dy) {

        if (dy > 0) {

            // Scroll Down
            if (teamActivity.getSegmentTabLayout().isShown()) {

                final Animation fallingAnimation = AnimationUtils.loadAnimation(recyclerView.getContext(),
                        R.anim.out_doun);
                fallingAnimation.setInterpolator(new LinearInterpolator());
                teamActivity.getSegmentTabLayout().startAnimation(fallingAnimation);
                teamActivity.getSegmentTabLayout().setVisibility(View.INVISIBLE);

                rvTouch = false;
            }
        } else if (dy < 0) {

            // Scroll Up
            if (!teamActivity.getSegmentTabLayout().isShown()) {

                final Animation fallingAnimation = AnimationUtils.loadAnimation(recyclerView.getContext(),
                        R.anim.in_up);
                fallingAnimation.setInterpolator(new LinearOutSlowInInterpolator());
                teamActivity.getSegmentTabLayout().startAnimation(fallingAnimation);
                teamActivity.getSegmentTabLayout().setVisibility(View.VISIBLE);

                rvTouch = false;
            }
        }
    }
}