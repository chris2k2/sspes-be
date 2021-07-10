package de.cweyermann.ssps.backend.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.util.Date;

@DynamoDBTable(tableName = "SSPS_Matches")
@Data
public class Match {

    @DynamoDBHashKey
    private String id;

    private Date date;

    private String team1name;
    private String team1choice;

    private String team2name;
    private String team2choice;

    private String winnerName;
}
