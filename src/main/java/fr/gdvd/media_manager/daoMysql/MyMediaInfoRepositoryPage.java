package fr.gdvd.media_manager.daoMysql;

import fr.gdvd.media_manager.entitiesMysql.MyMediaInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

// https://www.baeldung.com/spring-data-jpa-pagination-sorting
// https://www.baeldung.com/spring-thymeleaf-pagination

public interface MyMediaInfoRepositoryPage extends PagingAndSortingRepository<MyMediaInfo, String> {

    Page<MyMediaInfo> findAllBy(Pageable pageable);



}
