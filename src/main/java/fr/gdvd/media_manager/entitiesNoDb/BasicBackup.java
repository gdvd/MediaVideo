package fr.gdvd.media_manager.entitiesNoDb;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BasicBackup {

    private String idMyMediaInfo;
    private Double fileSize;
    private Double bitrate;
    private Double duration;
    private String codecId;
    private String nameSerieVo;
    private String typeName;
    private Integer episode;
    private Integer saison;
    private String idTt;

    public BasicBackup() {
    }

    public BasicBackup(String idMyMediaInfo, Double fileSize, Double bitrate, Double duration, String codecId) {
        this.idMyMediaInfo = idMyMediaInfo;
        this.fileSize = fileSize;
        this.bitrate = bitrate;
        this.duration = duration;
        this.codecId = codecId;
    }

    @Override
    public String toString() {
        return "<BasicBackup>" +
                "<idMyMediaInfo>" + idMyMediaInfo + "</idMyMediaInfo>" +
                "<fileSize>" + String.format ("%.0f", fileSize) +"</fileSize>"+
                "<bitrate>" + String.format ("%.0f", bitrate) +"</bitrate>"+
                "<duration>" + duration +"</duration>"+
                "<codecId>" + codecId + "</codecId>" +
                "<nameSerieVo>" + nameSerieVo + "</nameSerieVo>" +
                "<typeName>" + typeName + "</typeName>" +
                "<episode>" + episode +"</episode>"+
                "<saison>" + saison +"</saison>"+
                "<idTt>" + idTt + "</idTt>" +
                "</BasicBackup>\n";
    }
}
