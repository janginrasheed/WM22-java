package com.wm22;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Wm22ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void checkMatchResult() {
//		@Autowired
//		MatchesDao matchesDao = null;
//		Match match = new Match();

        String firstTeamGoals = "1.5";
        String secondTeamGoals = "0";


        assertTrue(Integer.parseInt(firstTeamGoals) > -1);
        assertTrue(Integer.parseInt(secondTeamGoals) > -1);

        assertTrue(Integer.parseInt(firstTeamGoals) <= 99);
        assertTrue(Integer.parseInt(secondTeamGoals) <= 99);
    }

}
