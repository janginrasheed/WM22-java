package com.wm22.rest;

import com.wm22.db.GroupsTeamsDao;
import com.wm22.db.MatchesDao;
import com.wm22.db.PredictionsDao;
import com.wm22.db.UsersDao;
import com.wm22.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class WM22Controller {

    GroupsTeamsDao groupsTeamsDao;
    MatchesDao matchesDao;
    PredictionsDao predictionsDao;
    UsersDao usersDao;

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

}
