package com.labralab.sporttournament.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import com.flyco.tablayout.SlidingTabLayout;
import com.labralab.sporttournament.dialogs.GameDialog;
import com.labralab.sporttournament.R;

import java.util.ArrayList;


public class TeamTabFragment extends Fragment {

    public  TeamListFragment teamListFragment;
    public  GameListFragment gameListFragment;
    private SlidingTabLayout mTabLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"КОМАНДЫ", "ИГРЫ"};
    public  Context context;
    public  FloatingActionButton fab;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_team_tab, container, false);
        context = view.getContext();
        return view;

    }

    @Override
    public void onStart() {
                super.onStart();

        //Getting UI elements
        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.STL);
        teamListFragment = new TeamListFragment();
        gameListFragment = new GameListFragment();
        mFragments.add(teamListFragment);
        mFragments.add(gameListFragment);

        ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabLayout.setViewPager(pager, mTitles, getActivity(), mFragments);


        //Calling newGameDialog
        fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newGameDialog = new GameDialog();
                newGameDialog.show(getActivity().getSupportFragmentManager(), "TAG");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Creating animation
        final Animation fallingAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.fab_animation);
        fallingAnimation.setInterpolator(new OvershootInterpolator());
        fab.startAnimation(fallingAnimation);
    }
}