package de.cweyermann.ssps.backend.logic;

import de.cweyermann.ssps.backend.ChrisBot;
import de.cweyermann.ssps.backend.repo.Match;
import de.cweyermann.ssps.backend.repo.Participant;
import de.cweyermann.ssps.backend.repo.Repository;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CallMatch {

    private static final Logger LOG = LogManager.getLogger(CallMatch.class);

    private static List<String> ALLOWED = Arrays.asList(
            "schere",
            "stein",
            "papier",
            "echse",
            "spock"
    );

    private static List<String> WINNING_COMB = Arrays.asList(
            "schere.papier",
            "papier.stein",
            "stein.echse",
            "echse.spock",
            "spock.schere",
            "schere.echse",
            "echse.papier",
            "papier.spock",
            "spock.stein",
            "stein.schere"
    );

    private Repository repo;

    public CallMatch(Repository repository) {
        repo = repository;
    }

    public void letsPlay() {
        List<Participant> participants = repo.getAllParticipants();
        Collections.shuffle(participants);

        if (participants.size() < 2) {
            // skip this round
            return;
        }

        Participant team1 = participants.get(0);
        Participant team2 = participants.get(1);

        String hintForTeam1 = buildHint(team2);
        String team1Choice = call(team1, hintForTeam1);

        String hintForTeam2 = buildHint(team1);
        String team2Choice = call(team2, hintForTeam2);

        boolean didTeam1PlayFair = ALLOWED.contains(team1Choice);
        boolean didTeam2PlayFair = ALLOWED.contains(team2Choice);
        boolean didTeam1Win = WINNING_COMB.contains(team1Choice + "." + team2Choice);
        boolean didTeam2Win = WINNING_COMB.contains(team2Choice + "." + team1Choice);

        String winner;
        if (!didTeam1PlayFair && !didTeam2PlayFair) {
            winner = "";
        } else if (!didTeam1PlayFair) {
            winner = team2.getName();
        } else if (!didTeam2PlayFair) {
            winner = team1.getName();
        } else if (didTeam1Win) {
            winner = team1.getName();
        } else if (didTeam2Win) {
            winner = team2.getName();
        } else {
            winner = "";
        }

        Match match = new Match();
        match.setId(UUID.randomUUID().toString());
        match.setDate(new Date());
        match.setTeam1name(team1.getName());
        match.setTeam1choice(team1Choice);
        match.setTeam2name(team2.getName());
        match.setTeam2choice(team2Choice);
        match.setWinnerName(winner);

        repo.save(match);
        LOG.info("Saved to DB: " + match);

        team1.setLastChoice(team1Choice);
        repo.saveParticipant(team1);
        LOG.info("Saved to DB: " + team1);

        team2.setLastChoice(team2Choice);
        repo.saveParticipant(team2);
        LOG.info("Saved to DB: " + team2);
    }

    private String buildHint(Participant opponent) {
        String lastChoice = encode(opponent.getLastChoice());
        String name = encode(opponent.getName());


        return "?opponent=" + name + "&lastChoice=" + lastChoice;
    }

    private String encode(String lastChoice) {
        String urlLc = "unknown";
        try{
            if (lastChoice == null) {
                lastChoice = "";
            }
            urlLc = URLEncoder.encode(lastChoice, "UTF-8");
        } catch(Exception e) {
            LOG.warn("cannot decode: " + lastChoice);
        }
        return urlLc;
    }

    private String call(Participant team, String hints) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(team.getUrl() + hints);

            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    LOG.info(team.getName() + ": " + result);
                    String lowerCase = result.toLowerCase();
                    String replaced = lowerCase.replaceAll("[^a-z]", "");
                    LOG.info("saving \": " + replaced);
                    return replaced;
                } else {
                    LOG.error(team.getName() + " returned nothing!");
                    return null;
                }
            }
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }
    }
}
