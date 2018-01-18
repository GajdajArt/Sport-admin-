package com.labralab.sporttournament.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.labralab.sporttournament.TeamActivity;
import com.labralab.sporttournament.adapters.TournAdapter;
import com.labralab.sporttournament.fragments.TournamentFragment;
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

    private static final int ID_TOURNAMENT = 1;
    private static final int ID_TEAM = 2;
    private static final int ID_GAME = 3;

    final View view;
    final String title;

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
                            case ID_TOURNAMENT:
                                Bundle bundle = new Bundle();
                                bundle.putString("title", title);

                                TournamentFragment editTournamentFragment = new TournamentFragment();
                                editTournamentFragment.setArguments(bundle);

                                AppCompatActivity activity_1 = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager_1 = activity_1.getSupportFragmentManager();

                                fragmentManager_1.
                                        beginTransaction().
                                        replace(R.id.container, editTournamentFragment).
                                        addToBackStack(null).
                                        commit();
                                break;

                            case ID_TEAM:
                                Bundle teamBundle = new Bundle();
                                teamBundle.putString("teamTitle", title);
                                DialogFragment newTeamDialog = new NewTeamDialog();
                                newTeamDialog.setArguments(teamBundle);

                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                newTeamDialog.show(fragmentManager, "TAG");

                                break;

                            case ID_GAME:
                                AppCompatActivity activity_3 = (AppCompatActivity) view.getContext();
                                FragmentManager fragmentManager_3 = activity_3.getSupportFragmentManager();
                                editGameDialog.show(fragmentManager_3, "Tag");
                        }
                        break;

                    case ID_DELETE:
                        //Определение на каком RecyclerView совершено нажатие
                        switch (adapterID) {

                            //Tournament list
                            case ID_TOURNAMENT:

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setMessage("Удалить " + title + "?");
                                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Tournament tournament = new Tournament();
                                        tournament.remove(title);
                                        tournament.removePlayoff(title, view.getContext());

                                        MainActivity mainActivity = (MainActivity) view.getContext();
                                        mainActivity.getStartFragment().onStart();
                                        TournAdapter.items = Tournament.getTournList(mainActivity.getStartFragment());

                                        StartFragment startFragment = mainActivity.getStartFragment();
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

                            case ID_TEAM:

                                MainActivity mainActivity = (MainActivity) view.getContext();
                                TournamentFragment tournamentFragment = mainActivity.getTournamentFragment();

                                tournamentFragment.getTeams().
                                        remove(TournamentUtil.getTeam(tournamentFragment.getTeams(), title));
                                tournamentFragment.getAdapter().notifyDataSetChanged();
                                break;

                            case ID_GAME:

                                final String teameOne = bundle.getString("teamOne");
                                final String teameTwo = bundle.getString("teamTwo");

                                AlertDialog.Builder gameDelBuilder = new AlertDialog.Builder(view.getContext());
                                gameDelBuilder.setMessage(R.string.delete_that_game);
                                gameDelBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Tournament.getInstance().removeGame(teameOne, teameTwo);
                                        Tournament.getInstance().recreateTournament();

                                        TeamActivity teamActivity = (TeamActivity) view.getContext();

                                        teamActivity.getTeamTabFragment().teamListFragment.onStart();
                                        teamActivity.getTeamTabFragment().gameListFragment.onStart();
                                        teamActivity.getPlayoffFragment().onStart();
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
