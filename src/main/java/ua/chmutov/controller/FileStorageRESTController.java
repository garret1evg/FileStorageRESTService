package ua.chmutov.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.chmutov.DTO.FileDTO;
import ua.chmutov.entity.MyFile;
import ua.chmutov.repository.FileRepository;
import ua.chmutov.responseClasses.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static ua.chmutov.constants.ErrorMessages.*;

@RequestMapping("file")
@RestController
public class FileStorageRESTController {

    @Autowired
    FileRepository repository;

    private long counter = 0;

    @PostMapping
    public ResponseEntity<ResponseInterface> upload(@RequestBody FileDTO file){
        if (counter ==0 ){
            MyFile maxIdFile = repository.findFirstByOrderByIdDesc();
            if (maxIdFile!=null)
                counter = maxIdFile.getId()+1;
        }

        if(file.getName()== null)
            return new ResponseEntity<>(new ErrorResponse(WRONG_NAME), HttpStatus.BAD_REQUEST);

        if(file.getSize()<= 0)
            return new ResponseEntity<>(new ErrorResponse(WRONG_SIZE), HttpStatus.BAD_REQUEST);

        repository.save(new MyFile(counter,file.getName(),file.getSize()));

        return new ResponseEntity<>(new UploadSuccess(counter++), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseInterface> delete(@PathVariable("id") String fileId){
        long id = getCorrectId(fileId);
        if (id==-1)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
        repository.deleteById(id);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }


    @PostMapping("{id}/tags")
    public ResponseEntity<ResponseInterface> assignTags(@PathVariable("id") String fileId,@RequestBody String[] tags){

        long id = getCorrectId(fileId);
        if (id==-1)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);

        MyFile file = repository.findById(id);
        String[] oldTags =file.getTags();
        if (oldTags != null){
            Set<String> set = new LinkedHashSet<>(Arrays.asList(oldTags));
            set.addAll(Arrays.asList(tags));
            tags = set.toArray(new String[0]);
        }
        file.setTags(tags);
        repository.save(file);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
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

    @GetMapping
    ResponseEntity<ResponseInterface> getList(
            @RequestParam(value = "tags",required = false) String[] tags,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size){

        Page<MyFile> pageObj = repository.getPageWithoutParameters(new TagsContainer(tags), PageRequest.of(page,size));
        return new ResponseEntity<>(new GetResponse((int) pageObj.getTotalElements(),pageObj.getContent().toArray(new MyFile[0])),HttpStatus.OK);
    }

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
