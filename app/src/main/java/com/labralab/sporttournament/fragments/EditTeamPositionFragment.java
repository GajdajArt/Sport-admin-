package com.labralab.sporttournament.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.labralab.sporttournament.R;
import com.labralab.sporttournament.TeamActivity;
import com.labralab.sporttournament.adapters.EditTeamAdapter;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 21.01.2018.
 */

public class EditTeamPositionFragment extends Fragment {

    TeamActivity teamActivity;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public static EditTeamAdapter teamAdapter;
    boolean rvTouch;
    FloatingActionButton fab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_team_position, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.edit_team_list);
        layoutManager = new LinearLayoutManager(getContext());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        teamActivity = (TeamActivity) getActivity();

        List<Team> teams = new ArrayList<>();
        teams.addAll(Tournament.getInstance().getPlayoff().getPlayoffTeamList());
        teams.remove(teams.size() - 1);

        recyclerView.setLayoutManager(layoutManager);
        teamAdapter = new EditTeamAdapter(teams);
        recyclerView.setAdapter(teamAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));


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

        Toast.makeText(getContext(), "Выберите пары команд для первого тура плейофф", Toast.LENGTH_LONG).show();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tournament tournament = teamActivity.getTournament();
                tournament.getPlayoff().setTeamListAfterSort(teamAdapter.getTeams());

                Intent intent = new Intent(getContext(), TeamActivity.class);
                String tableName = tournament.getTitle();
                intent.putExtra("title", tableName);
                teamActivity.finish();
                getContext().startActivity(intent);
            }
        });



//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab2);
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0) {
//                    // Scroll Down
//                    if (fab.isShown()) {
//
//
//                        final Animation fallingAnimation = AnimationUtils.loadAnimation(recyclerView.getContext(),
//                                R.anim.out_doun);
//                        fallingAnimation.setInterpolator(new LinearInterpolator());
//                        teamActivity.getSegmentTabLayout().startAnimation(fallingAnimation);
//                        teamActivity.getSegmentTabLayout().setVisibility(View.INVISIBLE);
//                        fab.hide();
//                        rvTouch = false;
//                    }
//                } else if (dy < 0) {
//                    // Scroll Up
//                    if (!fab.isShown()) {
//                        fab.show();
//                        final Animation fallingAnimation = AnimationUtils.loadAnimation(recyclerView.getContext(),
//                                R.anim.in_up);
//                        fallingAnimation.setInterpolator(new LinearOutSlowInInterpolator());
//                        teamActivity.getSegmentTabLayout().startAnimation(fallingAnimation);
//                        teamActivity.getSegmentTabLayout().setVisibility(View.VISIBLE);
//                        rvTouch = false;
//                    }
//                }
//            }
//        });
   }
}