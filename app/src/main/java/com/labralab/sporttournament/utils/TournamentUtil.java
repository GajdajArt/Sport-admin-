package com.labralab.sporttournament.utils;

import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Playoff;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.models.simple_models.SimplePlayoff;
import com.labralab.sporttournament.models.simple_models.SimpleTournament;
import com.labralab.sporttournament.models.sort_item.TeamSortItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 01.10.2017.
 */

public class TournamentUtil {

    public static int getTeam(List<Team> teamList, String title) {

        for (int i = 0; i < teamList.size(); i++) {
            String title_1 = teamList.get(i).getTitle();

            if (title_1.equals(title)) {
                return i;
            }
        }
        return -1;
    }

    public static int getGame(List<Game> gameList, String teamOne, String teamTwo) {

        for (int i = 0; i <= gameList.size(); i++) {
            if (gameList.get(i).getTeam_1().getTitle().equals(teamOne) && gameList.get(i).getTeam_2().getTitle().equals(teamTwo)) {
                return i;
            }

            if (gameList.get(i).getTeam_2().getTitle().equals(teamOne) && gameList.get(i).getTeam_1().getTitle().equals(teamTwo)) {
                return i;
            }
        }
        return -1;
    }

    public static int chackGame(List<Game> gameList, String teamOne, String teamTwo) {

        int i = 0;
        for (Game game : gameList) {
            if (game.getTeam_1().getTitle().equals(teamOne) && game.getTeam_2().getTitle().equals(teamTwo)) {
                i++;
            }

            if (game.getTeam_2().getTitle().equals(teamOne) && game.getTeam_1().getTitle().equals(teamTwo)) {
                i++;
            }
        }

        return i;
    }

    public static List<Team> getWinnerTeams(List<Team> teamList) {

        List<Team> winnerTeams = new ArrayList<>();

        for (Team team : teamList) {
            if (team.getGames() > 0 && team.getGames_lost() == 0) {
                winnerTeams.add(team);
            }
        }
        return winnerTeams;
    }

    public static Tournament simpleTournToHard(SimpleTournament oldTourn) {

        Tournament newTourn = new Tournament();
        newTourn.setTitle(oldTourn.getTitle());
        newTourn.setType(oldTourn.getType());
        newTourn.setLoops(oldTourn.getLoops());
        newTourn.setMaxCountGame(oldTourn.getMaxCountGame());
        newTourn.setTeamInPlayoff(oldTourn.getTeamInPlayoff());
        newTourn.setTeamsCount(oldTourn.getTeamsCount());
        newTourn.setYearOfTourn(oldTourn.getYearOfTourn());
        newTourn.setIsPlayoffFlag(oldTourn.getIsPlayoffFlag());

        List<Team> teams = new ArrayList<>();
        List<Game> games = new ArrayList<>();
        teams.addAll(oldTourn.getTeamList());
        games.addAll(oldTourn.getGameList());

        newTourn.setTeamList(teams);
        newTourn.setGameList(games);

        if (oldTourn.getPlayoff() != null) {
            newTourn.setPlayoff(toPlayoff(oldTourn.getPlayoff()));
        }
        return newTourn;
    }

    public static SimpleTournament tournToSimpleTourn(Tournament oldTourn) {

        SimpleTournament newTourn = new SimpleTournament();
        newTourn.setTitle(oldTourn.getTitle());
        newTourn.setType(oldTourn.getType());
        newTourn.setLoops(oldTourn.getLoops());
        newTourn.setMaxCountGame(oldTourn.getMaxCountGame());
        newTourn.setTeamInPlayoff(oldTourn.getTeamInPlayoff());
        newTourn.setTeamsCount(oldTourn.getTeamsCount());
        newTourn.setYearOfTourn(oldTourn.getYearOfTourn());
        newTourn.setIsPlayoffFlag(oldTourn.getIsPlayoffFlag());

        List<Team> teams = new ArrayList<>();
        List<Game> games = new ArrayList<>();
        teams.addAll(oldTourn.getTeamList());
        games.addAll(oldTourn.getGameList());

        newTourn.setTeamList(teams);
        newTourn.setGameList(games);

        if (oldTourn.getPlayoff() != null) {
            newTourn.setPlayoff(toSimplePlayoff(oldTourn.getPlayoff()));
        }

        return newTourn;
    }


