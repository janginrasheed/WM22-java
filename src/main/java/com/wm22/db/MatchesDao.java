package com.wm22.db;

import com.wm22.model.api1.Match;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface MatchesDao {
    @RegisterFieldMapper(Match.class)
    @SqlQuery("select * from matches")
    List<Match> getAllMatches();
}
