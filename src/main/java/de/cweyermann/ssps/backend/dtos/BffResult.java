package de.cweyermann.ssps.backend.dtos;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class BffResult {

    public static final SimpleDateFormat NICE_DATE = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

    @Data
    public static class Ranking {
        private int position;
        private String name;
        private String fullname;

        private double winningPercentage;

        private int won;
        private int lost;
        private int draw;

        public void calcWinningPercentage() {
            int total = won + lost + draw;
            double winingNumber = 0.0 + won + draw / 2.0;

            this.winningPercentage = winingNumber / total;
        }
    }

    @Data
    public static class Match {
        private String niceDate;
        private Date date;

        private String team1name;
        private String team1choice;

        private String team2name;
        private String team2choice;

        private String winnerName;

        public Match withNiceDate() {
            if (date != null) {
                this.niceDate = NICE_DATE.format(date);
            }
            return this;
        }
    }

    public List<Ranking> ranking;

    public List<Match> matches;


}
