package de.cweyermann.ssps.backend.logic;

import de.cweyermann.ssps.backend.dtos.BffResult;
import de.cweyermann.ssps.backend.repo.Participant;
import de.cweyermann.ssps.backend.repo.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class CalcRanking {

    private final List<Participant> allParticipants;

    public CalcRanking(Repository repo) {
        allParticipants = repo.getAllParticipants();
    }

    public List<BffResult.Ranking> fromMatches(List<BffResult.Match> matches) {
        Map<String, BffResult.Ranking> name2Ranking = new HashMap<>();

        for (BffResult.Match match : matches) {
            String team1 = match.getTeam1name();
            String team2 = match.getTeam2name();
            initTeam(name2Ranking, team1);
            initTeam(name2Ranking, team2);

            if (team1.equals(match.getWinnerName())) {
                name2Ranking.get(team1).setWon(name2Ranking.get(team1).getWon() + 1);
                name2Ranking.get(team2).setLost(name2Ranking.get(team2).getLost() + 1);
            } else if (team2.equals(match.getWinnerName())) {
                name2Ranking.get(team1).setLost(name2Ranking.get(team1).getLost() + 1);
                name2Ranking.get(team2).setWon(name2Ranking.get(team2).getWon() + 1);
            } else {
                name2Ranking.get(team1).setDraw(name2Ranking.get(team1).getDraw() + 1);
                name2Ranking.get(team2).setDraw(name2Ranking.get(team2).getDraw() + 1);
            }
        }

        List<BffResult.Ranking> values = new ArrayList<>(name2Ranking.values());
        values.forEach(BffResult.Ranking::calcWinningPercentage);
        values = values.stream()
                .sorted(Comparator.comparingDouble(BffResult.Ranking::getWinningPercentage))
                .collect(Collectors.toList());
        Collections.reverse(values);

        for (int i = 0; i < values.size(); i++) {
            values.get(i).setPosition(i + 1);
        }

        return values;
    }


    private void initTeam(Map<String, BffResult.Ranking> name2Ranking, String teamName) {
        if (!name2Ranking.containsKey(teamName)) {
            BffResult.Ranking ranking = new BffResult.Ranking();
            ranking.setName(teamName);
            ranking.setFullname(getFullnameForTeamname(teamName));
            ranking.setWon(0);
            ranking.setLost(0);
            ranking.setDraw(0);

            name2Ranking.put(teamName, ranking);
        }
    }

    private String getFullnameForTeamname(String teamname) {
        return allParticipants
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(teamname))
                .map(Participant::getFullname)
                .findFirst()
                .orElse("unbekannt");
    }
}
