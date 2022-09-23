package com.wm22.db;

import com.wm22.model.GroupTeam;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface GroupsTeamsDao {
    @RegisterFieldMapper(GroupTeam.class)
    @SqlQuery("select * from groups_teams")
    List<GroupTeam> getAllGroupsTeams();
}
