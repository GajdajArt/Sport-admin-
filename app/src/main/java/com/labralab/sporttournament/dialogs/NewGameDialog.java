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


import com.labralab.sporttournament.fragments.TeamTabFragment;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;

import java.util.Calendar;
import java.util.List;


public class NewGameDialog extends DialogFragment {

    boolean teamOneTeameTwo;

    EditText edScoreOne;
    EditText edScoreTwo;

    Button positiveButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //sets a title for the dialog task
        builder.setTitle(R.string.new_game);

        //object for working with Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();


        //find the layout.dialog_task and all the elements within it.
        final View container = inflater.inflate(R.layout.game_dialog_maket, null);

        final Spinner spTeamOne = (Spinner) container.findViewById(R.id.spinner);
        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                readTeamTitle(Tournament.getInstance().getTeamList()));
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeamOne.setAdapter(adapterOne);

        final Spinner spTeamTwo = (Spinner) container.findViewById(R.id.team_1);
        ArrayAdapter<String> adapterTwo = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                readTeamTitle(Tournament.getInstance().getTeamList()));
        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeamTwo.setAdapter(adapterTwo);

        edScoreOne = (EditText) container.findViewById(R.id.score_1);
        edScoreTwo = (EditText) container.findViewById(R.id.score_2);

        builder.setView(container);

        //Creates button ADD in the bottom of the dialog
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                String teamOne = spTeamOne.getSelectedItem().toString();
                String teamTwo = spTeamTwo.getSelectedItem().toString();


                int scoreOne = Integer.parseInt(edScoreOne.getText().toString());
                int scoreTwo = Integer.parseInt(edScoreTwo.getText().toString());


                if (Tournament.getInstance().checkGame(teamOne, teamTwo)) {

                    Tournament.getInstance().addGame(teamOne, teamTwo, scoreOne, scoreTwo, getActivity(), day, month, year);

                    TeamTabFragment.teamListFragment.onStart();
                    TeamTabFragment.gameListFragment.onStart();
                    dialog.dismiss();

                } else {
                    Toast.makeText(getActivity(), R.string.that_game_already_exist, Toast.LENGTH_SHORT).show();
                }


            }
        });

        //Creates button CANCEL in the bottom of the dialog
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    private String[] readTeamTitle(List<Team> teams) {

        String[] titleList = new String[teams.size()];
        for (int i = 0; i < teams.size(); i++) {

            String teamTitle = teams.get(i).getTitle();
            titleList[i] = teamTitle;
        }


        return titleList;
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

