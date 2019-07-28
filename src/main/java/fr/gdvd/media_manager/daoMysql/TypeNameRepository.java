package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.TypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface TypeNameRepository extends JpaRepository<TypeName, Long> {

    Optional<TypeName> findByTypeName(String typeName);

    @Query("SELECT tn.typeName FROM fr.gdvd.media_manager.entitiesMysql.TypeName AS tn")
    List<String> findAllTypeName();
}
