package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Match {
    private int id;
    private int stageId;
    private int firstTeamId;
    private int firstTeamGoals;
    private int firstTeamPenaltiesGoals;
    private int secondTeamId;
    private int secondTeamGoals;
    private int secondTeamPenaltiesGoals;
    private Date date;
}
