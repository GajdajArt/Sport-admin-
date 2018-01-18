package com.labralab.sporttournament.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.labralab.sporttournament.adapters.GameAdapter;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.TeamActivity;
import com.labralab.sporttournament.models.Tournament;

public class GameListFragment extends Fragment {

    TeamActivity teamActivity;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public static GameAdapter gameAdapter;
    boolean rvTouch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.gameList);
        layoutManager = new LinearLayoutManager(getContext());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        teamActivity = (TeamActivity) getActivity();

        recyclerView.setLayoutManager(layoutManager);
        gameAdapter = new GameAdapter(Tournament.getInstance().getGameList());
        recyclerView.setAdapter(gameAdapter);


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    rvTouch = true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    rvTouch = false;
                }
                return false;
            }

        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            FloatingActionButton fab = TeamTabFragment.fab;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down
                    if (fab.isShown()) {


                        final Animation fallingAnimation = AnimationUtils.loadAnimation(recyclerView.getContext(),
                                R.anim.out_doun);
                        fallingAnimation.setInterpolator(new LinearInterpolator());
                        teamActivity.getSegmentTabLayout().startAnimation(fallingAnimation);
                        teamActivity.getSegmentTabLayout().setVisibility(View.INVISIBLE);
                        fab.hide();
                        rvTouch = false;
                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!fab.isShown()) {
                        fab.show();
                        final Animation fallingAnimation = AnimationUtils.loadAnimation(recyclerView.getContext(),
                                R.anim.in_up);
                        fallingAnimation.setInterpolator(new LinearOutSlowInInterpolator());
                        teamActivity.getSegmentTabLayout().startAnimation(fallingAnimation);
                        teamActivity.getSegmentTabLayout().setVisibility(View.VISIBLE);
                        rvTouch = false;
                    }
                }
            }
        });
    }
}

