package models;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class MySportsFeeds {

    private String feedString = "https://api.mysportsfeeds.com/v1.0/pull/nfl/2018-regular/";
    private String playerString = "roster_players.json?rosterstatus=assigned-to-roster";
    private String positionString = "position=";
    private String pNameString = "player=";
    private String teamString = "overall_team_standings.json";
    private String teamGameString = "team_gamelogs.json?team=";
    private String playerTeamGameString = "player_gamelogs.json?team=";

    public String apiRequest(String reqString) {

        String jsonString = null;
        try {
            String encode = "b4b138b1-909e-4ffa-80ee-baccda:iste330";
            URL url = new URL(reqString);
            String encoding = Base64.getEncoder().encodeToString(encode.getBytes());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter, true);
            String line;
            while ((line = in.readLine()) != null) {
                writer.write(line);
            }
            jsonString = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public ArrayList<String[]> getPlayersByPosition(String pos) {

        ArrayList<String[]> playerData = new ArrayList<String[]>();
        String reqString = feedString + playerString + "&" + positionString + pos;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = apiRequest(reqString);
            JsonNode root = mapper.readTree(jsonString);
            JsonNode players = root.findPath("playerentry");
            for (int i = 0; i < players.size(); i++) {
                JsonNode curPlayer = players.get(i);
                if (curPlayer.findPath("JerseyNumber").isMissingNode()) {

                } else {
                    String[] player = new String[6];
                    player[0] = curPlayer.findPath("ID").asText();
                    player[1] = curPlayer.findPath("FirstName").asText();
                    player[2] = curPlayer.findPath("LastName").asText();
                    player[3] = curPlayer.findPath("Abbreviation").asText();
                    player[4] = curPlayer.findPath("Position").asText();
                    player[5] = curPlayer.findPath("JerseyNumber").asText();
                    playerData.add(player);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerData;
    }

    public ArrayList<String[]> getAllTeams() {

        ArrayList<String[]> teamData = new ArrayList<String[]>();
        String reqString = feedString + teamString;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = apiRequest(reqString);
            JsonNode root = mapper.readTree(jsonString);
            JsonNode teams = root.findPath("teamstandingsentry");
            for (int i = 0; i < teams.size(); i++) {
                JsonNode curTeam = teams.get(i);
                String[] team = new String[2];
                team[0] = curTeam.findPath("City").asText() + " " + curTeam.findPath("Name").asText();
                team[1] = curTeam.findPath("Abbreviation").asText();
                teamData.add(team);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamData;
    }

    public ArrayList<String[]> getGamesByTeam(String team) {

        ArrayList<String[]> gameData = new ArrayList<String[]>();
        String reqString = feedString + teamGameString + team;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = apiRequest(reqString);
            JsonNode root = mapper.readTree(jsonString);
            JsonNode games = root.findPath("gamelogs");
            for (int i = 0; i < games.size(); i++) {
                String[] game = new String[3];
                JsonNode curGame = games.get(i);
                game[0] = curGame.findPath("game").findPath("id").asText();
                game[1] = curGame.findPath("game").findPath("date").asText();
                game[2] = curGame.findPath("game").findPath("time").asText();
                gameData.add(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameData;
    }

    public ArrayList<int[]> getDefStatsByTeam(String team) {

        ArrayList<int[]> defStats = new ArrayList<int[]>();
        String reqString = feedString + teamGameString + team;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = apiRequest(reqString);
            JsonNode root = mapper.readTree(jsonString);
            JsonNode games = root.findPath("gamelogs");
            for (int i = 0; i < games.size(); i++) {
                int[] stats = new int[12];
                JsonNode curGame = games.get(i);
                stats[0] = curGame.findPath("id").asInt();
                stats[1] = curGame.findPath("PointsAgainst").findPath("#text").asInt();
                stats[2] = curGame.findPath("Sacks").findPath("#text").asInt();
                stats[3] = curGame.findPath("Safeties").findPath("#text").asInt();
                stats[4] = curGame.findPath("IntTD").findPath("#text").asInt();
                stats[5] = curGame.findPath("FumTD").findPath("#text").asInt();
                stats[6] = curGame.findPath("KrTD").findPath("#text").asInt();
                stats[7] = curGame.findPath("PrTD").findPath("#text").asInt();
                stats[8] = curGame.findPath("Interceptions").findPath("#text").asInt();
                stats[9] = curGame.findPath("Fumbles").findPath("#text").asInt();
                stats[10] = curGame.findPath("KB").findPath("#text").asInt();
                stats[11] = curGame.findPath("XpBlk").findPath("#text").asInt();
                defStats.add(stats);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defStats;
    }

    public ArrayList<int[]> getStatsByTeamPos(String team, String pos) {

        ArrayList<int[]> pStats = new ArrayList<int[]>();
        String reqString = feedString + playerTeamGameString + team + "&" + positionString + pos;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = apiRequest(reqString);
            JsonNode root = mapper.readTree(jsonString);
            JsonNode games = root.findPath("gamelogs");

            for (int i = 0; i < games.size(); i++) {
                JsonNode gamelog = games.get(i);
                int[] stats = new int[22];
                stats[0] = gamelog.findPath("game").findPath("id").asInt();
                stats[1] = gamelog.findPath("player").findPath("ID").asInt();
                stats[2] = gamelog.findPath("stats").findPath("PassAttempts").findPath("#text").asInt();
                stats[3] = gamelog.findPath("stats").findPath("PassCompletions").findPath("#text").asInt();
                stats[4] = gamelog.findPath("stats").findPath("PassYards").findPath("#text").asInt();
                stats[5] = gamelog.findPath("stats").findPath("PassTD").findPath("#text").asInt();
                stats[6] = gamelog.findPath("stats").findPath("TwoPtPassMade").findPath("#text").asInt();
                stats[7] = gamelog.findPath("stats").findPath("RushAttempts").findPath("#text").asInt();
                stats[8] = gamelog.findPath("stats").findPath("RushYards").findPath("#text").asInt();
                stats[9] = gamelog.findPath("stats").findPath("TwoPtRushMade").findPath("#text").asInt();
                stats[10] = gamelog.findPath("stats").findPath("Receptions").findPath("#text").asInt();
                stats[11] = gamelog.findPath("stats").findPath("RecYards").findPath("#text").asInt();
                stats[12] = gamelog.findPath("stats").findPath("RecTD").findPath("#text").asInt();
                stats[13] = gamelog.findPath("stats").findPath("TwoPtPassRec").findPath("#text").asInt();
                stats[14] = gamelog.findPath("stats").findPath("FgAtt").findPath("#text").asInt();
                stats[15] = gamelog.findPath("stats").findPath("FgMade").findPath("#text").asInt();
                stats[16] = gamelog.findPath("stats").findPath("XpAtt").findPath("#text").asInt();
                stats[17] = gamelog.findPath("stats").findPath("XpMade").findPath("#text").asInt();
                stats[18] = gamelog.findPath("stats").findPath("PassInt").findPath("#text").asInt();
                stats[19] = gamelog.findPath("stats").findPath("Fumbles").findPath("#text").asInt();
                stats[20] = gamelog.findPath("stats").findPath("KrTD").findPath("#text").asInt();
                stats[21] = gamelog.findPath("stats").findPath("PrTD").findPath("#text").asInt();
                pStats.add(stats);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pStats;
    }

}//end of MySportsFeeds