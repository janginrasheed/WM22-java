package com.wm22.db;

import com.wm22.domain.Team;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface TeamsDao {
    @RegisterFieldMapper(Team.class)
    @SqlQuery("select * from teams")
    List<Team> getAllTeams();

    @RegisterFieldMapper(Team.class)
    @SqlUpdate("insert into teams values(:id, :name, :shortName, :flag, :groupName);")
    void insertTeam(@BindBean Team team);

    @RegisterFieldMapper(Team.class)
    @SqlQuery("select * from teams where id = :teamId")
    Team getTeamById(@Bind("teamId") int teamId);

    @RegisterFieldMapper(Team.class)
    @SqlQuery("select * from teams where UPPER(group_name) = :groupName")
    List<Team> getTeamByGroupName(@Bind("groupName") char groupName);

}
