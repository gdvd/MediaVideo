package fr.gdvd.media_manager.dao;

import fr.gdvd.media_manager.entities.MediaPath;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MediaPathRepository extends CrudRepository<MediaPath, ObjectId> {
    MediaPath getByNameExportAndActive(String nameExport);
}
