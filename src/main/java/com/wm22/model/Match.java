package com.wm22.model;

import java.util.ArrayList;
import java.util.Date;

public class Match {
    public Area area;
    public Competition competition;
    public Season season;
    public int id;
    public Date utcDate;
    public String status;
    public int matchday;
    public String stage;
    public String group;
    public Date lastUpdated;
    public HomeTeam homeTeam;
    public AwayTeam awayTeam;
    public Score score;
    public Odds odds;
    public ArrayList<Object> referees;
}
