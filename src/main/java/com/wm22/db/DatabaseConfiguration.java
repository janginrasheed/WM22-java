package com.wm22.db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring4.JdbiFactoryBean;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public JdbiFactoryBean jdbiFactoryBean(DataSource dataSource) {
        JdbiFactoryBean jdbiFactoryBean = new JdbiFactoryBean(dataSource);

        // sqlobject-Plugin registrieren
        jdbiFactoryBean.setPlugins(Collections.singletonList(new SqlObjectPlugin()));

        return jdbiFactoryBean;
    }

    @Bean
    public GroupsTeamsDao groupsTeamsDao(Jdbi jdbi) {
        return jdbi.onDemand(GroupsTeamsDao.class);
    }

    @Bean
    public MatchesDao matchesDao(Jdbi jdbi) {
        return jdbi.onDemand(MatchesDao.class);
    }

    @Bean
    public PredictionsDao predictionsDao(Jdbi jdbi) {
        return jdbi.onDemand(PredictionsDao.class);
    }

    @Bean
    public UsersDao usersDao(Jdbi jdbi) {
        return jdbi.onDemand(UsersDao.class);
    }

}