    private static SimplePlayoff toSimplePlayoff(Playoff oldPlayoff) {

        SimplePlayoff newPlayoff = new SimplePlayoff();
        newPlayoff.setPlayoffTitle(oldPlayoff.getPlayoffTitle());
        newPlayoff.setCountGames(oldPlayoff.getCountGames());
        newPlayoff.setTeamInPlayoff(oldPlayoff.getTeamInPlayoff());
        newPlayoff.setTeamsSort(oldPlayoff.getIsTeamsSort());

        List<Game> playoffGameList = new ArrayList<>();
        playoffGameList.addAll(oldPlayoff.getPlayoffGameList());
        List<Team> playoffTeamList = new ArrayList<>();
        playoffTeamList.addAll(oldPlayoff.getPlayoffTeamList());
        List<Game> playoffFirstTurGames = new ArrayList<>();
        playoffFirstTurGames.addAll(oldPlayoff.getPlayoffFirstTurGames());
        List<Game> playoffSecondTurGames = new ArrayList<>();
        playoffSecondTurGames.addAll(oldPlayoff.getPlayoffSecondTurGames());
        List<Game> playoffLastTurGames = new ArrayList<>();
        playoffLastTurGames.addAll(oldPlayoff.getPlayoffLastTurGames());

        newPlayoff.setPlayoffGameList(playoffGameList);
        newPlayoff.setPlayoffTeamList(playoffTeamList);
        newPlayoff.setPlayoffFirstTurGames(playoffFirstTurGames);
        newPlayoff.setPlayoffSecondTurGames(playoffSecondTurGames);
        newPlayoff.setPlayoffLastTurGames(playoffLastTurGames);

        return newPlayoff;
    }

    private static Playoff toPlayoff (SimplePlayoff oldPlayoff){

        Playoff newPlayoff = new Playoff();
        newPlayoff.setPlayoffTitle(oldPlayoff.getPlayoffTitle());
        newPlayoff.setCountGames(oldPlayoff.getCountGames());
        newPlayoff.setTeamInPlayoff(oldPlayoff.getTeamInPlayoff());
        newPlayoff.setTeamsSort(oldPlayoff.getIsTeamsSort());

        List<Game> playoffGameList = new ArrayList<>();
        playoffGameList.addAll(oldPlayoff.getPlayoffGameList());
        List<Team> playoffTeamList = new ArrayList<>();
        playoffTeamList.addAll(oldPlayoff.getPlayoffTeamList());
        List<Game> playoffFirstTurGames = new ArrayList<>();
        playoffFirstTurGames.addAll(oldPlayoff.getPlayoffFirstTurGames());
        List<Game> playoffSecondTurGames = new ArrayList<>();
        playoffSecondTurGames.addAll(oldPlayoff.getPlayoffSecondTurGames());
        List<Game> playoffLastTurGames = new ArrayList<>();
        playoffLastTurGames.addAll(oldPlayoff.getPlayoffLastTurGames());

        newPlayoff.setPlayoffGameList(playoffGameList);
        newPlayoff.setPlayoffTeamList(playoffTeamList);
        newPlayoff.setPlayoffFirstTurGames(playoffFirstTurGames);
        newPlayoff.setPlayoffSecondTurGames(playoffSecondTurGames);
        newPlayoff.setPlayoffLastTurGames(playoffLastTurGames);


        return newPlayoff;
    }
    public static TeamSortItem hardTeamToSort(Team oldTeam){

        TeamSortItem newTeam = new TeamSortItem();
        newTeam.setTitle(oldTeam.getTitle());
        newTeam.setId(TeamSortItem.TEAM_ID);

        return newTeam;
    }

    public static Team simpleTeamToHard(TeamSortItem oldTeam, List<Team> teams){
        return teams.get(getTeam(teams, oldTeam.getTitle()));
    }

}
