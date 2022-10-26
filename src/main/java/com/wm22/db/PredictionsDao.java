package com.wm22.db;

import com.wm22.domain.Prediction;
import org.hibernate.annotations.SQLDelete;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PredictionsDao {
    @RegisterFieldMapper(Prediction.class)
    @SqlQuery("select * from predictions")
    List<Prediction> getAllPredictions();

    @RegisterFieldMapper(Prediction.class)
    @SqlQuery("select * from predictions where email = :email order by match_number, group_name")
    List<Prediction> getPredictionsByEmail(@Bind("email") String email);

    @RegisterFieldMapper(Prediction.class)
    @SqlUpdate("insert into predictions (email, first_team_id, second_team_id, match_number, group_name, date) values (:email, :firstTeamId, :secondTeamId, :matchNumber, :groupName, current_timestamp)")
    int setPrediction(@BindBean Prediction prediction);

    @RegisterFieldMapper(Prediction.class)
    @SqlQuery("delete from predictions where email like :email")
    int deletePredictions(@Bind("email") String email);
}
