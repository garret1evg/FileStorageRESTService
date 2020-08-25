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
@RequestMapping("file")
@RestController
public class FileStorageRESTController {

    private final AtomicLong counter = new AtomicLong();

    @PostMapping
    public ResponseEntity<ResponseInterface> upload(@RequestBody FileDTO file){
        System.out.println(file);
        if(file.getName()== null)
            return new ResponseEntity<>(new ErrorResponse("wrong name"), HttpStatus.BAD_REQUEST);
        if(file.getSize()<= 0)
            return new ResponseEntity<>(new ErrorResponse("wrong size"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new UploadSuccess( counter.incrementAndGet()), HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseInterface> delete(@PathVariable("id") String fileId){
        try {
            if(Long.parseLong(fileId) < 0)
                return new ResponseEntity<>(new ErrorResponse("file not found"), HttpStatus.NOT_FOUND);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(new ErrorResponse("file not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
    @PostMapping("{id}/tags")
    ResponseEntity<ResponseInterface> assignTags(@PathVariable("id") String fileId,@RequestBody String[] tags){
        for (String tag:tags) {
            System.out.println(tag);
        }

        try {
            if(Long.parseLong(fileId) < 0)
                return new ResponseEntity<>(new ErrorResponse("file not found"), HttpStatus.NOT_FOUND);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(new ErrorResponse("file not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
    @DeleteMapping("{id}/tags")
    ResponseEntity<ResponseInterface> deleteTags(@PathVariable("id") String fileId,@RequestBody String[] tags){
        try {
            if(Long.parseLong(fileId) < 0)
                return new ResponseEntity<>(new ErrorResponse("file not found"), HttpStatus.NOT_FOUND);
        }catch (NumberFormatException e){
            return new ResponseEntity<>(new ErrorResponse("file not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
