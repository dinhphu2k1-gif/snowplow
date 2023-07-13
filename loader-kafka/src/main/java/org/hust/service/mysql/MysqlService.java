package org.hust.service.mysql;

import org.hust.storage.mysql.MysqlConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class MysqlService {
    private final Connection connection;
    private final static Logger LOGGER = LoggerFactory.getLogger(MysqlService.class);

    public MysqlService() {
        connection = MysqlConnection.getConnection();
    }

    public void close() {
        MysqlConnection.close();
    }

    public boolean checkExistMapping(int user_id, String domain_userid) {
        String sql = "SELECT * FROM cdp_mapping WHERE domain_userid = ? and user_id = ?";

        boolean exist = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, domain_userid);
            statement.setInt(2, user_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                exist = true;
            }

            statement.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return exist;
    }

    public void insertMapping(int user_id, String domain_userid) {
        String sql = "INSERT INTO cdp_mapping (domain_userid, user_id, create_at) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, domain_userid);
            statement.setInt(2, user_id);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis() + 7 * 3600 * 1000));

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public int getUserId(String domain_userid) {
        String sql = "SELECT user_id FROM cdp_mapping WHERE domain_userid = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, domain_userid);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }

            statement.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return -1;
    }


    public static void main(String[] args) {
        MysqlService mysqlService = new MysqlService();

//        System.out.println(mysqlService.checkExistMapping(1, "123"));
//        mysqlService.insertMapping(1, "1234");
        System.out.println(mysqlService.getUserId("1234"));
        mysqlService.close();
    }
}
