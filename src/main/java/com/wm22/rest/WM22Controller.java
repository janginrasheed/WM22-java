package com.wm22.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wm22.db.GroupsTeamsDao;
import com.wm22.db.MatchesDao;
import com.wm22.db.PredictionsDao;
import com.wm22.db.UsersDao;
import com.wm22.domain.Match;
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

    GroupsTeamsDao groupsTeamsDao;
    MatchesDao matchesDao;
    PredictionsDao predictionsDao;
    UsersDao usersDao;
    ArrayList<Match> matchesToInsert;

    public WM22Controller(GroupsTeamsDao groupsTeamsDao,
                          MatchesDao matchesDao,
                          PredictionsDao predictionsDao,
                          UsersDao usersDao) {
        this.groupsTeamsDao = groupsTeamsDao;
        this.matchesDao = matchesDao;
        this.predictionsDao = predictionsDao;
        this.usersDao = usersDao;
    }

    @GetMapping(path = "/users")
    public List<User> getAllSeasonMatches() {
        return usersDao.getAllUsers();
    }

    @PostConstruct
    public void getDataFromApi() throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();

        Root root;
        root = objectMapper.readValue(new File("src/main/resources/world_cup_matches.json"), Root.class);

/* ****** Direkt von API **********
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.football-data.org/v4/competitions/2001/matches?season=2021"))
                .header("X-Auth-Token", "bf68569154884d98b8a336180242686d")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
*/
    }

    public void insertDataInDB(Root root) {
//        matchesToInsert = new ArrayList<>();

        //Teams Tabelle ausfüllen



    }

}
