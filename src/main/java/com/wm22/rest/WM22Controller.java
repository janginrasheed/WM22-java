package com.wm22.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wm22.db.*;
import com.wm22.domain.*;
import com.wm22.model.Root;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class WM22Controller {
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
    int matchId = 0;

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

    /**
     * Beim Aufruf von dieser Methode wird ein Insert in DB durchgeführt der ein User als Admin anlegt
     */
    @GetMapping(path = "/setNewAdmin")
    public void setAdmin() {
        User user = new User();
        user.setEmail("jangin9rasheed@gmail.com");
        user.setFirstName("Jangin");
        user.setLastName("Rasheed");
        user.setPassword("123456");
        user.setRoleId(1);
        register(user);
    }

    /**
     * Diese Methode holt die Daten von API und speichert die in eine lokale JSON Datei.
     * @throws IOException
     */
    //@PostConstruct
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

    /**
     * Diese Methode speichert die Daten von API in DB
     * @param root die Daten die vom API gekommen.
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
            matchId += 1;
            matchToInsert = new Match();
            if (Objects.equals(match.stage, "GROUP_STAGE")) {
                matchToInsert.setId(matchId);
                matchToInsert.setStageId(1);
                matchToInsert.setFirstTeamId(match.homeTeam.id);
                matchToInsert.setSecondTeamId(match.awayTeam.id);
                matchToInsert.setDate(match.utcDate);
                matchesDao.insertMatchWithTeams(matchToInsert);
            } else {
                matchToInsert.setId(matchId);
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

    /**
     * Diese Methode gibt alle Phasen aus DB zurueck
     * @return Liste alle Phasen
     */
    @GetMapping(path = "/stages")
    public List<Stage> getStages() {
        return stagesDao.getStages();
    }

    /**
     * Diese Methode gibt alle Rollen aus DB zurueck
     * @return Liste alle Rollen
     */
    @GetMapping(path = "/roles")
    public List<Role> getRoles() {
        return rolesDao.getRoles();
    }

    /**
     * Diese Methode gibt alle Users aus DB zurueck
     * @return Liste alle Users
     */
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    /**
     * Diese Methode gibt alle Vereine aus DB zurueck
     * @return Liste alle Vereine
     */
    @GetMapping(path = "/teams")
    public List<Team> getAllTeams() {
        return teamsDao.getAllTeams();
    }

    /**
     * Diese Methode gibt alle Spiele aus DB zurueck
     * @return Liste alle Spiele
     */
    @GetMapping(path = "/matches")
    public List<Match> getAllMatches() {
        return matchesDao.getAllMatches();
    }

    /**
     * Diese Methode aktualisiert ein Spielergebnis
     * @param matchId
     * @param match
     * @return
     */
    @PutMapping(path = "/matches/updateMatchResult/{matchId}")
    public ResponseEntity<Void> updateMatchResult(@PathVariable int matchId, @RequestBody Match match) {
        if (matchesDao.updateMatchResult(matchId, match) == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Diese Methode aktualisiert die IDs von Spielvereine
     * @param matchId
     * @param match
     * @return
     */
    @PutMapping(path = "/matches/updateMatchTeams/{matchId}")
    public ResponseEntity<Void> updateMatchTeams(@PathVariable int matchId, @RequestBody Match match) {
        if (matchesDao.updateMatchTeams(matchId, match) == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Diese Methode löscht alle Vereine IDs von K.o.-Phasenspiele, wird fürs Zurücksetzen der Spiele benötigt
     * @param dummy
     */
    @PutMapping(path = "/matches/clearKOStagesTeams/{dummy}")
    public void clearKOStagesTeams(@PathVariable int dummy) {
        matchesDao.clearKOStagesTeams();
    }

    /**
     * Diese Methode gibt Userdaten zurück
     * @param email
     * @return Userdaten
     */
    @GetMapping(path = "/userByEmail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return usersDao.getUserByEmail(email);
    }

    /**
     * Diese Methode prueft ob ein User-Anmeldedaten zustimmen
     * @param email
     * @param password
     * @return Userdaten
     */
    @GetMapping(path = "/user/{email}/{password}")
    public User getUser(@PathVariable String email, @PathVariable String password) {
        //Pruefen ob der User vorhanden ist
        if (usersDao.getUserByEmail(email) == null)
            return null;

        //Eingegebenes Passwort mit dem verschluesselten Passwort in DB vergleichen
        if (BCrypt.checkpw(password, usersDao.getUserByEmail(email).getPassword())) {
            return usersDao.getUserByEmail(email);
        } else {
            return null;
        }

    }

    /**
     * Diese Methode fügt ein User in DB hinzu
     * @param user
     * @return
     */
    @PostMapping(path = "register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        //User-Passwort wird verschluesselt
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        if (usersDao.register(user) == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Diese Methode gibt Gruppendaten zurueck
     * @return Liste der Gruppen mit den Vereinen
     */
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

    /**
     * Diese Methode gibt alle Vorhersagen zurueck
     * @return Liste alle Vorhersagen
     */
    @GetMapping(path = "/predictions")
    public List<Prediction> getPredictions() {
        return predictionsDao.getAllPredictions();
    }

    /**
     * Diese Methode gibt alle Vorhersagen von einem User by Email
     * @param email
     * @return Liste alle Vorhersagen
     */
    @GetMapping(path = "/predictions/{email}")
    public List<Prediction> predictionsByEmail(@PathVariable String email) {
        return predictionsDao.getPredictionsByEmail(email);
    }

    /**
     * Diese Methode fügt die Vorhersagen von einem User in DB hinzu
     * @param predictions
     */
    @PostMapping(path = "submitPredictions")
    public void submitPredictions(@RequestBody Prediction[] predictions) {
        for (Prediction prediction : predictions) {
            predictionsDao.setPrediction(prediction);
        }
    }

    /**
     * Diese Methode löscht alle Vorhersagen von einem User
     * @param email
     */
    @DeleteMapping(path = "deletePredictions/{email}")
    public void deletePredictions(@PathVariable String email) {
        predictionsDao.deletePredictions(email);
    }

}
