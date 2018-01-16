package com.labralab.sporttournament.models;


import android.content.Context;
import android.widget.Toast;

import com.labralab.sporttournament.db.RealmDB;
import com.labralab.sporttournament.db.TournamentRepository;
import com.labralab.sporttournament.fragments.StartFragment;
import com.labralab.sporttournament.utils.TournamentUtil;


import java.io.Serializable;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;


public class Tournament extends RealmObject implements Serializable {


    @Ignore
    private TournamentRepository repository;
    private String title;
    private String yearOfTourn;

    private RealmList<Team> teamList = new RealmList<>();

    private RealmList<Game> gameList = new RealmList<>();
    private String type;
    private int teamInPlayoff;
    private int loops;
    private int teamsCount;
    private int maxCountGame;
    private Playoff playoff;
    private Boolean isPlayoffFlag;

    private static Tournament instance;

    //Constructor for creating new Tournament and adding it to DB
    public Tournament(String title, String yearOfTourn, String type, List<Team> teamList,
                      List<Game> gameList, int teamInPlayoff, int loops, Context context) {
        this.title = title;
        this.yearOfTourn = yearOfTourn;
        this.teamList.addAll(teamList);
        this.type = type;
        this.teamInPlayoff = teamInPlayoff;
        this.loops = loops;
        this.isPlayoffFlag = false;

        if (gameList != null) {
            this.gameList.addAll(gameList);
        }

        if (gameList != null) {
            isPlayoff();
        }
        //Добавляем турнир в базу данных при создании


//        //TESTTESTTEST
//        for (int i = 0; i < 9; i++) {
//            Team team = new Team("team" + i);
//            teamList.add(team);
//        }
        teamsCount = teamList.size();
        maxCountGame = ((teamsCount * (teamsCount - 1)) / 2) * loops;

        if (!isPlayoffFlag) {
            repository = new RealmDB();
            repository.createTournament(this);
        }
    }

    //Constructor for creating new Tournament
    public Tournament(String title, String yearOfTourn, String type, List<Team> teamList,
                      List<Game> gameList, int teamInPlayoff, int loops, Boolean isPlayoffFlag) {
        this.title = title;
        this.yearOfTourn = yearOfTourn;
        this.teamList.addAll(teamList);
        this.type = type;
        this.teamInPlayoff = teamInPlayoff;
        this.loops = loops;
        this.isPlayoffFlag = isPlayoffFlag;

        if (gameList != null) {
            this.gameList.addAll(gameList);
        }

        teamsCount = teamList.size();
        maxCountGame = ((teamsCount * (teamsCount - 1)) / 2) * loops;
    }

    public Tournament(String title, String yearOfTourn, String type) {
        this.title = title;
        this.yearOfTourn = yearOfTourn;
        this.type = type;
    }

    //Constructor for getting Tournament from DB
    public Tournament(String title, Context context) {

        //Getting repository
        repository = new RealmDB();
        //Getting Tournament from repository
        Tournament tournament = repository.getTournament(title);

        //Setting params
        this.title = title;
        this.yearOfTourn = tournament.getYearOfTourn();
        this.teamList.addAll(tournament.getTeamList());
        this.type = tournament.getType();
        this.gameList.addAll(tournament.getGameList());
        this.teamInPlayoff = tournament.getTeamInPlayoff();
        this.loops = tournament.getLoops();
        this.isPlayoffFlag = tournament.getIsPlayoffFlag();


        teamsCount = teamList.size();

        maxCountGame = ((teamsCount * (teamsCount - 1)) / 2) * loops;

        if (isPlayoffFlag) {
            this.playoff = tournament.getPlayoff();
        }

    }

    public Tournament() {

    }

    //Methods for getting Instance
    public static Tournament getInstance(String title, Context context) {

        instance = new Tournament(title, context);
        return instance;
    }

