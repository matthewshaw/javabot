package javabot.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.antwerkz.maven.SPI;
import javabot.Javabot;

@SPI(UpgradeScript.class)
public class RenamePrefixes extends UpgradeScript {
    @Override
    public int id() {
        return 1;
    }

    @Override
    public void doUpgrade(final Javabot bot, final Connection connection) throws SQLException, IOException {
        final ResultSet rs = connection.getMetaData().getColumns(null, null, "configuration", "prefixes");
        while(rs.next()) {
            final Statement statement = connection.createStatement();
            try {
                statement.execute("update configuration set trigger=prefixes");
                statement.execute("alter table configuration drop prefixes");
            } finally {
                statement.close();
            }
        }

    }
}
