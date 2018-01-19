package com.labralab.sporttournament.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.Button;


import com.labralab.sporttournament.TeamActivity;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.Calendar;
import java.util.List;

public class DateDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    GameDialog gameDialog;
    DialogInterface dialog;
    public int[] date;
    Bundle bundle;

    public void getDate(DialogInterface dialog
            , GameDialog gameDialog
            , FragmentManager fragmentManager
            , String TAG) {
        this.dialog = dialog;
        this.gameDialog = gameDialog;
        this.show(fragmentManager, TAG);
    }

    public void getDate(Bundle bundle, FragmentManager fragmentManager, String TAG) {
        this.bundle = bundle;
        this.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = 1;
        int month = 1;
        int day = 1;

        //dateTextView OnClick
        if (bundle != null) {

            Tournament tournament = Tournament.getInstance();
            List<Game> gameList = tournament.getGameList();
            Game game = gameList.get(TournamentUtil.getGame(gameList,
                    bundle.getString("teamOne"),
                    bundle.getString("teamTwo")));

            year = game.getYear();
            month = game.getMonth();
            day = game.getDay();
        }

        //new or edit game
        if (gameDialog != null) {
            if (gameDialog.getArguments() != null) {

                if (gameDialog.getArguments().getInt("playoffID") == gameDialog.PLAYOFF_ID) {
                    // определяем текущую дату
                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH) + 1;
                    day = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    year = gameDialog.oldGame.getYear();
                    month = gameDialog.oldGame.getMonth();
                    day = gameDialog.oldGame.getDay();
                }

            } else {

                // определяем текущую дату
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);
            }
        }


        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle("Выберите дату");

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton = ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("ок");

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {

        date = new int[3];
        date[0] = year;
        date[1] = month;
        date[2] = day;

        if (gameDialog != null) {
            gameDialog.onOkClick(dialog, date);
        } else {

            Tournament.getInstance().removeGame(bundle.getString("teamOne"), bundle.getString("teamTwo"));
            Tournament.getInstance().addGame(bundle.getString("teamOne")
                    , bundle.getString("teamTwo")
                    , Integer.parseInt(bundle.getString("scoreOne"))
                    , Integer.parseInt(bundle.getString("scoreTwo"))
                    , getContext()
                    , day, month, year);

            TeamActivity teamActivity = (TeamActivity) getActivity();
            teamActivity.getTeamTabFragment().gameListFragment.onStart();
        }

    }


}