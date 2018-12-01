package models;
import java.sql.*;
import java.util.ArrayList;
/** Object used to perform operations on MySQL football statistics database.
 * @author Brandon Connors
 * @author bdc5435@rit.edu
 * @version 2.0
 * @since 1.0
 */
public class FootballDatabase {

    private String uri;
    private String user;
    private String driver;
    private String password;
    private Connection conn = null;
    /** Creates a FootballDatabase with predefined information
     */
    public FootballDatabase() {
        uri = "jdbc:mariadb://localhost:3305/football";
        driver = "org.mariadb.jdbc:mariadb-java-client:2.3.0";
        user = "root";
        password = "student";
    }
    /** Creates a FootballDatabase database with user specified information
     * @param uri The connection string used to connect to database
     * @param driver The driver used to connect to database
     * @param user The user name used to connect to database
     * @param password The password used to connect to database
     */
    public FootballDatabase(String uri, String driver, String user, String password) {
        this.uri = uri;
        this.user = user;
        this.driver = driver;
        this.password = password;
    }
    /** Creates a connection to database
     * @throws Throws Data Layer Exception if connection unsuccessful
     * @return A boolean representing a successful or unsuccessful connection
     */
    public boolean connect() throws DLException {
        boolean status = true;
        try {
            if (conn == null || conn.getAutoCommit()) {
                conn = DriverManager.getConnection(uri, user, password);
                status = true;
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
            throw new DLException(e, "Could Not Connect to Server", "uri: " + uri, "User: " + user, "Driver: " + driver);
        }
        return status;
    }
    /** Closes the connection to database
     * @throws Throws Data Layer Exception if close is unsuccesful
     * @return A boolean representing successfull or unsuccessful close
     */
    public boolean close() throws DLException {
        boolean status = true;
        try {
            if (conn != null && conn.getAutoCommit()) {
                conn.close();
                status = true;
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
            throw new DLException(e, "Could Not Close Connection", "uri: " + uri, "User: " + user, "Driver: " + driver);
        }
        return status;
    }
    /**Retrieves data from database using a PreparedStatement
     * @throws Throws Data Layer Exception if data cannot be retrieved
     * @param query A String containing an SQL query statement
     * @param list An ArrayList of String containing query values
     * @return An ArrayList of String[] containing data requested
     */
    public ArrayList<String[]> getData(String query, ArrayList<String> list) throws DLException {
        ArrayList<String[]> data = new ArrayList<String[]>();
        connect();
        try {
            PreparedStatement stmnt = prepare(query, list);
            ResultSet rs = stmnt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            String[] colNameRow = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                colNameRow[i] = rsmd.getColumnName(i + 1);
            }
            data.add(colNameRow);
            while (rs.next()) {
                String[] valueRow = new String[colCount];
                for (int i = 1; i <= colCount; i++) {
                    valueRow[i - 1] = rs.getString(i);
                }
                data.add(valueRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return data;
    }
    /**Retrieves data from database
     * @throws Throws Data Layer Exception if data cannot be retrieved
     * @param query A String containing an SQL query statement
     * @return An ArrayList of String[] containing data requested
     */
    public ArrayList<String[]> getData(String query) throws DLException {
        connect();
        ArrayList<String[]> data = new ArrayList<String[]>();
        int numFields = 0;
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(query);
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                numFields = rsmd.getColumnCount();
                String[] row = new String[numFields];
                for (int i = 1; i <= numFields; i++) {
                    row[i - 1] = rs.getString(i);
                }
                data.add(row);
            }
            close();
        } catch (Exception e) {
            throw new DLException(e, "Could Not Retrieve Data", "query: " + query, "numFields: " + String.valueOf(numFields));
        }
        return data;
    }
    /**Sets data in database
     * @throws Throws Data Layer Exception if data cannot be set
     * @param update A String containing an SQL update statement
     * @param list An ArrayList of String containing update values
     * @return An int representing the number of rows effected
     */
    public int setData(String update, ArrayList<String> list) throws DLException {
        connect();
        int effected = 0;
        try {
            effected = executeStmnt(update, list);
        } catch (Exception e) {
            effected = -1;
            throw new DLException(e, "Could Not Set Data", "Query: " + update);
        }
        close();

        return effected;
    }
    /**Sets data in database
     * @throws Throws Data Layer Exception if data cannot be set
     * @param update A String containing an SQL update statement
     * @return An int representing the number of rows effected
     */
    public int setData(String update) throws DLException {
        connect();
        int effected = 0;
        try {
            Statement stmnt = conn.createStatement();
            effected = stmnt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
            effected = -1;
            throw new DLException(e, "Could Not Set Data", "Query: " + update);
        }
        return effected;
    }
    /**Creates a PreparedStatement from String and an ArrayList of values
     * @throws Throws Data Layer Exception if statement cannot be created
     * @param query A String containing an SQL statement
     * @return A PreparedStatement object
     */
    public PreparedStatement prepare(String query, ArrayList<String> list) throws DLException {
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            for (int i = 0; i < list.size(); i++) {
                String value = list.get(i);
                stmnt.setString(i + 1, value);
            }
            return stmnt;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, query, "Could Not Prepare Statement");
        }

    }
    /**Prepares a statement from an SQL update String and an ArrayList of values and executes it
     * @throws Throws Data Layer Exception if statement cannot be executed
     * @param update A String containing an SQL statement
     * @return An int representing the number of rows effected
     */
    public int executeStmnt(String update, ArrayList<String> list) throws DLException {
        PreparedStatement stmnt = null;
        int effected = 0;
        try {
            connect();
            stmnt = prepare(update, list);
            effected = stmnt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, update, "Could Not Execute Statement");
        }
        return effected;
    }
    /**Prints to console a table containing requested data
     * @throws Throws Data Layer Exception if statement cannot be created
     * @param query A String containing an SQL query statement
     */
    public void descTable(String query) throws DLException {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(query);
            ArrayList<String[]> data = getData(query);
            TablePrinter print = new TablePrinter(rs, data);
            print.printData();
            print.printTypes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, "Could Not print meta data");
        }
    }
    /**Retrieves data on players of the specified position from an API and loads it into database
     * @throws Throws Data Layer Exception if player data could not be loaded
     * @param pos A String containing a specified player position (Ex.'WR','RB','TE','QB','K')
     */
    public void loadPlayersByPos(String pos) throws DLException {
        try {
            startTrans();
            MySportsFeeds feed = new MySportsFeeds();
            ArrayList<String[]> players = feed.getPlayersByPosition(pos);
            for (int i = 0; i < players.size(); i++) {
                String[] curPlayer = players.get(i);
                Player player = new Player(curPlayer);
                player.post();
            }
            endTrans();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, "could not load player data");
        }
    }
    /**Retrieves data on all 32 NFL teams from an API and loads it into database
     * @throws Throws Data Layer Exception if team data could not be loaded
     */
    public void loadAllTeams() throws DLException {
        try {
            startTrans();
            MySportsFeeds feed = new MySportsFeeds();
            String[] curTeam = new String[2];
            ArrayList<String[]> teams = feed.getAllTeams();
            for (int i = 0; i < teams.size(); i++) {
                curTeam = teams.get(i);
                Team team = new Team(curTeam);
                team.post();
            }
            endTrans();
        } catch (DLException e) {
            e.printStackTrace();
            throw new DLException(e, "cannot load teams");
        }
    }
    /**Retrieves data on all games played by a specified NFL team and loads them into database
     * @throws Throws Data Layer Exception if game data could not be loaded
     * @param team A String containing an NFL team 2 or 3 letter abbreviation (ex. 'NYG' is 'New York Giants')
     */
    public void loadGamesByTeam(String team) throws DLException {
        try {
            startTrans();
            MySportsFeeds feed = new MySportsFeeds();
            String[] curGame = new String[3];
            String gameCheck = null;
            ArrayList<String[]> games = feed.getGamesByTeam(team);
            for (int i = 0; i < games.size(); i++) {
                curGame = games.get(i);
                gameCheck = "select * from game where gameid = '" + curGame[0] + "';";
                if (existsInDB(gameCheck) == true) {

                } else {
                    Game game = new Game(curGame);
                    game.post();
                }
            }
            endTrans();
        } catch (DLException e) {
            e.printStackTrace();
            throw new DLException(e, "could not load team data");
        }
    }
    /**Retrieves player game statistics from all players of a specified position
     * on a specified team and loads them into database
     * @throws Throws Data Layer Exception if player statistics could not be loaded
     * @param team A String containing an NFL team 2 or 3 letter abbreviation (ex. 'NYG' is 'New York Giants')
     * @param pos A String containing a specified player position (Ex.'WR','RB','TE','QB','K')
     */
    public void loadPlayerStats(String team, String pos) throws DLException {
        try {
            startTrans();
            MySportsFeeds feed = new MySportsFeeds();
            ArrayList<int[]> stats = feed.getStatsByTeamPos(team, pos);
            int[] curStats = new int[22];
            DefenseStats dfs = new DefenseStats();

            for (int i = 0; i < stats.size(); i++) {
                curStats = stats.get(i);
                PlayerStats pstats = new PlayerStats(curStats);
                pstats.post();
            }
            endTrans();
        } catch (DLException e) {
            e.printStackTrace();
            throw new DLException(e, "could not load team data");
        }
    }
    /**Retrieves Defensive game statistics of a specified NFL team and loads them into database
     * @throws Throws Data Layer Exception if Defensive statistics could not be loaded
     * @param team A String containing an NFL team 2 or 3 letter abbreviation (ex. 'NYG' is 'New York Giants')
     */
    public void loadDefStats(String team) throws DLException {
        try {
            startTrans();
            MySportsFeeds feed = new MySportsFeeds();
            ArrayList<int[]> stats = feed.getDefStatsByTeam(team);
            int[] curStats = new int[12];
            DefenseStats dfs = new DefenseStats();

            for (int i = 0; i < stats.size(); i++) {
                curStats = stats.get(i);
                DefenseStats dstats = new DefenseStats(curStats);
                dstats.setTeam(team);
                dstats.post();
            }
            endTrans();
        } catch (DLException e) {
            e.printStackTrace();
            throw new DLException(e, "could not load team data");
        }
    }
    /**Querys the database to check if a record exists
     * @throws Throws Data Layer Exception if check cannot be excuted
     * @param query A String containing an SQL query statement
     * @return A boolean indicating if the record exists
     */
    public boolean existsInDB(String query) throws DLException {
        boolean exists = false;

        try {
            ArrayList<String[]> data = getData(query);
            if (data.isEmpty()) {
                exists = false;
            } else {
                exists = true;
            }
        } catch (DLException e) {
            e.printStackTrace();
            throw new DLException(e, "could not check if exists");
        }
        return exists;
    }
    /**Begins a transaction, setting auto commit to false
     * @throws Throws Data Layer Exception if transaction cannot be started
     */
    public void startTrans() throws DLException {
        try {
            connect();
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, "Could not start transaction");
        }
    }
    /**Ends a transaction, setting auto commit to true
     * @throws Throws Data Layer Exception if transaction cannot end
     */
    public void endTrans() throws DLException {
        try {
            conn.commit();
            conn.setAutoCommit(true);
            close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, "Could not end transaction");
        }
    }
    /**Rolls a transaction back
     * @throws Throws Data Layer Exception if transaction cannot be rolled back
     */
    public void rollbackTrans() throws DLException {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DLException(e, "Could not rollback transaction");
        }
    }
}//end FootballDatabase