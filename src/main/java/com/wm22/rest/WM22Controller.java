package com.wm22.rest;

import com.wm22.db.UsersDao;
import com.wm22.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class WM22Controller {

    @Autowired
    UsersDao usersDao;

    @GetMapping(path = "/users")
    public User getAllSeasonMatches() {
        return usersDao.getUsers();
    }

}
