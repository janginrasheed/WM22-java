package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Beinhaltet die Attribute die für das Strukturieren von der Roles-Tabelle genutzt wird
 */
@Data
@NoArgsConstructor
public class Role {
    private int id;
    private String role;
}
