package com.wm22.db;

import com.wm22.domain.Stage;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

/**
 * In diesem Interface sind die Methoden die, die SQL Abfragen in der Stages-Tabelle durchführen
 */
public interface StagesDao {
    @RegisterFieldMapper(Stage.class)
    @SqlQuery("select * from stages")
    List<Stage> getStages();

}
