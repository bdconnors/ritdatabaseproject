package org.rit.footballapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.rit.footballapi.util.DBInterface;
import org.rit.footballapi.util.DLException;

import java.util.ArrayList;

public class AllLeagueRequests extends DBInterface {

    public ArrayList<LeagueRequest> allLeagueRequests = new ArrayList<>();
    @JsonIgnore
    public String leagueid;

    public AllLeagueRequests()
    {

    }
    public AllLeagueRequests(String leagueid)
    {
        this.leagueid = leagueid;
    }
    public void fetch()throws DLException
    {
        setQuery("allleaguerequests.sql");
        setBindValues(new ArrayList<String>(){{add(leagueid);}});
        super.fetch();
        LeagueRequest lr = null;
        ArrayList<String[]> requests = getResults();
        for(String[] request: requests)
        {
            lr = new LeagueRequest(request);
            allLeagueRequests.add(lr);
        }
    }

    public ArrayList<LeagueRequest> getAllLeagueRequests() {
        return allLeagueRequests;
    }

    public void setAllLeagueRequests(ArrayList<LeagueRequest> allLeagueRequests) {
        this.allLeagueRequests = allLeagueRequests;
    }
}
