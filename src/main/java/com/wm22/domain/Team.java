package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Team {
    private int id;
    private String name;
    private String shortName;
    private String flag;
    private char groupName;
}
