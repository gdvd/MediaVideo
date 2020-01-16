package fr.gdvd.media_manager.entitiesNoDb;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import fr.gdvd.media_manager.entitiesMysql.VideoUserScore;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Filmlight {

    private String title;
    private String idTt;
    private String infoTitle;
    private int year;
    private int score;
    private int pos;

    private int actorPos;
    private boolean actor;
    private boolean writer;
    private boolean director;
    private boolean soundtrack;
    private boolean producer;
    private boolean thanks;
    private boolean self;
    private boolean showit=true;

    private boolean appeared;
    private boolean loaded;

    private List<MyMediaInfo> lmmi;
    private VideoUserScore vus;
}
