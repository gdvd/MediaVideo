package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface MediaPathRepository extends MongoRepository<MediaPath, String> {
    MediaPath getByNameExportAndActive(String nameExport);
    MediaPath getByPathGeneral(String pathGeneral);
    MediaPath getByNameExport(String nameExport);
    MediaPath getById(String id);
    MediaPath save(MediaPath mediaPath);
    List<MediaPath> findAllByActiveTrue();

}
