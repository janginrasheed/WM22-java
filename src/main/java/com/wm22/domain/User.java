package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Beinhaltet die Attribute die für das Strukturieren von der Users-Tabelle genutzt wird
 */
@Data
@NoArgsConstructor
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private int roleId;
}
