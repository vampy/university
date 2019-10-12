package project.controller;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import javax.sql.DataSource;


public class DB extends JdbcDaoSupport
{
    private static DB instance;
    private static DataSource ds;

    public static void setDS(DataSource dataSource)
    {
        DB.ds = dataSource;
    }

    public static DB get()
    {
        if (instance == null)
        {
            System.out.println("DB::get Init Singleton");
            instance = new DB();
            instance.setDataSource(ds);
        }

        return instance;
    }
}
