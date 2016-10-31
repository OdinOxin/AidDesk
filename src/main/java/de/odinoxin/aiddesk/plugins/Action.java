package de.odinoxin.aiddesk.plugins;

import java.sql.SQLException;

public interface Action<T> {
    public abstract T run() throws SQLException;
}