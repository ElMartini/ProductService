package com.example.productService.database.mysql.service;

import com.example.productService.database.mysql.model.ActionStatus;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository

public interface ActionStatusService {

    List<ActionStatus> getActionStatus() throws SQLException;
    int addActionStatus(ActionStatus actionStatus) throws SQLException, ClassNotFoundException;
    int updateActionStatus(ActionStatus actionStatus, String aid) throws SQLException;
}
