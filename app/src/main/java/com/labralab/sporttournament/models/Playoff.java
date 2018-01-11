package com.labralab.sporttournament.models;

import android.content.Context;

import com.google.firebase.database.IgnoreExtraProperties;
import com.labralab.sporttournament.db.RealmDB;
import com.labralab.sporttournament.db.TournamentRepository;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;


@IgnoreExtraProperties
public class Playoff extends RealmObject {

    @Ignore
    Realm realm;

    private String playoffTitle;

    private int countGames = -1;
    private int teamInPlayoff;

    private RealmList<Game> playoffGameList;
    private RealmList<Team> playoffTeamList;

    private RealmList<Game> playoffFirstTurGames;
    private RealmList<Game> playoffSecondTurGames;
    private RealmList<Game> playoffLastTurGames;
    @Ignore
    private TournamentRepository repository;

    //Constructors
    public Playoff(List<Team> teamList, int teamInPlayoff, String title) {

        realm = Realm.getDefaultInstance();

        Set<Team> items = new TreeSet<>(teamList);
        teamList.clear();
        teamList.addAll(items);

        if (countGames < 0) {
            this.countGames = 0;
            this.playoffTitle = title;
            this.teamInPlayoff = teamInPlayoff;

            this.playoffTeamList = new RealmList<>();
            this.playoffGameList = new RealmList<>();
            this.playoffLastTurGames = new RealmList<>();
            this.playoffSecondTurGames = new RealmList<>();

            for (int i = 0; i < teamInPlayoff; i++) {

                Team team = new Team(teamList.get(i).getTitle());
                playoffTeamList.add(team);
            }

            onGameListCreate();
            addEmptyGames();

        } else {

            //get playoff from DB
            Playoff playoff = new Playoff();
            this.playoffTitle = playoff.getPlayoffTitle();
            this.countGames = playoff.getCountGames();
            this.teamInPlayoff = playoff.getTeamInPlayoff();
            this.playoffGameList.addAll(playoff.getPlayoffGameList());
            this.playoffTeamList.addAll(playoff.getPlayoffTeamList());

        }
    }

    public Playoff(String playoffTitle, int countGames, int teamInPlayoff, List<Game> playoffGameList, List<Team> playoffTeamList) {


        this.playoffTitle = playoffTitle;
        this.countGames = countGames;
        this.teamInPlayoff = teamInPlayoff;
        this.playoffGameList.addAll(playoffGameList);
        this.playoffTeamList.addAll(playoffTeamList);
    }

    public Playoff() {
        this.playoffTeamList = new RealmList<>();
        this.playoffGameList = new RealmList<>();
        this.playoffLastTurGames = new RealmList<>();
        this.playoffFirstTurGames = new RealmList<>();
        this.playoffSecondTurGames = new RealmList<>();
    }

    private void onGameListCreate() {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        List<Team> newTeamList = new ArrayList<>();
        for (int i = 0; i < playoffTeamList.size() / 2; i++) {

            Game game = new Game(playoffTeamList.get(i),
                    playoffTeamList.get((playoffTeamList.size() - 1) - i));

            newTeamList.add(playoffTeamList.get(i));
            newTeamList.add(playoffTeamList.get((playoffTeamList.size() - 1) - i));

            playoffGameList.add(game);
        }

        playoffTeamList.addAll(newTeamList);

        if (flag) {
            realm.commitTransaction();
        }
    }

    //Getters
    public String getPlayoffTitle() {
        return playoffTitle;
    }

    public int getCountGames() {
        return countGames;
    }

    public int getTeamInPlayoff() {
        return teamInPlayoff;
    }

    public List<Game> getPlayoffGameList() {
        return playoffGameList;
    }

    public List<Team> getPlayoffTeamList() {
        return playoffTeamList;
    }


    public List<Game> getPlayoffFirstTurGames() {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        playoffFirstTurGames = new RealmList<>();

        //Если команд в плей-офф больше двух:
        if (teamInPlayoff > 2) {
            for (int i = 0; i < teamInPlayoff / 2; i++) {
                playoffFirstTurGames.add(playoffGameList.get(i));
            }
        } else {
            //Если меньше двух то первый тур является единственным
            playoffFirstTurGames = playoffGameList;
        }

        if (flag) {
            realm.commitTransaction();
        }
        return playoffFirstTurGames;
    }

    public List<Game> getPlayoffSecondTurGames() {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        playoffSecondTurGames = new RealmList<>();

        //Второй тур формируем только в случае если 8 командв плей-офф
        if (teamInPlayoff == 8) {
            for (int i = 4; i < 6; i++) {
                playoffSecondTurGames.add(playoffGameList.get(i));
            }
        }

        if (flag) {
            realm.commitTransaction();
        }

        return playoffSecondTurGames;
    }

    public List<Game> getPlayoffLastTurGames() {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        //В последнем туре всегда только последняя игра
        playoffLastTurGames = new RealmList<>();
        playoffLastTurGames.add(playoffGameList.get(playoffGameList.size() - 1));

        if (flag) {
            realm.commitTransaction();
        }


        return playoffLastTurGames;
    }

