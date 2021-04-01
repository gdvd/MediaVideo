package fr.gdvd.media_manager.entitiesNoDb;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LinkMmiVf {

    List<MyMediaInfo> lmmi;
    List<LinkVfTmmi> llvftmmi;

}
