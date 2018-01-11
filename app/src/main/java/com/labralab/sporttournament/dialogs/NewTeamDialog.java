package com.labralab.sporttournament.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.labralab.sporttournament.fragments.EditTournamentFragment;
import com.labralab.sporttournament.fragments.NewTournFragment;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.utils.TournamentUtil;


public class NewTeamDialog extends DialogFragment {

    String temeTitle;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        //object for working with Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //find the layout.dialog_task and all the elements within it.
        View container = inflater.inflate(R.layout.team_dialog_maket, null);

        final TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.edit_team_title);
        final EditText etTitle = tilTitle.getEditText();

        if (this.getArguments() == null) {

            //sets a title for the dialog
            builder.setTitle(R.string.new_team);


        } else {

            Bundle bundle = this.getArguments();
            temeTitle = bundle.getString("teamTitle");
            builder.setTitle(R.string.edit_team);
            etTitle.setText(bundle.getString("teamTitle"));

        }

        builder.setView(container);

        //Creates button OK in the bottom of the dialog
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Если диалог вызван дляредактирования команд, то следует удалить
                // из списка команду со старым названием
                if (NewTeamDialog.this.getArguments() != null) {

                    //Проверяем есть ли в списке команда с таким названием и добавляем новую команду
                    boolean isAdded = EditTournamentFragment.addTeam(etTitle.getText().toString());
                    if (isAdded) {

                        EditTournamentFragment.getTeams().
                                remove(TournamentUtil.getTeam(EditTournamentFragment.getTeams(), temeTitle));
                        EditTournamentFragment.getAdapter().notifyDataSetChanged();
                        dialog.dismiss();

                    } else {
                        Toast.makeText(getContext(), R.string.that_team_already_exists, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (NewTournFragment.adapter != null) {
                        //Проверяем есть ли в списке команда с таким названием и добавляем новую команду
                        boolean isAdded = NewTournFragment.addTeam(etTitle.getText().toString());
                        if (isAdded) {

                            NewTournFragment.adapter.notifyDataSetChanged();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(getContext(), R.string.that_team_already_exists, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(EditTournamentFragment.getAdapter() != null){
                        boolean isAdded = EditTournamentFragment.addTeam(etTitle.getText().toString());
                        if (isAdded) {

                            EditTournamentFragment.getAdapter().notifyDataSetChanged();
                            dialog.dismiss();


                        } else {
                            Toast.makeText(getContext(), R.string.that_team_already_exists, Toast.LENGTH_SHORT).show();
                        }
                    }

                }


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
        return alertDialog;

    }//onCreateDialog

    public void editTeam() {

    }
}//NewTeamDialog


