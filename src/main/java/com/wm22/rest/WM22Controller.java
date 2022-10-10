package com.wm22.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wm22.db.*;
import com.wm22.domain.*;
import com.wm22.model.Root;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class WM22Controller {

    MatchesDao matchesDao;
    PredictionsDao predictionsDao;
    StagesDao stagesDao;
    RolesDao rolesDao;
    UsersDao usersDao;
    TeamsDao teamsDao;

    Team teamToInsert;
    ArrayList<Team> teamsToInsert = new ArrayList<>();
    Match matchToInsert;
    ArrayList<Match> matchesToInsert;
    char[] groups = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    public WM22Controller(MatchesDao matchesDao,
                          PredictionsDao predictionsDao,
                          StagesDao stagesDao,
                          RolesDao rolesDao,
                          UsersDao usersDao,
                          TeamsDao teamsDao) {
        this.matchesDao = matchesDao;
        this.predictionsDao = predictionsDao;
        this.stagesDao = stagesDao;
        this.rolesDao = rolesDao;
        this.usersDao = usersDao;
        this.teamsDao = teamsDao;
    }
/*
    @PostConstruct
    public void getDataFromApi() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Daten lokal abholen - funktioniert mit Azure nicht
        // Root root = objectMapper.readValue(new File("src/main/resources/world_cup_matches.json"), Root.class);

        // Daten mit InputStream lokal abholen - funktioniert mit Azure
        InputStream inputStream = getClass().getResourceAsStream("/world_cup_matches.json");
        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        Root root = objectMapper.readValue(contents, Root.class);

        insertDataInDB(root);
    }
*/
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

    @GetMapping(path = "/stages")
    public List<Stage> getStages() {
        return stagesDao.getStages();
    }

    @GetMapping(path = "/roles")
    public List<Role> getRoles() {
        return rolesDao.getRoles();
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

    @GetMapping(path = "/userByEmail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return usersDao.getUserByEmail(email);
    }

    @GetMapping(path = "/user/{email}/{password}")
    public User getUser(@PathVariable String email, @PathVariable String password) {
        if (usersDao.getUserByEmail(email) == null)
            return null;

        if (BCrypt.checkpw(password, usersDao.getUserByEmail(email).getPassword())) {
            return usersDao.getUserByEmail(email);
        } else {
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

    @GetMapping(path = "/teamsGroups")
    public List<GroupDetails> getGroupsDetails() {
        List<GroupDetails> groupsDetails = new ArrayList<>();

        for (char group : groups) {
            List<Team> groupTeams = teamsDao.getTeamByGroupName(group);
            GroupDetails groupDetails = new GroupDetails();
            groupDetails.setGroupTeams(groupTeams);
            groupsDetails.add(groupDetails);
        }

        return groupsDetails;
    }
}
