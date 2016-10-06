package de.odinoxin.aiddesk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database
{
    public static Connection DB;

    static
    {
        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Database.DB = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AidDesk", "", "");
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }
}
