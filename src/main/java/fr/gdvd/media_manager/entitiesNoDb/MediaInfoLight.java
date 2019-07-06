package fr.gdvd.media_manager.entitiesNoDb;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

//@Projection(name = "MediaInfoLight", types = {MyMediaInfo.class})
public interface MediaInfoLight extends List<MyMediaInfo> {

//    String getIdMyMediaInfo = null;
//    String getFormat = null;
//    Double getFileSize = null;
//    Double getDuration = null;
}



/*

@Value("#{target.id}")
    long getId();

    @Value("#{target.getAuthors().size()}")
    int getAuthorCount();

List<Author> getAuthors();

    @Value("#{target.getAuthors().size()}")
    int getAuthorCount();

@Projection(
  name = "customBook", 
  types = { Book.class }) 
public interface CustomBook { 
    String getTitle();
}
@Value("#{target.firstName} #{target.lastName}")
     String getFullName();

     @Value("#{target.department.name}")
     String getDepartmentName();


___________

@Repository
 public interface EmployeeRepository extends JpaRepository<Employee, Long> {
     List<EmployeeByCloseProjectionRs> findByFirstName(String firstName);
}
*/
