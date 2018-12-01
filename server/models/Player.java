package models;

import java.util.ArrayList;

public class Player {

    public String id;
    public String fName;
    public String lName;
    public String team;
    public String position;
    public String jNumber;
    public FootballDatabase db = new FootballDatabase();

    public Player(String[] player) {
        id = player[0];
        fName = player[1];
        lName = player[2];
        team = player[3];
        position = player[4];
        jNumber = player[5];
    }

    public Player(String id) {
        this.id = id;

    }

    public Player() {

    }

    public void fetch() throws DLException {
        //SQL query string
        String query = "SELECT FirstName,LastName,Team,Pos,JerseyNumber FROM player WHERE PlayerID=?;";

        ArrayList<String> values = new ArrayList<String>();
        values.add(id);

        try {

            ArrayList<String[]> info = db.getData(query, values);
            String[] fields = info.get(1);
            fName = fields[0];
            lName = fields[1];
            team = fields[2];
            position = fields[3];
            jNumber = fields[4];
        } catch (Exception e) {
            System.out.println("No Record Found");

        }

    }

    public int post() throws DLException {

        int effected = 0;
        ArrayList<String> values = new ArrayList<String>();
        String insert = "INSERT INTO player(PlayerID,FirstName,LastName,Team,Pos,JerseyNumber)VALUES(?,?,?,?,?,?);";
        values.add(id);
        values.add(fName);
        values.add(lName);
        values.add(team);
        values.add(position);
        values.add(jNumber);

        try {
            effected = db.setData(insert, values);
        } catch (DLException e) {
            effected = -1;
            e.printStackTrace();
        }

        return effected;
    }

    public int put() throws DLException {

        int effected = 0;
        String update = "UPDATE player SET FirstName= ? , LastName = ? , Team = ?, Position = ?,JerseyNumber= ? WHERE PlayerID = ?;";
        ArrayList<String> values = new ArrayList<String>();

        values.add(fName);
        values.add(lName);
        values.add(team);
        values.add(position);
        values.add(jNumber);
        values.add(id);

        try {
            //perform update and return number of effected records
            effected = db.setData(update, values);
        } catch (DLException e) {
            effected = -1;
        }

        return effected;
    }

    //---------------------------------------------------------------------------------------------
    //Method Name: delete
    //Description:deletes player record from database using their id
    //---------------------------------------------------------------------------------------------
    public int delete() throws DLException {

        //effected records
        int effected = 0;
        //SQL delete string
        String delete = "DELETE FROM player WHERE PlayerID=?;";
        ArrayList<String> values = new ArrayList<String>();
        values.add(id);


        try {

            //perform delete and return number of effected records
            effected = db.setData(delete, values);

        } catch (DLException e) {
            effected = -1;
            e.printStackTrace();

        }

        return effected;
    }

    //toString
    public String toString() {
        return "PlayerID: " + getID() + "\n" + "First Name: " + getFName() + "\n" + "Last Name: "
                + getLName() + "\n" + "Team: " + getTeam() + "\n" + "Position: "
                + getPosition() + "\n" + "Jersey Number: " + getJNumber();

    }

    //getters
    public String getID() {
        return id;
    }

    //setters
    public void setID(String id) {
        this.id = id;

    }

    public String getFName() {
        return fName;

    }

    public void setFName(String fName) {
        this.fName = fName;

    }

    public String getLName() {
        return lName;

    }

    public void setLName(String lName) {
        this.lName = lName;

    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;

    }

    public String getPosition() {
        return position;

    }

    public void setPosition(String position) {
        this.position = position;

    }

    public String getJNumber() {
        return jNumber;

    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;

    }

}//end player class