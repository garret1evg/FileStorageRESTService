package ua.chmutov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.chmutov.DTO.FileDTO;
import ua.chmutov.responseClasses.ErrorResponse;
import ua.chmutov.responseClasses.ResponseInterface;
import ua.chmutov.responseClasses.SuccessResponse;
import ua.chmutov.responseClasses.UploadSuccess;

import java.util.concurrent.atomic.AtomicLong;

import static ua.chmutov.constants.ErrorMessages.*;

@RequestMapping("file")
@RestController
public class FileStorageRESTController {

    private final AtomicLong counter = new AtomicLong();

    @PostMapping
    public ResponseEntity<ResponseInterface> upload(@RequestBody FileDTO file){
        System.out.println(file);
        if(file.getName()== null)
            return new ResponseEntity<>(new ErrorResponse(WRONG_NAME), HttpStatus.BAD_REQUEST);
        if(file.getSize()<= 0)
            return new ResponseEntity<>(new ErrorResponse(WRONG_SIZE), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new UploadSuccess( counter.incrementAndGet()), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseInterface> delete(@PathVariable("id") String fileId){
        long id=-1;
        try {
            id = Long.parseLong(fileId);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if(id < 0)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
    @PostMapping("{id}/tags")
    public ResponseEntity<ResponseInterface> assignTags(@PathVariable("id") String fileId,@RequestBody String[] tags){
        for (String tag:tags) {
            System.out.println(tag);
        }

        long id=-1;
        try {
            id = Long.parseLong(fileId);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if(id < 0)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
    @DeleteMapping("{id}/tags")
    public ResponseEntity<ResponseInterface> deleteTags(@PathVariable("id") String fileId,@RequestBody String[] tags){
        long id=-1;
        try {
            id = Long.parseLong(fileId);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if(id < 0)
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);

        if(id >7)
            return new ResponseEntity<>(new ErrorResponse(TAG_NOT_FOUND_ON_FILE), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping ResponseEntity<ResponseInterface> getList(
            @RequestParam(value = "tags",required = false) String[] tags,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size){
        if (tags!= null)
            for (String tag:tags)
                System.out.println(tag);

        System.out.println(page);
        System.out.println(size);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

}