    //Method for adding game in Playoff
    public void addGame(String team_1, String team_2, int score_1, int score_2, Context context, int day, int month, int year) {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        Team team_one = playoffTeamList.get(TournamentUtil.getTeam(playoffTeamList, team_1));
        Team team_two = playoffTeamList.get(TournamentUtil.getTeam(playoffTeamList, team_2));

        Team winner = new Team();

        int currentPos = TournamentUtil.getGame(playoffGameList, team_1, team_2);
        int nextPos;
        countGames++;


        team_one.plusGame();
        team_two.plusGame();

        if (score_1 > score_2) {

            team_one.plusGameWon();
            team_two.plusGameLost();

            team_one.plusPoints();
            team_one.plusPoints();

            winner = team_one;

        } else {
            if (score_1 < score_2) {

                team_two.plusGameWon();
                team_one.plusGameLost();

                team_two.plusPoints();
                team_two.plusPoints();

                winner = team_two;
            }
        }

        //if that game from first tur
        Game game = playoffGameList.get(currentPos);
        game.setScore_1(score_1);
        game.setScore_2(score_2);
        game.setDay(day);
        game.setMonth(month);
        game.setYear(year);

        nextPos = nextGameForWinner(team_one, team_two, currentPos);
        if (playoffGameList.get(nextPos).getTeam_1().getTitle().equals("__")) {
            playoffGameList.get(nextPos).setTeam_1(winner);
        } else {
            playoffGameList.get(nextPos).setTeam_2(winner);
        }


        repository = new RealmDB();
        Tournament tournament = repository.getTournament(playoffTitle);
        tournament.setPlayoff(this);
        repository.delTournament(playoffTitle, true);
        repository.createTournament(tournament);

        if (flag) {
            realm.commitTransaction();
        }
    }

    //Method for filling future games in the playoff grid
    private void addEmptyGames() {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        //Create empty teams and games
        Team teamOne = new Team("__");
        playoffTeamList.add(teamOne);
        Game game = new Game(teamOne, teamOne, 0, 0, 0, 0, 0);
        playoffLastTurGames.add(game);
        for (int i = 0; i < 2; i++) {
            playoffSecondTurGames.add(game);
        }

        //Add empty games to the general list
        if (teamInPlayoff > 2) {
            playoffGameList.addAll(playoffLastTurGames);
        }
        if (teamInPlayoff == 8) {
            playoffGameList.addAll(playoffSecondTurGames);
        }

        if (flag) {
            realm.commitTransaction();
        }
    }

    //Method for placing the winner's team in the game of the next round
    private int nextGameForWinner(Team teamOne, Team teamTwo, int currentPosition) {

        realm = Realm.getDefaultInstance();
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        int nextPosition = 0;
        //Depending on the number of teams in the tournament
        switch (teamInPlayoff) {
            case 8:
                //Depending on the position of the current game
                if (currentPosition < 2) {
                    nextPosition = 4;
                }
                if (currentPosition > 1 && currentPosition < 4) {
                    nextPosition = 5;
                }
                if (currentPosition > 3) {
                    nextPosition = 6;
                }
                break;
            case 4:
                //Immediately to the final
                nextPosition = 2;
                break;
        }

        if (flag) {
            realm.commitTransaction();
        }

        //Position return value
        return nextPosition;
    }

    public void setPlayoffTitle(String playoffTitle) {
        this.playoffTitle = playoffTitle;
    }

    public void setCountGames(int countGames) {
        this.countGames = countGames;
    }

    public void setTeamInPlayoff(int teamInPlayoff) {
        this.teamInPlayoff = teamInPlayoff;
    }

    public void setPlayoffGameList(List<Game> playoffGameList) {
        this.playoffGameList.addAll(playoffGameList);
    }

    public void setPlayoffTeamList(List<Team> playoffTeamList) {
        this.playoffTeamList.addAll(playoffTeamList);
    }

    public void setPlayoffFirstTurGames(List<Game> playoffFirstTurGames) {
        this.playoffFirstTurGames.addAll(playoffFirstTurGames);
    }

    public void setPlayoffSecondTurGames(List<Game> playoffSecondTurGames) {
        this.playoffSecondTurGames.addAll(playoffSecondTurGames);
    }

    public void setPlayoffLastTurGames(List<Game> playoffLastTurGames) {
        this.playoffLastTurGames.addAll(playoffLastTurGames);
    }

    public TournamentRepository getRepository() {
        return repository;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public void setPlayoffGameList(RealmList<Game> playoffGameList) {
        this.playoffGameList = playoffGameList;
    }

    public void setPlayoffTeamList(RealmList<Team> playoffTeamList) {
        this.playoffTeamList = playoffTeamList;
    }

    public void setPlayoffFirstTurGames(RealmList<Game> playoffFirstTurGames) {
        this.playoffFirstTurGames = playoffFirstTurGames;
    }

    public void setPlayoffSecondTurGames(RealmList<Game> playoffSecondTurGames) {
        this.playoffSecondTurGames = playoffSecondTurGames;
    }

    public void setPlayoffLastTurGames(RealmList<Game> playoffLastTurGames) {
        this.playoffLastTurGames = playoffLastTurGames;
    }

    public void setRepository(TournamentRepository repository) {
        this.repository = repository;
    }
}
