package com.labralab.sporttournament.db;

import android.database.sqlite.SQLiteDatabase;

import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.fragments.StartFragment;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Playoff;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;

import java.util.List;

/**
 * Created by pc on 24.09.2017.
 */

public interface TournamentRepository {


    void createTournament(Tournament tournament);

    List<Tournament> readDBTourn(StartFragment startFragment);

    void delTournament(String title, Boolean isPlayoff);

    Tournament getTournament(String title);

}
