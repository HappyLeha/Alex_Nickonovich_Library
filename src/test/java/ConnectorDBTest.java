import org.junit.Assert;
import org.junit.Test;
import util.ConnectorDB;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectorDBTest {
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = ConnectorDB.getConnection();
        Assert.assertNotNull(connection);
    }
}
