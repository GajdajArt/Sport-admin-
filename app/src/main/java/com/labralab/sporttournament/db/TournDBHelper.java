package com.labralab.sporttournament.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.labralab.sporttournament.MainActivity;
import com.labralab.sporttournament.fragments.StartFragment;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Playoff;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.ArrayList;
import java.util.List;

public class TournDBHelper extends SQLiteOpenHelper implements TournamentRepository {

    public static final String DB_NAME = "Tournaments";
    public static final int VERSION = 1;
    Context context;


    public TournDBHelper(Context context) {

        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TOUR(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TITLE TEXT, "
                + "TYPE TEXT, "
                + "TEAM_IN_PLAYOFF INTEGER, "
                + "LOOPS INTEGER, "
                + "IS_PLAYOFF INTEGER, "
                + "YEAR TEXT);");

        db.execSQL("CREATE TABLE PLAYOFF(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TITLE TEXT, "
                + "GAME_COUNT INTEGER, "
                + "TEAM_IN_PLAYOFF INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createTournament(String tournTitle, String year, String type, List<Team> teamItems,
                                 List<Game> gameList, int teamInPlayoff, int loops, Boolean isPlayoff) {

        String title = tournTitle.replace(" ", "_");
        int bool;
        if (isPlayoff) {
            bool = 1;
        } else {
            bool = 0;
        }

        try {
            //Добавляем параметры нового турнира в таблицу БД
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues tournValues = new ContentValues();
            tournValues.put("TITLE", title);
            tournValues.put("TYPE", type);
            tournValues.put("TEAM_IN_PLAYOFF", teamInPlayoff);
            tournValues.put("LOOPS", loops);
            tournValues.put("YEAR", year);
            tournValues.put("IS_PLAYOFF", bool);
            db.insert("TOUR", null, tournValues);

            createTeamTable(db, title);

            createGameTable(db, title);
            //Добавляем команды из списка в таблицу БД
            for (int i = 0; i < teamItems.size(); i++) {

                Team team = teamItems.get(i);
                ContentValues teamValues = new ContentValues();

                String teamTitle = team.getTitle().replace(" ", "_");
                teamValues.put("TEAM", teamTitle);
                teamValues.put("POINTS", team.getPoints());
                teamValues.put("GAMES", team.getGames());
                teamValues.put("GAMES_WON", team.getGames_won());
                teamValues.put("GAME_LOST", team.getGames_lost());
                db.insert(title, null, teamValues);
            }
            if (gameList != null) {
                //Добавляем игры из списка в ьаблицу БД
                for (int i = 0; i < gameList.size(); i++) {

                    Game game = gameList.get(i);
                    ContentValues gameValues = new ContentValues();
                    gameValues.put("TEAM_ONE", game.getTeam_1().getTitle());
                    gameValues.put("TEAM_TWO", game.getTeam_2().getTitle());
                    gameValues.put("SCORE_ONE", game.getScore_1());
                    gameValues.put("SCORE_TWO", game.getScore_2());
                    gameValues.put("DAY", game.getDay());
                    gameValues.put("MONTH", game.getMonth());
                    gameValues.put("YEAR", game.getYear());
                    db.insert(title + "Games", null, gameValues);
                }
            }

            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void createTournament(Tournament tournament) {

    }

    public void createTeamTable(SQLiteDatabase sqLiteDatabase, String tournTitle) {

        //Создаем таблицу под названием турнира списком команд и их параметрами в ней
        sqLiteDatabase.execSQL("CREATE TABLE " + tournTitle + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TEAM TEXT, " +
                "POINTS INTEGER, " +
                "GAMES INTEGER, " +
                "GAMES_WON INTEGER, " +
                "GAME_LOST INTEGER);");

    }

    public void createGameTable(SQLiteDatabase sqLiteDatabase, String tournTitle) {
        //Создаем список игр
        sqLiteDatabase.execSQL("CREATE TABLE " + tournTitle + "Games" + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TEAM_ONE TEXT, " +
                "TEAM_TWO TEXT, " +
                "SCORE_ONE INTEGER, " +
                "SCORE_TWO INTEGER, " +
                "DAY INTEGER, " +
                "MONTH INTEGER, " +
                "YEAR INTEGER);");
    }

    @Override
    public List<Tournament> readDBTourn(StartFragment startFragment) {

        List<Tournament> items = new ArrayList<>();
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor newCursor = db.query("TOUR",
                    new String[]{"_id", "TITLE", "YEAR", "TYPE", "TEAM_IN_PLAYOFF", "LOOPS"},
                    null, null, null, null, null);
            if (newCursor.moveToFirst()) {

                do {
                    String title = newCursor.getString(1).replace("_", " ");
                    items.add(new Tournament(title, newCursor.getString(2), newCursor.getString(3)));

                } while (newCursor.moveToNext());
            } else {

                newCursor.close();
            }
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
        }

        return items;
    }

    @Override
    public void delTournament(String title, Boolean isPlayoff) {

        String tournTitle = title.replace(" ", "_");
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("TOUR", "TITLE = ?", new String[]{tournTitle});
            db.execSQL("DROP TABLE IF EXISTS " + title.replace(" ", "_"));
            db.execSQL("DROP TABLE IF EXISTS " + title.replace(" ", "_") + "GAMES");
            db.close();


        } catch (SQLiteException e) {
            Toast.makeText(context, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public Tournament getTournament(String title) {

        //Возвращаемый турнир
        Tournament tournament = null;
        //Параметры турнира
        List<Team> teamList = new ArrayList<>();
        List<Game> gameList = new ArrayList<>();

        //Получаем доступ к базе данных
        SQLiteDatabase db = this.getReadableDatabase();
        //Курсор для получения списка команд турнира
        Cursor teamCursor = db.query(title.replace(" ","_"),
                new String[]{"_id", "TEAM", "POINTS", "GAMES", "GAMES_WON", "GAME_LOST"},
                null, null, null, null, null);
        //Ситываем команды из курсра и добавляем их в teamList
        if (teamCursor.moveToFirst()) {
            do {
                Team team = new Team(teamCursor.getString(1).replace("_", " "));
                team.setPoints(teamCursor.getInt(2));
                team.setGames(teamCursor.getInt(3));
                team.setGames_won(teamCursor.getInt(4));
                team.setGames_lost(teamCursor.getInt(5));
                teamList.add(team);
            } while (teamCursor.moveToNext());
        }

        //Курсор для получения списка игр турнира
        Cursor gameCursor = db.query(title.replace(" ", "_") + "Games",
                new String[]{"_id", "TEAM_ONE", "TEAM_TWO", "SCORE_ONE",
                        "SCORE_TWO", "DAY", "MONTH", "YEAR"},
                null, null, null, null, null);

        //Ситываем игры из курсра и добавляем их в gameList
        if (gameCursor.moveToFirst()) {
            do {
                Team team_one = teamList.get(TournamentUtil.getTeam(teamList, gameCursor.getString(1).replace("_"," ")));
                Team team_two = teamList.get(TournamentUtil.getTeam(teamList, gameCursor.getString(2).replace("_"," ")));

                Game game = new Game(team_one, team_two,
                        gameCursor.getInt(3), gameCursor.getInt(4),
                        gameCursor.getInt(5), gameCursor.getInt(6),
                        gameCursor.getInt(7));
                gameList.add(game);
            } while (gameCursor.moveToNext());
        }

        //Курсор для получения остальных параметров турнира
        Cursor tournCursor = db.query("TOUR", new String[]{"_id", "YEAR", "TYPE", "TEAM_IN_PLAYOFF", "LOOPS", "IS_PLAYOFF"}, "TITLE = ?", new String[]{title}, null, null, null);
        //Считываем из курсора остальные парамеры турнира
        if (tournCursor.moveToFirst()) {
            do {
                Boolean isPlayoff = false;
                if (tournCursor.getInt(5) == 1) {
                    isPlayoff = true;
                }
                //Запускаем конструктор new Tournament
                tournament = new Tournament(title, tournCursor.getString(1), tournCursor.getString(2),
                        teamList, gameList, tournCursor.getInt(3), tournCursor.getInt(4), isPlayoff);
            } while (tournCursor.moveToNext());
        }

        //Закрываем все курсоры и доступ к базе данных
        teamCursor.close();
        gameCursor.close();
        tournCursor.close();
        db.close();

        return tournament;

    }


    public void createPlayoff(String playoffTitle, int countGames, int teamInPlayoff, List<Team> teamItems, List<Game> gameList) {

        try {
            //Добавляем параметры нового турнира в таблицу БД
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues playoffValues = new ContentValues();
            playoffValues.put("TITLE", playoffTitle);
            playoffValues.put("GAME_COUNT", countGames);
            playoffValues.put("TEAM_IN_PLAYOFF", teamInPlayoff);
            db.insert("PLAYOFF", null, playoffValues);

            createTeamTable(db, playoffTitle + "_playoff");
            createGameTable(db, playoffTitle + "_playoff");

            //Добавляем команды из списка в таблицу БД
            for (int i = 0; i < teamItems.size(); i++) {

                Team team = teamItems.get(i);
                ContentValues teamValues = new ContentValues();
                teamValues.put("TEAM", team.getTitle());
                teamValues.put("POINTS", team.getPoints());
                teamValues.put("GAMES", team.getGames());
                teamValues.put("GAMES_WON", team.getGames_won());
                teamValues.put("GAME_LOST", team.getGames_lost());
                db.insert(playoffTitle + "_playoff", null, teamValues);
            }
            if (gameList != null) {
                //Добавляем игры из списка в ьаблицу БД
                for (int i = 0; i < gameList.size(); i++) {

                    Game game = gameList.get(i);
                    ContentValues gameValues = new ContentValues();
                    gameValues.put("TEAM_ONE", game.getTeam_1().getTitle());
                    gameValues.put("TEAM_TWO", game.getTeam_2().getTitle());
                    gameValues.put("SCORE_ONE", game.getScore_1());
                    gameValues.put("SCORE_TWO", game.getScore_2());
                    gameValues.put("DAY", game.getDay());
                    gameValues.put("MONTH", game.getMonth());
                    gameValues.put("YEAR", game.getYear());
                    db.insert(playoffTitle + "_playoff" + "Games", null, gameValues);
                }
            }

            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
        }
    }

    public Playoff getPlayoff(String playoffTitle) {

        //Возвращаемый плей-офф
        Playoff playoff = null;
        //Параметры турнира
        List<Team> teamList = new ArrayList<>();

        List<Game> gameList = new ArrayList<>();

        //Получаем доступ к базе данных
        SQLiteDatabase db = this.getReadableDatabase();
        //Курсор для получения списка команд турнира
        Cursor teamCursor = db.query(playoffTitle + "_playoff",
                new String[]{"_id", "TEAM", "POINTS", "GAMES", "GAMES_WON", "GAME_LOST"},
                null, null, null, null, null);
        //Ситываем команды из курсра и добавляем их в teamList
        if (teamCursor.moveToFirst()) {
            do {
                Team team = new Team(teamCursor.getString(1));
                team.setPoints(teamCursor.getInt(2));
                team.setGames(teamCursor.getInt(3));
                team.setGames_won(teamCursor.getInt(4));
                team.setGames_lost(teamCursor.getInt(5));
                teamList.add(team);
            } while (teamCursor.moveToNext());
        }

        //Курсор для получения списка игр турнира
        Cursor gameCursor = db.query(playoffTitle + "_playoff"  + "Games",
                new String[]{"_id", "TEAM_ONE", "TEAM_TWO", "SCORE_ONE",
                        "SCORE_TWO", "DAY", "MONTH", "YEAR"},
                null, null, null, null, null);

        //Считываем игры из курсра и добавляем их в gameList
        if (gameCursor.moveToFirst()) {
            do {
                Team team_one = teamList.get(TournamentUtil.getTeam(teamList, gameCursor.getString(1)));
                Team team_two = teamList.get(TournamentUtil.getTeam(teamList, gameCursor.getString(2)));

                Game game = new Game(team_one, team_two,
                        gameCursor.getInt(3), gameCursor.getInt(4),
                        gameCursor.getInt(5), gameCursor.getInt(6),
                        gameCursor.getInt(7));
                gameList.add(game);
            } while (gameCursor.moveToNext());
        }

        //Курсор для получения остальных параметров playoff
        Cursor playoffCursor = db.query("PLAYOFF", new String[]{"_id", "GAME_COUNT", "TEAM_IN_PLAYOFF"},
                "TITLE = ?", new String[]{playoffTitle}, null, null, null);
        //Считываем из курсора остальные парамеры турнира
        if (playoffCursor.moveToFirst()) {
            do {

                //Запускаем конструктор new Tournament
                playoff = new Playoff(playoffTitle, playoffCursor.getInt(1),
                        playoffCursor.getInt(2), gameList, teamList, true);

            } while (playoffCursor.moveToNext());
        }

        //Закрываем все курсоры и доступ к базе данных
        teamCursor.close();
        gameCursor.close();
        playoffCursor.close();
        db.close();

        return playoff;
    }

    public void delPlayoff(String title) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("PLAYOFF", "TITLE = ?", new String[]{title});
            db.execSQL("DROP TABLE IF EXISTS " + title.replace(" ", "_") + "_playoff");
            db.execSQL("DROP TABLE IF EXISTS " + title.replace(" ", "_") + "_playoff" + "Games");
            db.close();

        } catch (SQLiteException e) {
            Toast.makeText(context, "Ошибка базы данных", Toast.LENGTH_SHORT).show();
        }
    }

}
