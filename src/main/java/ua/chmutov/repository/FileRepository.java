package ua.chmutov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ua.chmutov.entity.MyFile;
@Repository
public interface FileRepository extends ElasticsearchRepository<MyFile,Long> {

    boolean existsById(Long id);
    MyFile findFirstByOrderByIdDesc();

    MyFile findById(long id);
//    @Query("\"query\": {\n" +
//            "        \"match\" : {\n" +
//            "            \"name\" : \"5ltdtyjkuhstdghth\"\n" +
//            "        }\n" +
//            "    },\n{\"size\": ?1," +
//            "    \"from\": ?0," +
//            "    \"_source\": [ \"id\", \"name\", \"size\" ,\"tags\"]}")
//    Page<MyFile> getPage(int from,int size);
}
