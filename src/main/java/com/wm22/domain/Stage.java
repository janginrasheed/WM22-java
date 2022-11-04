package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Beinhaltet die Attribute die für das Strukturieren von der Stages-Tabelle genutzt wird
 */
@Data
@NoArgsConstructor
public class Stage {
    private int id;
    private String stage;
}
