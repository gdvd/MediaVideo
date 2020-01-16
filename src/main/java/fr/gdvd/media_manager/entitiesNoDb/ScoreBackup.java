package fr.gdvd.media_manager.entitiesNoDb;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ScoreBackup {

    private String idtt;
    private String comment;
    private int score;
    private String dateModif;

    public ScoreBackup(String idtt, String comment, int score, String dateModif) {
        this.idtt = idtt;
        this.comment = comment;
        this.score = score;
        this.dateModif = dateModif;
    }


    @Override
    public String toString() {
        return "<ScoreBackup>" +
                "<idtt>" + idtt + "</idtt>" +
                "<comment>" + comment + "</comment>" +
                "<score>" + score + "</score>" +
                "<dateModif>" + dateModif + "</dateModif>" +
                "</ScoreBackup>\n";
    }
}
