package com.wm22.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wm22.db.MatchesDao;
import com.wm22.db.PredictionsDao;
import com.wm22.db.TeamsDao;
import com.wm22.db.UsersDao;
import com.wm22.domain.Match;
import com.wm22.domain.Team;
import com.wm22.domain.User;
import com.wm22.model.Root;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class WM22Controller {

    MatchesDao matchesDao;
    PredictionsDao predictionsDao;
    UsersDao usersDao;
    TeamsDao teamsDao;

    Team teamToInsert;
    ArrayList<Team> teamsToInsert = new ArrayList<>();
    Match matchToInsert;
    ArrayList<Match> matchesToInsert;

    public WM22Controller(MatchesDao matchesDao,
                          PredictionsDao predictionsDao,
                          UsersDao usersDao,
                          TeamsDao teamsDao) {
        this.matchesDao = matchesDao;
        this.predictionsDao = predictionsDao;
        this.usersDao = usersDao;
        this.teamsDao = teamsDao;
    }

    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    @GetMapping(path = "/teams")
    public List<Team> getAllTeams() {
        return teamsDao.getAllTeams();
    }

    @GetMapping(path = "/matches")
    public List<Match> getAllMatches() {
        return matchesDao.getAllMatches();
    }

    @PostConstruct
    public void getDataFromApi() throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();

        Root root;
        root = objectMapper.readValue(new File("src/main/resources/world_cup_matches.json"), Root.class);

        insertDataInDB(root);
    }

    public void insertDataInDB(Root root) {
        matchesToInsert = new ArrayList<>();

        //Teams Tabelle ausfüllen
        root.matches.forEach(match -> {
            if (Objects.equals(match.stage, "GROUP_STAGE")) {
                teamToInsert = new Team();
                teamToInsert.setId(match.homeTeam.id);
                teamToInsert.setName(match.homeTeam.name);
                teamToInsert.setShortName(match.homeTeam.tla);
                teamToInsert.setFlag(match.homeTeam.crest);
                teamToInsert.setGroupName(match.group.charAt(6));
                teamsToInsert.add(teamToInsert);
            }
        });

        teamsToInsert.forEach(team -> {
            if (teamsDao.getTeamById(team.getId()) == null) {
                teamsDao.insertTeam(team);
            }
        });

        //Matches Tabelle ausfüllen
        root.matches.forEach(match -> {
            if (Objects.equals(match.stage, "GROUP_STAGE")) {
                matchToInsert = new Match();
                matchToInsert.setId(match.id);
                matchToInsert.setStageId(1);
                matchToInsert.setFirstTeamId(match.homeTeam.id);
                matchToInsert.setSecondTeamId(match.awayTeam.id);
                matchToInsert.setDate(match.utcDate);
                matchesDao.insertMatch(matchToInsert);
            }
        });

    }

}
