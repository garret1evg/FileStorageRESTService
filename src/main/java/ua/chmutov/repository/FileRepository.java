package ua.chmutov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ua.chmutov.entity.MyFile;
import ua.chmutov.response.TagsContainer;

@Repository
public interface FileRepository extends ElasticsearchRepository<MyFile,Long> {

    boolean existsById(Long id);
    MyFile findFirstByOrderByIdDesc();

    MyFile findById(long id);
    @Query(" {\n" +
            "        \"bool\":{\n" + "?0"+
            "\n" +
            "        }\n" +
            "        \n" +
            "    }")
    Page<MyFile> getPageWithoutParameters(TagsContainer container, Pageable pageable);
}
