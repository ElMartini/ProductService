package com.example.productService.database.mysql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Getter
@Setter
public class ActionStatus {
    @Id
    String aid;
    String status;
    String query;
    LocalDateTime lastChange;

    @Override
    public String toString() {
        return "ActionStatus{" +
                "aid='" + aid + '\'' +
                ", status='" + status + '\'' +
                ", query='" + query + '\'' +
                ", lastChange=" + lastChange +
                '}';
    }
}

