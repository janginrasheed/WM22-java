package com.wm22.db;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface PredictionsDao {
    @RegisterFieldMapper(PredictionsDao.class)
    @SqlQuery("select * from predictions")
    List<PredictionsDao> getAllPredictions();
}
