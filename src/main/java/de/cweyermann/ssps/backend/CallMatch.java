package de.cweyermann.ssps.backend;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import de.cweyermann.sspes.api.SayHello;
import de.cweyermann.ssps.backend.repo.Match;
import de.cweyermann.ssps.backend.repo.Repository;

import java.util.Map;

public class CallMatch implements RequestHandler<Map<String, Object>, Void> {

    @Override
    public Void handleRequest(Map<String, Object> stringObjectMap, Context context) {
        de.cweyermann.ssps.backend.logic.CallMatch callMatch = new de.cweyermann.ssps.backend.logic.CallMatch(new Repository());

        System.out.println("Starting CallMatch");
        for (int i = 0; i < 5; i++) {
            callMatch.letsPlay();
        }

        return null;
    }

    public static void main(String[] args) {
        new CallMatch().handleRequest(null, null);
    }
}