    public static Tournament getInstance() {

        if (instance == null) {
            instance = new Tournament();
        }
        return instance;
    }

    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
    }


    //Getters and Setters
    public Boolean getIsPlayoffFlag() {
        return isPlayoffFlag;
    }

    public Playoff getPlayoff() {
        return playoff;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList.addAll(teamList);
    }

    public int getTeamInPlayoff() {
        return teamInPlayoff;
    }

    public int getLoops() {
        return loops;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public List<Team> getTeamList() {
        return teamList;
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

    public String getType() {
        return type;
    }

    public void setYearOfTourn(String yearOfTourn) {
        this.yearOfTourn = yearOfTourn;
    }

    public void setTeamList(RealmList<Team> teamList) {
        this.teamList = teamList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList.clear();
        this.gameList.addAll(gameList);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTeamInPlayoff(int teamInPlayoff) {
        this.teamInPlayoff = teamInPlayoff;
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

    public void setPlayoff(Playoff playoff) {
        this.playoff = playoff;
    }

    public void setIsPlayoffFlag(Boolean playoffFlag) {
        isPlayoffFlag = playoffFlag;
    }

    public static void setInstance(Tournament instance) {
        Tournament.instance = instance;
    }

    public static List<Tournament> getTournList(StartFragment startFragment) {

//        RealmDB realmDB = new RealmDB();
//        List<Tournament> tournaments = realmDB.getTournList();
        TournamentRepository repository = new RealmDB();
        List<Tournament> tournaments = repository.readDBTourn(startFragment);

        return tournaments;
    }

    //Method for adding new game
    public void addGame(String team_1, String team_2, int score_1, int score_2, Context context, int day, int month, int year) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Team team_one = teamList.get(TournamentUtil.getTeam(teamList, team_1));
        Team team_two = teamList.get(TournamentUtil.getTeam(teamList, team_2));

        this.gameList.add(new Game(team_one, team_two, score_1, score_2, day, month, year));

        team_one.plusGame();
        team_two.plusGame();

        if (score_1 == 100 || score_2 == 0) {

            team_one.plusGameWon();
            team_two.plusGameLost();

            team_one.plusPoints();
            team_one.plusPoints();

            if (type.equals("Футбол")) {
                team_one.plusPoints();
            }
            Toast.makeText(context, "Неявка " + team_2, Toast.LENGTH_LONG).show();
            return;
        }
        if (score_1 == 0 || score_2 == 100) {

            team_two.plusGameWon();
            team_one.plusGameLost();

            team_two.plusPoints();
            team_two.plusPoints();

            if (type.equals("Футбол")) {
                team_two.plusPoints();
            }
            Toast.makeText(context, "Неявка " + team_1, Toast.LENGTH_LONG).show();
            return;
        }


        if (score_1 == score_2) {

            if (type.equals("Футбол")) {

                team_one.plusPoints();
                team_one.plusPoints();

                team_two.plusPoints();
                team_two.plusPoints();

            } else {
                Toast.makeText(context, "Не может быть равного счета", Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (score_1 > score_2) {

            team_one.plusGameWon();
            team_two.plusGameLost();

            team_one.plusPoints();
            team_one.plusPoints();

            if (type.equals("Футбол")) {
                team_one.plusPoints();
                team_two.plusPoints();
            }

        } else {
            if (score_1 < score_2) {

                team_two.plusGameWon();
                team_one.plusGameLost();

                team_two.plusPoints();
                team_two.plusPoints();

                if (type.equals("Футбол")) {
                    team_one.plusPoints();
                    team_two.plusPoints();
                }
            }
        }
        remove(title);
        repository.createTournament(this);
        isPlayoff();

        realm.commitTransaction();
    }

    //Method for removing game
    public void removeGame(String teamOne, String teamTwo) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        int id = TournamentUtil.getGame(gameList, teamOne, teamTwo);

        Team team_one = teamList.get(TournamentUtil.getTeam(teamList, teamOne));
        Team team_two = teamList.get(TournamentUtil.getTeam(teamList, teamTwo));

        team_one.minusGame();
        team_two.minusGame();

        if (gameList.get(id).getScore_1() > gameList.get(id).getScore_2()) {

            team_one.minusGameWon();
            team_two.minusGameLost();

            team_one.minusPoints();
            team_one.minusPoints();

            if (type.equals("Футбол")) {
                team_one.minusPoints();
                team_two.minusPoints();
            }

        } else {
            if (gameList.get(id).getScore_1() < gameList.get(id).getScore_2()) {

                team_two.minusGameWon();
                team_one.minusGameLost();

                team_two.minusPoints();
                team_two.minusPoints();

                if (type.equals("Футбол")) {
                    team_two.minusPoints();
                    team_one.minusPoints();
                }
            }
        }
        gameList.remove(id);
        isPlayoff();
        realm.commitTransaction();

        recreateTournament();

    }

    //Method for checking for the same games
    public Boolean checkGame(String teamOne, String teamTwo) {
        int i = TournamentUtil.chackGame(gameList, teamOne, teamTwo);
        if (i < loops) {
            return true;
        } else {
            return false;
        }
    }

    //Method for removing Tournament
    public void remove(String title) {
        repository = new RealmDB();
        repository.delTournament(title, isPlayoffFlag);
    }

    //Method for removing playoff
    public void removePlayoff(String title, Context context) {
//        repository = new TournDBHelper(context);
//        repository.delPlayoff(title);
    }

    //Method fof making changes
    public void recreateTournament() {
        remove(title);
        repository.createTournament(this);
    }

    //Method for checking whether the playoff started
    public boolean isPlayoff() {

        Realm realm = Realm.getDefaultInstance();

        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        if (isPlayoffFlag == false) {
            if (gameList.size() >= maxCountGame) {
                repository = new RealmDB();
//                //Ставим флаг что плей-офф начался
                isPlayoffFlag = true;
//                recreateTournament(context);
                playoff = new Playoff(teamList, teamInPlayoff, title);
                repository.delTournament(title, isPlayoffFlag);
                repository.createTournament(this);
            }
        } else {
            if (gameList.size() < maxCountGame) {
                playoff = null;
                isPlayoffFlag = false;
            } else {
                playoff = repository.getTournament(title).getPlayoff();
            }
        }

        if (flag) {
            realm.commitTransaction();
        }

        return isPlayoffFlag;
    }

}
