package com.labralab.sporttournament.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.labralab.sporttournament.TeamActivity;
import com.labralab.sporttournament.fragments.TeamTabFragment;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Playoff;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.Calendar;
import java.util.List;


public class GameDialog extends DialogFragment {

    static final int PLAYOFF_ID = 1;
    Playoff playoff;

    AlertDialog.Builder builder;
    View container;

    boolean teamOneTeameTwo;

    EditText edScoreOne;
    EditText edScoreTwo;

    Spinner spTeamOne;
    Spinner spTeamTwo;

    Button positiveButton;

    List<Team> teamList;
    List<Game> gameList;
    Game oldGame;

    String team_One;
    String team_Two;

    TeamActivity teamActivity;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        teamActivity = (TeamActivity) getActivity();
        teamList = Tournament.getInstance().getTeamList();
        gameList = Tournament.getInstance().getGameList();

        builder = new AlertDialog.Builder(getActivity());
        setTitle();

        //object for working with Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //find the layout.dialog_task and all the elements within it.
        container = inflater.inflate(R.layout.game_dialog_maket, null);

        initUI();

        if (this.getArguments() != null) {
            if (this.getArguments().getInt("playoffID") == PLAYOFF_ID) {

                playoff = Tournament.getInstance().getPlayoff();
                teamList = playoff.getPlayoffTeamList();
                gameList = playoff.getPlayoffGameList();
                spTeamOne.setEnabled(false);
                spTeamTwo.setEnabled(false);
            }
        }

        setParams();

        builder.setView(container);

        //Creates button OK in the bottom of the dialog
        builder.setPositiveButton("Далее", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DateDialog dateDialog = new DateDialog();
                dateDialog.getDate(dialog,GameDialog.this, teamActivity.getSupportFragmentManager(), "TAG");

            }

        });

        //Creates button CANCEL in the bottom of the dialog
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                String teamOne = spTeamOne.getSelectedItem().toString();
                String teamTwo = spTeamTwo.getSelectedItem().toString();
                positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                if (teamOne == teamTwo) {
                    positiveButton.setEnabled(false);

                }

            }
        });

        spTeamOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String teamOne = spTeamOne.getSelectedItem().toString();
                String teamTwo = spTeamTwo.getSelectedItem().toString();
                if (teamOne != teamTwo) {
                    teamOneTeameTwo = true;
                    positiveButtonEnable();
                } else {
                    positiveButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTeamTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String teamOne = spTeamOne.getSelectedItem().toString();
                String teamTwo = spTeamTwo.getSelectedItem().toString();
                if (teamOne != teamTwo) {
                    teamOneTeameTwo = true;
                    positiveButtonEnable();
                } else {
                    positiveButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edScoreOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                positiveButtonEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edScoreTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                positiveButtonEnable();

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        return alertDialog;
    }

    public void onOkClick(DialogInterface dialog, int[] date){

//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//        int day = c.get(Calendar.DAY_OF_MONTH);

        int year = date[0];
        int month = date[1];
        int day = date[2];

        String teamOne = spTeamOne.getSelectedItem().toString();
        String teamTwo = spTeamTwo.getSelectedItem().toString();
        int scoreOne = Integer.parseInt(edScoreOne.getText().toString());
        int scoreTwo = Integer.parseInt(edScoreTwo.getText().toString());


        if (GameDialog.this.getArguments() == null) {

            if (Tournament.getInstance().checkGame(teamOne, teamTwo)) {

                Tournament.getInstance().addGame(teamOne
                        , teamTwo
                        , scoreOne
                        , scoreTwo
                        , getActivity()
                        , day, month, year);

            } else {
                Toast.makeText(getActivity(), R.string.that_game_already_exist, Toast.LENGTH_SHORT).show();
            }

        } else {

            if (GameDialog.this.getArguments().getInt("playoffID") == PLAYOFF_ID) {

                playoff.addGame(teamOne
                        , teamTwo
                        , scoreOne, scoreTwo, getContext()
                        , day, month, year);

            } else {

                Tournament.getInstance().removeGame(team_One, team_Two);
                Tournament.getInstance().addGame(teamOne
                        , teamTwo
                        , scoreOne, scoreTwo, getContext()
                        , day, month, year);

            }
        }

        teamActivity.getTeamTabFragment().teamListFragment.onStart();
        teamActivity.getTeamTabFragment().gameListFragment.onStart();
        teamActivity.getPlayoffFragment().onStart();
        dialog.dismiss();


    }

    private void setTitle() {

        if (this.getArguments() != null) {

            //if it is playoff game
            if (this.getArguments().getInt("playoffID") == 1) {
                builder.setTitle(R.string.game);
            } else {
                //if edit tournament game
                builder.setTitle(R.string.edit_game);
            }
        } else {
            //if it is new game
            builder.setTitle(R.string.new_game);
        }
    }

    private void setParams() {

        if (this.getArguments() != null) {

            String score_One = this.getArguments().getString("scoreOne");
            edScoreOne.setText(score_One);
            String score_Two = this.getArguments().getString("scoreTwo");
            edScoreTwo.setText(score_Two);

            team_One = this.getArguments().getString("teamOne");
            spTeamOne.setSelection(TournamentUtil.getTeam(teamList, team_One));

            team_Two = this.getArguments().getString("teamTwo");
            spTeamTwo.setSelection(TournamentUtil.getTeam(teamList, team_Two));

            oldGame = gameList.get(TournamentUtil.getGame(gameList, team_One, team_Two));
        }
    }

    private void initUI() {

        spTeamOne = (Spinner) container.findViewById(R.id.spinner);
        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                readTeamTitle(teamList));

        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeamOne.setAdapter(adapterOne);

        spTeamTwo = (Spinner) container.findViewById(R.id.team_1);
        ArrayAdapter<String> adapterTwo = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                readTeamTitle(teamList));

        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeamTwo.setAdapter(adapterTwo);

        edScoreOne = (EditText) container.findViewById(R.id.score_1);
        edScoreTwo = (EditText) container.findViewById(R.id.score_2);
    }


    private String[] readTeamTitle(List<Team> teams) {

        String[] titleList = new String[teams.size()];
        for (int i = 0; i < teams.size(); i++) {

            String teamTitle = teams.get(i).getTitle();
            titleList[i] = teamTitle;
        }


        return titleList;
    }

    private int getIdItemTeam(String title, List<Team> teams) {
        int id = 0;
        String[] titleList = readTeamTitle(teams);
        for (int i = 0; i < titleList.length; i++) {
            String t = titleList[i];
            if (t.equals(title)) {
                id = i;
            }
        }
        return id;
    }

    private void positiveButtonEnable() {

        if (teamOneTeameTwo
                && edScoreTwo.getText().toString().trim().isEmpty() == false
                && edScoreOne.getText().toString().trim().isEmpty() == false) {
            positiveButton.setEnabled(true);
        } else {
            positiveButton.setEnabled(false);
        }

    }
}

