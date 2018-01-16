package com.labralab.sporttournament.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.labralab.sporttournament.adapters.TournAdapter;
import com.labralab.sporttournament.fragments.EditTournamentFragment;
import com.labralab.sporttournament.fragments.StartFragment;
import com.labralab.sporttournament.fragments.TeamTabFragment;
import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.TournamentUtil;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

/**
 * Created by pc on 03.10.2017.
 */

public class QuickActionDialog {

    private static final int ID_EDIT = 1;
    private static final int ID_DELETE = 2;

    final View view;
    final String title;
    int position;

    private QuickAction quickAction;
    int adapterID;
    DialogFragment editGameDialog;
    Bundle bundle;

    public QuickActionDialog(View view, int adapterID, String title) {
        this.view = view;
        this.adapterID = adapterID;
        this.title = title;
    }

    public QuickActionDialog(View view, int adapterID, DialogFragment editGameDialog, Bundle bundle) {
        this.view = view;
        this.adapterID = adapterID;
        this.editGameDialog = editGameDialog;
        this.title = null;
        this.bundle = bundle;
    }


    public void onActionStart() {

        QuickAction.setDefaultColor(R.color.colorAccent);
        QuickAction.setDefaultTextColor(R.color.colorAccent);

        ActionItem editItem = new ActionItem(ID_EDIT, R.drawable.ic_mode_edit_white_24dp);
        ActionItem deleteItem = new ActionItem(ID_DELETE, R.drawable.ic_delete_forever_white_24dp);

        //use setSticky(true) to disable QuickAction dialog being dismissed after out_doun item is clicked
        editItem.setSticky(true);
        deleteItem.setSticky(true);

        quickAction = new QuickAction(view.getContext(), QuickAction.HORIZONTAL);
        quickAction.setColorRes(R.color.colorAccent);
        quickAction.setTextColorRes(R.color.colorPrimaryDark);


        quickAction.addActionItem(editItem, deleteItem);
        quickAction.setTextColor(Color.YELLOW);

        quickAction.show(view);

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(ActionItem item) {
                quickAction.dismiss();

                switch (item.getActionId()) {

                    case ID_EDIT:
                        switch (adapterID) {

                            //Tournament list
                            case 1:
                                Bundle bundle = new Bundle();
                                bundle.putString("title", title);

                                EditTournamentFragment editTournamentFragment = new EditTournamentFragment();
                                editTournamentFragment.setArguments(bundle);

                                AppCompatActivity activity_1 = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager_1 = activity_1.getSupportFragmentManager();

                                fragmentManager_1.
                                        beginTransaction().
                                        replace(R.id.container, editTournamentFragment).
                                        addToBackStack(null).
                                        commit();
                                break;
                            case 2:
                                Bundle teamBundle = new Bundle();
                                teamBundle.putString("teamTitle", title);
                                DialogFragment newTeamDialog = new NewTeamDialog();
                                newTeamDialog.setArguments(teamBundle);

                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                newTeamDialog.show(fragmentManager, "TAG");

                                break;
                            case 3:
                                AppCompatActivity activity_3 = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager_3 = activity_3.getSupportFragmentManager();
                                editGameDialog.show(fragmentManager_3, "Tag");
                        }
                        break;

                    case ID_DELETE:
                        //Определение на каком RecyclerView совершено нажатие
                        switch (adapterID) {

                            //Tournament list
                            case 1:

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setMessage("Удалить " + title + "?");
                                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Tournament tournament = new Tournament();
                                        tournament.remove(title);
                                        tournament.removePlayoff(title, view.getContext());
                                        MainActivity.getStartFragment().onStart();
                                        TournAdapter.items = Tournament.getTournList(MainActivity.getStartFragment());

                                        StartFragment startFragment = MainActivity.getStartFragment();
                                        startFragment.getAdapter().notifyDataSetChanged();

                                        dialog.cancel();
                                    }
                                });
                                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                builder.show();
                                break;
                            case 2:

                                EditTournamentFragment.getTeams().
                                        remove(TournamentUtil.getTeam(EditTournamentFragment.getTeams(), title));
                                EditTournamentFragment.getAdapter().notifyDataSetChanged();
                                break;
                            case 3:

                                final String teameOne = bundle.getString("teamOne");
                                final String teameTwo = bundle.getString("teamTwo");

                                AlertDialog.Builder gameDelBuilder = new AlertDialog.Builder(view.getContext());
                                gameDelBuilder.setMessage(R.string.delete_that_game);
                                gameDelBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Tournament.getInstance().removeGame(teameOne, teameTwo);
                                        Tournament.getInstance().recreateTournament();

                                        TeamTabFragment.teamListFragment.onStart();
                                        TeamTabFragment.gameListFragment.onStart();
                                        dialog.cancel();


                                    }
                                });
                                gameDelBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });
                                AlertDialog alertDelGameDialog = gameDelBuilder.create();
                                gameDelBuilder.show();

                        }
                }
            }
        });
    }
}
