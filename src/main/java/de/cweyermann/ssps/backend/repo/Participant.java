package de.cweyermann.ssps.backend.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "SSPS_Participants")
public class Participant {

    @DynamoDBHashKey
    private String name;

    private String url;

    private String lastChoice;

    private String fullname;

}
