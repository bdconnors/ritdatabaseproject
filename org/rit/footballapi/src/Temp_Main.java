import main.java.datalayer.ui.stats.game.QuarterBackGameStats;

public class Temp_Main {

    public static void main(String[] args)throws Exception
    {
        QuarterBackGameStats qbgs = new QuarterBackGameStats();
        qbgs.setPlayerid("6294");
        qbgs.setGameid("45961");
        qbgs.fetch();
    }
}
