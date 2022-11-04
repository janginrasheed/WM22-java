package com.wm22.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Struktur für die Gruppendaten, wird für die Ausgabe der Gruppendaten genutzt
 */
@Data
@NoArgsConstructor
public class GroupDetails {
    private List<Team> groupTeams = new ArrayList<>();
}
