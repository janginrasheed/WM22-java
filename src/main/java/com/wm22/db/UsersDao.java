package com.wm22.db;

import com.wm22.domain.User;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface UsersDao {
    @RegisterFieldMapper(User.class)
    @SqlQuery("select * from users")
    List<User> getAllUsers();

    @RegisterFieldMapper(User.class)
    @SqlUpdate("insert into users values (:email, :firstName, :lastName, :password)")
    int register(@BindBean User user);

    @RegisterFieldMapper(User.class)
    @SqlQuery("select * from users where email = :email")
    User getUserByEmail(@Bind("email") String email);

}
