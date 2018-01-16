package com.labralab.sporttournament.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.labralab.sporttournament.adapters.TeamAdapter;
import com.labralab.sporttournament.dialogs.NewTeamDialog;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.DividerItemDecoration;
import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.utils.UiUtil;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;


public class EditTournamentFragment extends Fragment {

    static List<Team> teams = new ArrayList<>();
    FragmentManager fm;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Tournament tournament;
    Bundle bundle;
    static TeamAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_tourn, container, false);

        //Connecting RecyclerView, layoutManager and Adapter
        recyclerView = (RecyclerView) rootView.findViewById(R.id.teamList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);


        return rootView;
    }//onCreateView()

    @Override
    public void onStart() {
        super.onStart();

        MainActivity.getToolbar().setTitle(R.string.edit_tournament);

        final TextInputLayout tilTitle = (TextInputLayout) getActivity().findViewById(R.id.tournTitle);
        final EditText etTitle = tilTitle.getEditText();

        final TextInputLayout tilYear = (TextInputLayout) getActivity().findViewById(R.id.tournYear);
        final EditText etYear = tilYear.getEditText();

        //Types of tournaments
        String[] tournType = {getString(R.string.football), getString(R.string.basketball)};
        //quantity teams in the playoff
        String[] teamInPlayoff = {"2", "4", "8"};
        //quantity loops
        String[] loops = {"1", "2", "3"};

        //Getting all spinners and setting him parameters
        final Spinner spTournType = (MaterialSpinner) getActivity().findViewById(R.id.spTournType);
        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tournType);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTournType.setAdapter(adapterOne);

        final Spinner spInPlayoff = (MaterialSpinner) getActivity().findViewById(R.id.team_in_playoff);
        ArrayAdapter<String> adapterTeamInPlayoff = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, teamInPlayoff);
        adapterTeamInPlayoff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInPlayoff.setAdapter(adapterTeamInPlayoff);

        final Spinner spLoops = (MaterialSpinner) getActivity().findViewById(R.id.loops);
        ArrayAdapter<String> adapterLoops = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, loops);
        adapterLoops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoops.setAdapter(adapterLoops);


        //Button for new tams
        Button addTeamBatton = (Button) getActivity().findViewById(R.id.add_team_button);
        addTeamBatton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment neeTeamDialog = new NewTeamDialog();
                neeTeamDialog.show(getFragmentManager(), "TAG");

            }
        });

        if (getArguments() != null) {

            bundle = this.getArguments();

            //Getting Tournament for edit
            tournament = new Tournament(bundle.getString("title"), getActivity());

            //Setting params
            etTitle.setText(tournament.getTitle());
            etYear.setText(tournament.getYearOfTourn());

            String type = tournament.getType().toString();
            spTournType.setSelection(UiUtil.getSpinnerPosition(type, tournType) + 1);

            int mTeamInPlayoff = tournament.getTeamInPlayoff();
            spInPlayoff.setSelection(UiUtil.getSpinnerPosition( mTeamInPlayoff, teamInPlayoff) + 1);

            int mLoop = tournament.getLoops();
            spLoops.setSelection(UiUtil.getSpinnerPosition( mLoop, loops) + 1);

            //Checking number of games. If there is no game, we can edit team list
            if (tournament.getGameList().size() == 0) {
                teams = tournament.getTeamList();
                adapter = new TeamAdapter(teams);
                recyclerView.setAdapter(adapter);

            } else {
                addTeamBatton.setVisibility(View.INVISIBLE);
                spTournType.setVisibility(View.INVISIBLE);
                spInPlayoff.setVisibility(View.INVISIBLE);
                spLoops.setVisibility(View.INVISIBLE);
            }
        }


        //FloatingActionButton for confirm
        final FloatingActionButton button = (FloatingActionButton) getActivity().findViewById(R.id.buttonOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Removing old Tournament and adding new
                if (tournament.getGameList().size() == 0) {

                    int teamInPlaypff = Integer.parseInt(spInPlayoff.getSelectedItem().toString());
                    int loops = Integer.parseInt(spLoops.getSelectedItem().toString());

                    tournament.remove(bundle.getString("title"));
                    Tournament newTournament = new Tournament(etTitle.getText().toString(),
                            etYear.getText().toString(),
                            spTournType.getSelectedItem().toString(),
                            teams, null, teamInPlaypff, loops,
                            getActivity());
                }else {
                    List<Game> gameList = tournament.getGameList();
                    List<Team> teamList = tournament.getTeamList();
                    String type = tournament.getType();
                    int teamInPlaypff = tournament.getTeamInPlayoff();
                    int loops = tournament.getLoops();

                    tournament.remove(bundle.getString("title"));
                    Tournament newTournament = new Tournament(etTitle.getText().toString(),
                            etYear.getText().toString(),
                            type,
                            teamList, gameList,
                            teamInPlaypff, loops,
                            getActivity());
                }


                ///hide SoftInput From Window
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Backing to StartFragment
                teams.clear();
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

            }
        });
    }

    //Method for adding teams to 'teams'
    public static boolean addTeam(String title) {
        boolean isEqualsTeam = false;
        if (teams.size() > 0) {
            for (int i = 0; i < teams.size(); i++) {
                Team team = teams.get(i);
                String titleTeam = team.getTitle();
                if (titleTeam.equals(title)) {
                    isEqualsTeam = true;
                }
            }
        }
        if (isEqualsTeam == false) {
            teams.add(new Team(title));
            return true;
        } else {
            return false;
        }
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static TeamAdapter getAdapter() {
        return adapter;
    }
}