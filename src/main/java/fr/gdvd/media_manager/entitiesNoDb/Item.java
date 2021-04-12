package fr.gdvd.media_manager.entitiesNoDb;

import lombok.Data;

@Data
public class Item {

    private String title;
    private String description;
    private String pubDate;
    private String link;
    private String comment;

    @Override
    public String toString() {
        return "<item>" +
                "<title>" + title + "</title>" +
                "<link>" + link + "</link>" +
                "<pubDate>" + pubDate + "</pubDate>" +
                "<comment>" + comment + "</comment>" +
                "<description>" + description + "</description>" +
                "</item>";
    }
}
