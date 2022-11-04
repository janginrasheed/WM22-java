package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Beinhaltet die Attribute die für das Strukturieren von der Matches-Tabelle genutzt wird
 */
@Data
@NoArgsConstructor
public class Match {
    private int id;
    private int stageId;
    private int firstTeamId;
    private String firstTeamGoals;
    private int firstTeamPenaltiesGoals;
    private int secondTeamId;
    private String secondTeamGoals;
    private int secondTeamPenaltiesGoals;
    private Date date;
}
