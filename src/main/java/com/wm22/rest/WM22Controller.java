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
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostConstruct
    public void getDataFromApi() throws IOException {
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
            matchToInsert = new Match();
            if (Objects.equals(match.stage, "GROUP_STAGE")) {
                matchToInsert.setId(match.id);
                matchToInsert.setStageId(1);
                matchToInsert.setFirstTeamId(match.homeTeam.id);
                matchToInsert.setSecondTeamId(match.awayTeam.id);
                matchToInsert.setDate(match.utcDate);
                matchesDao.insertMatchWithTeams(matchToInsert);
            } else {
                matchToInsert.setId(match.id);
                switch (match.stage) {
                    case "LAST_16": {
                        matchToInsert.setStageId(2);
                        break;
                    }
                    case "QUARTER_FINALS": {
                        matchToInsert.setStageId(3);
                        break;
                    }
                    case "SEMI_FINALS": {
                        matchToInsert.setStageId(4);
                        break;
                    }
                    case "THIRD_PLACE": {
                        matchToInsert.setStageId(5);
                        break;
                    }
                    case "FINAL": {
                        matchToInsert.setStageId(6);
                        break;
                    }
                }
                matchToInsert.setDate(match.utcDate);
                matchesDao.insertMatch(matchToInsert);
            }

        });

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

    @GetMapping(path = "/userbyemail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return usersDao.getUserByEmail(email);
    }

    @GetMapping(path = "/user/{email}/{password}")
    public User getUser(@PathVariable String email, @PathVariable String password) {
        if (usersDao.getUser(email, password) == null)
            return null;

        if (BCrypt.checkpw(password, usersDao.getUser(email, password).getPassword())) {
            System.out.println("It matches");
            return usersDao.getUser(email, password);
        } else {
            System.out.println("It dous not matches");
            return null;
        }

    }

    @PostMapping(path = "register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        if (usersDao.register(user) == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
