package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Beinhaltet die Attribute die für das Strukturieren von der Predictions-Tabelle genutzt wird
 */
@Data
@NoArgsConstructor
public class Prediction {
    private int id;
    private String email;
    private int firstTeamId;
    private int secondTeamId;
    private char groupName;
    private int matchNumber;
    private String predict_date;
}
