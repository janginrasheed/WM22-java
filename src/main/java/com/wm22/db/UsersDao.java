package com.wm22.db;

import com.wm22.model.User;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface UsersDao {
    @RegisterFieldMapper(User.class)
    @SqlQuery("select * from users")
    List<User> getAllUsers();
}
