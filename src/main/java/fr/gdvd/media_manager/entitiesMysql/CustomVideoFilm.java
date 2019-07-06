package fr.gdvd.media_manager.entitiesMysql;

public interface CustomVideoFilm {
    /* https://www.baeldung.com/spring-data-rest-projections-excerpts
    @Projection(
    name = "customBook",
    types = { Book.class })
    public interface CustomBook {
            String getTitle();
    }

    @Projection(
    name = "customBook",
    types = { Book.class })
    public interface CustomBook {
        @Value("#{target.id}")
        long getId();

        String getTitle();
    }

@Projection(name = "customBook", types = { Book.class })
public interface CustomBook {

    @Value("#{target.id}")
    long getId();

    String getTitle();

    @Value("#{target.getAuthors().size()}")
    int getAuthorCount();
}
    */



}
