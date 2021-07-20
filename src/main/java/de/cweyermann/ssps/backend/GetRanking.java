package de.cweyermann.ssps.backend;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cweyermann.ssps.backend.dtos.BffResult;
import de.cweyermann.ssps.backend.logic.CalcRanking;
import de.cweyermann.ssps.backend.logic.GetAllMatches;
import de.cweyermann.ssps.backend.repo.Match;
import de.cweyermann.ssps.backend.repo.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetRanking implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(GetRanking.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: {}", input);

		Repository repo = new Repository();
		BffResult result = new BffResult();
		result.matches = new GetAllMatches(repo).allFromDb();
		result.ranking = new CalcRanking(repo).fromMatches(result.matches);


		Map<String, String> headers = new HashMap<>();
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Credentials", "true");
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(result)
				.setHeaders(headers)
				.build();
	}

	public static void main(String[] args) {
		new GetRanking().handleRequest(null, null);
	}
}
