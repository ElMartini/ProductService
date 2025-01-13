package com.example.productService.database.mysql.service;

import com.example.productService.database.mysql.DBUtil.DBUtil;
import com.example.productService.database.mysql.model.ActionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;

@Slf4j
@Service
public class ActionStatusServiceImpl implements ActionStatusService {




    @Autowired
    static List<ActionStatus> actionStatusList = new ArrayList<>();

    Connection connection;

    public ActionStatusServiceImpl() throws SQLException, ClassNotFoundException {
        connection = DBUtil.getConnection();
    }

    @Override
    public List<ActionStatus> getActionStatus() throws SQLException {
        actionStatusList.clear();
        String statementQuery = "SELECT * FROM actionStatus";
        PreparedStatement statement = connection.prepareStatement(statementQuery);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            ActionStatus actionStatus = new ActionStatus();
            actionStatus.setAid(rs.getString(1));
            actionStatus.setStatus(rs.getString(2));
            actionStatus.setQuery(rs.getString(3));
            actionStatus.setLastChange(rs.getTimestamp(4).toLocalDateTime());

            actionStatusList.add(actionStatus);
        }
        return actionStatusList;
    }


    @Override
    public int addActionStatus(ActionStatus actionStatus) throws SQLException, ClassNotFoundException {
        String aid = UUID.randomUUID().toString();
        String status = actionStatus.getStatus();
        String query = actionStatus.getQuery();

        String statementQuery = "INSERT INTO actionStatus (aid, status, query, lastChange) VALUES (?, ?, ?,?)";
        connection = DBUtil.getConnection();

        actionStatusList = getActionStatus();
        boolean actionUpdate = false;
        ActionStatus lastAction = null;
        if (!actionStatusList.isEmpty()) {
            lastAction = actionStatusList.get(actionStatusList.size() - 1);
            if (lastAction.getQuery().equals(query)) {
                LocalDateTime lastActionDate = lastAction.getLastChange();
                LocalDateTime now = LocalDateTime.now();
                Duration difference = Duration.between(lastActionDate, now);
                if (abs(difference.getSeconds()) <= 3) {
                    actionUpdate = true;
                }
            }
        }
        if (actionUpdate) {
            return updateActionStatus(actionStatus, lastAction.getAid());
        } else {
            return createAddStatement(statementQuery, aid, status, query).executeUpdate();
        }

    }

    @Override
    public int updateActionStatus(ActionStatus actionStatus, String aid) throws SQLException {
        String status = actionStatus.getStatus();
        String query = actionStatus.getQuery();


        String statementQuery = "UPDATE actionStatus SET " +
                "aid ='" + aid + "'," +
                "status ='" + status + "'," +
                "query ='" + query + "'," +
                "lastChange ='" + Timestamp.valueOf(LocalDateTime.now()) + "'" +
                "where aid='" + aid + "'";


        PreparedStatement statement = connection.prepareStatement(statementQuery);
        int i = statement.executeUpdate();
        log.info("Action status updated");
        return i;
    }

    PreparedStatement createAddStatement(String statementQuery, String aid, String status, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(statementQuery);

        statement.setString(1, aid);
        statement.setString(2, status);
        statement.setString(3, query);
        statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        return statement;
    }

}
