package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GroupDetails {
    private List<Team> groupTeams = new ArrayList<>();
}
