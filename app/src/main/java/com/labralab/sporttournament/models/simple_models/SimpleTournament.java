package com.labralab.sporttournament.models.simple_models;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.Exclude;
import com.labralab.sporttournament.db.RealmDB;
import com.labralab.sporttournament.db.TournDBHelper;
import com.labralab.sporttournament.db.TournamentRepository;
import com.labralab.sporttournament.fragments.StartFragment;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Playoff;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.annotations.Ignore;

/**
 * Created by pc on 25.12.2017.
 */

public class SimpleTournament {

    private String title;
    private String yearOfTourn;
    private List<Team> teamList = new RealmList<>();
    private List<Game> gameList = new RealmList<>();
    private String type;
    private int teamInPlayoff;
    private int loops;
    private int teamsCount;
    private int maxCountGame;
    private SimplePlayoff playoff;
    private Boolean isPlayoffFlag;

    public SimpleTournament() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYearOfTourn() {
        return yearOfTourn;
    }

    public void setYearOfTourn(String yearOfTourn) {
        this.yearOfTourn = yearOfTourn;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTeamInPlayoff() {
        return teamInPlayoff;
    }

    public void setTeamInPlayoff(int teamInPlayoff) {
        this.teamInPlayoff = teamInPlayoff;
    }

    public int getLoops() {
        return loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    public int getTeamsCount() {
        return teamsCount;
    }

    public void setTeamsCount(int teamsCount) {
        this.teamsCount = teamsCount;
    }

    public int getMaxCountGame() {
        return maxCountGame;
    }

    public void setMaxCountGame(int maxCountGame) {
        this.maxCountGame = maxCountGame;
    }

    public SimplePlayoff getPlayoff() {
        return playoff;
    }

    public void setPlayoff(SimplePlayoff playoff) {
        this.playoff = playoff;
    }

    public Boolean getPlayoffFlag() {
        return isPlayoffFlag;
    }

    public void setPlayoffFlag(Boolean playoffFlag) {
        isPlayoffFlag = playoffFlag;
    }
}
