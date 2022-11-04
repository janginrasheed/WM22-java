package com.wm22.db;

import com.wm22.domain.Match;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * In diesem Interface sind die Methoden die, die SQL Abfragen in der Matches-Tabelle durchführen
 */
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

    @RegisterFieldMapper(Match.class)
    @SqlUpdate("update matches set first_team_goals = :firstTeamGoals, second_team_goals = :secondTeamGoals where id = :id")
    int updateMatchResult(@BindBean int matchid, @BindBean Match match);

    @RegisterFieldMapper(Match.class)
    @SqlUpdate("update matches set first_team_id = :firstTeamId, second_team_id = :secondTeamId where id = :id")
    int updateMatchTeams(@BindBean int matchid, @BindBean Match match);

    @RegisterFieldMapper(Match.class)
    @SqlUpdate("update matches set first_team_id = 0, second_team_id = 0 where id > 48")
    int clearKOStagesTeams();

}
