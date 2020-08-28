package ua.chmutov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.chmutov.DTO.FileDTO;
import ua.chmutov.entity.MyFile;
import ua.chmutov.repository.FileRepository;
import ua.chmutov.response.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static ua.chmutov.constants.DefaultTagsName.*;
import static ua.chmutov.constants.ErrorMessages.*;

/**
 * Main controller
 */
@RequestMapping("file")
@RestController
public class FileStorageRESTController {

    final FileRepository repository;

    private AtomicLong counter ;

    public FileStorageRESTController(FileRepository repository) {
        this.repository = repository;
        // choosing value for counter
        Iterable<MyFile> list = repository.findAll();
        if(list.spliterator().getExactSizeIfKnown()>0){
            MyFile maxIdFile = repository.findFirstByOrderByIdDesc();
            if (maxIdFile!=null)
                counter = new AtomicLong(maxIdFile.getId()+1);
        }else
            counter = new AtomicLong();

    }

    /**
     * Upload file to DB
     * @param file uploading file
     * @return json
     */
    @PostMapping
    public ResponseEntity<ResponseInterface> upload(
            @RequestBody FileDTO file
    ){


        if(file.getName()== null)
            return new ResponseEntity<>(new ErrorResponse(WRONG_NAME), HttpStatus.BAD_REQUEST);

        if(file.getSize()<= 0)
            return new ResponseEntity<>(new ErrorResponse(WRONG_SIZE), HttpStatus.BAD_REQUEST);

        String[] tags = checkForExtensionAndAddToFile(file.getName());

        MyFile fileInDB = repository.save(new MyFile(counter.getAndIncrement(),file.getName(),file.getSize(),tags));


        //check for default extensions

        return new ResponseEntity<>(new UploadSuccess(fileInDB.getId()), HttpStatus.OK);
    }

    /**
     * adding tags to file
     * @param name file name
     * @return tags array
     */
    private String[] checkForExtensionAndAddToFile(String name){
        String extension ;

        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i+1);
            switch (extension){
                case ("mp3"):
                    return new String[]{AUDIO_TAG};

                case ("avi"):
                    return new String[]{VIDEO_TAG};

                case ("pdf"):
                    return new String[]{DOCUMENT_TAG};

                case ("jpg"):
                    return new String[]{IMAGE_TAG};

                default:
                    return null;

            }
        }
        return null;

    }

    /**
     * delete file
     * @param fileId file id in String
     * @return  json
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseInterface> delete(@PathVariable("id") String fileId){
        //проверка на валидность id
        long id = getCorrectId(fileId);
        if (id==-1)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
        repository.deleteById(id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    /**
     * adding tags to file
     * @param fileId fileId file id
     * @param tags array of String tags values
     * @return json
     */
    @PostMapping("{id}/tags")
    public ResponseEntity<ResponseInterface> assignTags(@PathVariable("id") String fileId,@RequestBody String[] tags){

        long id = getCorrectId(fileId);
        if (id==-1)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);

        setTags(tags, id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    /**
     * merge arrays tags and saving to DB
     * @param tags array of String tags values
     * @param id file id
     */
    private void setTags(@RequestBody String[] tags, long id) {
        MyFile file = repository.findById(id);
        String[] oldTags =file.getTags();
        if (oldTags != null){
            Set<String> set = new LinkedHashSet<>(Arrays.asList(oldTags));
            set.addAll(Arrays.asList(tags));
            tags = set.toArray(new String[0]);
        }
        file.setTags(tags);
        repository.save(file);
    }

    /**
     * delete tags from file
     * @param fileId file id
     * @param tags array of String tags values
     * @return json
     */
    @DeleteMapping("{id}/tags")
    public ResponseEntity<ResponseInterface> deleteTags(@PathVariable("id") String fileId,@RequestBody String[] tags){
        long id = getCorrectId(fileId);
        if (id==-1)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);

        MyFile file = repository.findById(id);
        String[] oldTags =file.getTags();
        String[] modifyTags;
        if (oldTags != null){
            Set<String> set = new LinkedHashSet<>(Arrays.asList(oldTags));
            set.removeAll(Arrays.asList(tags));
            modifyTags = set.toArray(new String[0]);
        }else
            return new ResponseEntity<>(new ErrorResponse(TAG_NOT_FOUND_ON_FILE), HttpStatus.NOT_FOUND);
        if (!Arrays.equals(oldTags,modifyTags)){
            file.setTags(modifyTags);
            repository.save(file);
        }else{
            return new ResponseEntity<>(new ErrorResponse(TAG_NOT_FOUND_ON_FILE), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    /**
     *search
     * @param tags array of String tags values
     * @param page page number
     * @param size page size
     * @param wildcard String pattern to search for
     * @return json
     */
    @GetMapping
    ResponseEntity<ResponseInterface> getList(
            @RequestParam(value = "tags",required = false) String[] tags,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "q",required = false) String wildcard){

        Page<MyFile> pageObj = repository.getPage(new TagsContainer(tags,wildcard), PageRequest.of(page,size));
        return new ResponseEntity<>(new GetResponse((int) pageObj.getTotalElements(),pageObj.getContent().toArray(new MyFile[0])),HttpStatus.OK);
    }

    /**
     * get valid id
     * @param fileId file id
     * @return json
     */
    private long getCorrectId(String fileId) {
        long id = -1;
        try {
            id = Long.parseLong(fileId);
        }catch (NumberFormatException e){
            return id;
        }
        if((id < 0)||!(repository.existsById(id)))
            return -1;
        return id;
    }

}
