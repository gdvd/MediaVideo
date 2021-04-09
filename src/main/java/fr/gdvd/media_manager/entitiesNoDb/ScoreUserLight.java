package fr.gdvd.media_manager.entitiesNoDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreUserLight {

    private String idtt;
    private String title;
    private int score;
    private String comment;
    private Timestamp date;

    @Override
    public String toString() {
        return "<idtt>" + idtt + "</idtt>" +
                "<title>" + title + "</title>" +
                "<score>" + score + "</score>" +
                "<date>" + date + "</date>" +
                "<comment>" + comment + "</comment>";

    }
}
