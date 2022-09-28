package com.wm22.db;

import com.wm22.domain.Match;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface MatchesDao {
    @RegisterFieldMapper(Match.class)
    @SqlQuery("select * from matches")
    List<Match> getAllMatches();

    @RegisterFieldMapper(Match.class)
    @SqlUpdate("insert into matches (id, stage_id, first_team_id, second_team_id, date) values (:id, :stageId, :firstTeamId, :secondTeamId, :date)")
    int insertMatchWithTeams(@BindBean Match match);

    @RegisterFieldMapper(Match.class)
    @SqlUpdate("insert into matches (id, stage_id, date) values (:id, :stageId, :date)")
    int insertMatch(@BindBean Match match);

}
