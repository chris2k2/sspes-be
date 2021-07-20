package de.cweyermann.ssps.backend;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import de.cweyermann.ssps.backend.dtos.BffResult;
import de.cweyermann.ssps.backend.logic.CalcRanking;
import de.cweyermann.ssps.backend.logic.GetAllMatches;
import de.cweyermann.ssps.backend.repo.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ChrisBot implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        List<String> list = Arrays.asList("Schere", "Stein", "Papier", "Echse", "Spock");
        Collections.shuffle(list);


        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(list.get(3))
                .build();
    }
}
