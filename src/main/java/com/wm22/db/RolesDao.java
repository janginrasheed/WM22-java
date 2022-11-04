package com.wm22.db;

import com.wm22.domain.Role;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

/**
 * In diesem Interface sind die Methoden die, die SQL Abfragen in der Roles-Tabelle durchführen
 */
public interface RolesDao {
    @RegisterFieldMapper(Role.class)
    @SqlQuery("select * from roles")
    List<Role> getRoles();

}
