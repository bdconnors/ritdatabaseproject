package org.rit.footballapi.models;

import org.rit.footballapi.util.DBInterface;
import org.rit.footballapi.util.DLException;

import java.util.ArrayList;

public class PlayerGameRush extends DBInterface {
    private String gameid;
    private String playerid;
    private int rushAtt;
    private int rushYds;
    private int rushTd;
    private int rush2pt;


    public PlayerGameRush(int[] stats) {
        gameid = String.valueOf(stats[0]);
        playerid = String.valueOf(stats[1]);
        rushAtt = stats[2];
        rushYds = stats[3];
        rushTd  = stats[4];
        rush2pt  = stats[5];

    }
    public PlayerGameRush(String gameid,String playerid)
    {
        this.gameid = gameid;
        this.playerid = playerid;
    }
    public void fetch() throws DLException {
        //SQL query string
        String query = "SELECT rushatt,rushyds,rushtd,rush2pt FROM playergamerush WHERE gameid=? AND playerid= ?;";

        ArrayList<String> values = new ArrayList<>();
        try {
            //returns a ArrayList<String[]> filled with info that corresponds to the query statement and number of fields
            ArrayList<String[]> info = getData(query, values);
            String[] fields = info.get(1);
            //set name to the first field value
            rushAtt = Integer.parseInt(fields[0]);
            rushYds = Integer.parseInt(fields[1]);
            rush2pt = Integer.parseInt(fields[2]);

        } catch (Exception e) {
            System.out.println("No Record Found");

        }

    }
    public int post() throws DLException {

        //effected records
        int effected;
        ArrayList<String> values = new ArrayList<>();
        //SQL Insert String
        String insert = "INSERT INTO playergamerush(gameid,playerid,rushatt,rushyds,rushtd,rush2pt)VALUES(?,?,?,?,?,?);";
        //bind values
        values.add(gameid);
        values.add(playerid);
        values.add(String.valueOf(rushAtt));
        values.add(String.valueOf(rushYds));
        values.add(String.valueOf(rushTd));
        values.add(String.valueOf(rush2pt));

        try {
            //perform insert and return number of effected
            effected = setData(insert, values);

        } catch (DLException e) {
            effected = -1;
            e.printStackTrace();

        }

        return effected;

    }
    public int put() throws DLException {  //effected records
        int effected;
        //SQL Update String
        String update = "UPDATE playergamerush SET rushatt=?,rushyds=?,rushtd=?,rush2pt=? WHERE gameid = ? AND playerid=?;";
        ArrayList<String> values = new ArrayList<>();

        values.add(String.valueOf(rushAtt));
        values.add(String.valueOf(rushYds));
        values.add(String.valueOf(rushTd));
        values.add(String.valueOf(rush2pt));
        values.add(gameid);
        values.add(playerid);

        try {
            //perform update and return number of effected records
            effected = setData(update, values);
        } catch (DLException e) {
            effected = -1;
        }

        return effected;
    }
    public int delete() throws DLException {

        //effected records
        int effected;
        //SQL delete string
        String delete = "DELETE FROM playergamerush WHERE gameid =? AND playerid=?;";
        ArrayList<String> values = new ArrayList<>();
        values.add(gameid);
        values.add(playerid);
        try {

            //perform delete and return number of effected records
            effected = setData(delete, values);

        } catch (DLException e) {
            effected = -1;
            e.printStackTrace();

        }

        return effected;
    }
    @Override
    public String toString() {
        return "PlayerGameRush{" +
                "gameid='" + gameid + '\'' +
                ", playerid='" + playerid + '\'' +
                ", rushAtt=" + rushAtt +
                ", rushYds=" + rushYds +
                ", rushTd=" + rushTd +
                ", rush2pt=" + rush2pt +
                '}';
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public int getRushAtt() {
        return rushAtt;
    }

    public void setRushAtt(int rushAtt) {
        this.rushAtt = rushAtt;
    }

    public int getRushYds() {
        return rushYds;
    }

    public void setRushYds(int rushYds) {
        this.rushYds = rushYds;
    }

    public int getRushTd() {
        return rushTd;
    }

    public void setRushTd(int rushTd) {
        this.rushTd = rushTd;
    }

    public int getRush2pt() {
        return rush2pt;
    }

    public void setRush2pt(int rush2pt) {
        this.rush2pt = rush2pt;
    }
}