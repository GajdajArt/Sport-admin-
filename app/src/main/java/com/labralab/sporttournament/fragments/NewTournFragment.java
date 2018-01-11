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
import android.widget.Toast;

import com.labralab.sporttournament.adapters.TeamAdapter;
import com.labralab.sporttournament.dialogs.NewTeamDialog;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.DividerItemDecoration;
import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class NewTournFragment extends Fragment {

    public static List<Team> teams = new ArrayList<>();
    FragmentManager fm;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public static TeamAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_tourn, container, false);

        //Connecting RecyclerView, layoutManager and Adapter
        recyclerView = (RecyclerView) rootView.findViewById(R.id.teamList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TeamAdapter(teams);
        recyclerView.setAdapter(adapter);

        return rootView;
    }//onCreateView()

    @Override
    public void onStart() {
        super.onStart();

        //Setting toolBar title
        MainActivity.getToolbar().setTitle(R.string.new_tournament);
        MainActivity.getToolbar().setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        //Getting entry fields
        final TextInputLayout tilTitle = (TextInputLayout) getActivity().findViewById(R.id.tournTitle);
        final EditText etTitle = tilTitle.getEditText();

        final TextInputLayout tilYear = (TextInputLayout) getActivity().findViewById(R.id.tournYear);
        final EditText etYear = tilYear.getEditText();

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        etYear.setText(Integer.toString(year));

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

        //Button for new teams
        Button addTeamBatton = (Button) getActivity().findViewById(R.id.add_team_button);
        addTeamBatton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newTeamDialog = new NewTeamDialog();

                newTeamDialog.show(getFragmentManager(), "TAG");

            }
        });

        //FloatingActionButton for confirm
        final FloatingActionButton button = (FloatingActionButton) getActivity().findViewById(R.id.buttonOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int teamInPlaypff = Integer.parseInt(spInPlayoff.getSelectedItem().toString());
                int loops = Integer.parseInt(spLoops.getSelectedItem().toString());

                //Checking etTitle and etYear isEmpty
                if (!etTitle.getText().toString().isEmpty() && !etYear.getText().toString().isEmpty()) {

                    //Checking quantity teams
                    if (teams.size() >= teamInPlaypff) {

                        //Creating new Tournament with parameters and saving it to DB
                        Tournament tournament = new Tournament(etTitle.getText().toString(),
                                etYear.getText().toString(),
                                spTournType.getSelectedItem().toString(),
                                teams, null, teamInPlaypff, loops,
                                getActivity());

                        //hide SoftInput From Window
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(button.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        //Backing to StartFragment
                        teams.clear();
                        fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();


                    } else {
                        Toast.makeText(getActivity(), R.string.more_teams, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.enter_all_data, Toast.LENGTH_SHORT).show();
                }


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
}
