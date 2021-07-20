package de.cweyermann.ssps.backend.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class Repository {

    public List<Match> getAll() {
        PaginatedScanList<Match> scan = DynamoDb.mapper.scan(Match.class, new DynamoDBScanExpression());
        return new ArrayList<>(scan);
    }

    public List<Participant> getAllParticipants() {
        PaginatedScanList<Participant> scan = DynamoDb.mapper.scan(Participant.class, new DynamoDBScanExpression());
        return new ArrayList<>(scan);
    }

    public void save(Match match) {
        DynamoDb.mapper.save(match);
    }

    public void saveParticipant(Participant participant) {
        DynamoDb.mapper.save(participant);
    }

}