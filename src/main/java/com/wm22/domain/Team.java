package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Beinhaltet die Attribute die für das Strukturieren von der Teams-Tabelle genutzt wird
 */
@Data
@NoArgsConstructor
public class Team {
    private int id;
    private String name;
    private String shortName;
    private String flag;
    private char groupName;
}
