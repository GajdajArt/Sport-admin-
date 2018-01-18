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
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.DividerItemDecoration;
import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.utils.UiUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class TournamentFragment extends Fragment {

    private List<Team> teams = new ArrayList<>();
    private TeamAdapter adapter;
    MainActivity mainActivity;

    FragmentManager fm;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Tournament tournament;
    Bundle bundle;

    EditText etTitle;
    EditText etYear;

    //Types of tournaments
    String[] tournType = {"Футбол", "Баскетбол"};
    //quantity teams in the playoff
    String[] teamInPlayoff = {"2", "4", "8"};
    //quantity loops
    String[] loops = {"1", "2", "3"};

    Spinner spTournType;
    Spinner spInPlayoff;
    Spinner spLoops;

    FloatingActionButton button;
    Button addTeamButton;


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

        mainActivity = (MainActivity) getActivity();

        setToolbarTitle();
        initUI();
        setParams();



        //FloatingActionButton for confirm
        button = (FloatingActionButton) getActivity().findViewById(R.id.buttonOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getArguments() == null) {
                    createNewTournament();
                } else {
                    editTournament();
                }
            }
        });
    }

    //Method for adding teams to 'teams'
    public boolean addTeam(String title) {
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

    private void setToolbarTitle() {

        //Setting toolBar title
        if (this.getArguments() == null) {
            mainActivity.getToolbar().setTitle(R.string.new_tournament);
        } else {
            mainActivity.getToolbar().setTitle(R.string.edit_tournament);
        }
        mainActivity.getToolbar().setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }

    private void setParams() {

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
            spInPlayoff.setSelection(UiUtil.getSpinnerPosition(mTeamInPlayoff, teamInPlayoff) + 1);

            int mLoop = tournament.getLoops();
            spLoops.setSelection(UiUtil.getSpinnerPosition(mLoop, loops) + 1);

            //Checking number of games. If there is no game, we can edit team list
            if (tournament.getGameList().size() == 0) {
                teams = tournament.getTeamList();
                adapter = new TeamAdapter(teams);
                recyclerView.setAdapter(adapter);

            } else {
                addTeamButton.setVisibility(View.INVISIBLE);
                spTournType.setVisibility(View.INVISIBLE);
                spInPlayoff.setVisibility(View.INVISIBLE);
                spLoops.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void createNewTournament() {

        if (spInPlayoff.getSelectedItemId() != 0
                && spLoops.getSelectedItemId() != 0
                && spTournType.getSelectedItemId() != 0
                && !etTitle.getText().toString().isEmpty()
                && !etYear.getText().toString().isEmpty()) {

            int teamInPlaypff = Integer.parseInt(spInPlayoff.getSelectedItem().toString());
            int loops = Integer.parseInt(spLoops.getSelectedItem().toString());

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

    private void editTournament() {

        //Removing old Tournament and adding new
        if (tournament.getGameList().size() == 0) {

            if (spInPlayoff.getSelectedItemId() != 0
                    && spLoops.getSelectedItemId() != 0
                    && spTournType.getSelectedItemId() != 0
                    && !etTitle.getText().toString().isEmpty()
                    && !etYear.getText().toString().isEmpty()) {

                int teamInPlaypff = Integer.parseInt(spInPlayoff.getSelectedItem().toString());
                int loops = Integer.parseInt(spLoops.getSelectedItem().toString());

                //Checking quantity teams
                if (teams.size() >= teamInPlaypff) {

                    tournament.remove(bundle.getString("title"));
                    Tournament newTournament = new Tournament(etTitle.getText().toString(),
                            etYear.getText().toString(),
                            spTournType.getSelectedItem().toString(),
                            teams, null, teamInPlaypff, loops,
                            getActivity());

                    ///hide SoftInput From Window
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

        } else {

            if (!etTitle.getText().toString().isEmpty() && !etYear.getText().toString().isEmpty()) {

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

                ///hide SoftInput From Window
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //Backing to StartFragment
                teams.clear();
                fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

            } else {
                Toast.makeText(getActivity(), R.string.enter_all_data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initUI() {

        //Getting entry fields
        final TextInputLayout tilTitle = (TextInputLayout) getActivity().findViewById(R.id.tournTitle);
        etTitle = tilTitle.getEditText();

        final TextInputLayout tilYear = (TextInputLayout) getActivity().findViewById(R.id.tournYear);
        etYear = tilYear.getEditText();

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        etYear.setText(Integer.toString(year));


        //Getting all spinners and setting him parameters
        spTournType = (MaterialSpinner) getActivity().findViewById(R.id.spTournType);
        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tournType);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTournType.setAdapter(adapterOne);

        spInPlayoff = (MaterialSpinner) getActivity().findViewById(R.id.team_in_playoff);
        ArrayAdapter<String> adapterTeamInPlayoff = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, teamInPlayoff);
        adapterTeamInPlayoff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInPlayoff.setAdapter(adapterTeamInPlayoff);

        spLoops = (MaterialSpinner) getActivity().findViewById(R.id.loops);
        ArrayAdapter<String> adapterLoops = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, loops);
        adapterLoops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoops.setAdapter(adapterLoops);

        //Button for new teams
        addTeamButton = (Button) getActivity().findViewById(R.id.add_team_button);
        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newTeamDialog = new NewTeamDialog();
                newTeamDialog.show(getFragmentManager(), "TAG");

            }
        });
    }

    public List<Team> getTeams() {
        return teams;
    }

    public TeamAdapter getAdapter() {
        return adapter;
    }
}
