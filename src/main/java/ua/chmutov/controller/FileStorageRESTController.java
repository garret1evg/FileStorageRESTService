package ua.chmutov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.chmutov.DTO.FileDTO;
import ua.chmutov.repository.FileRepository;
import ua.chmutov.response.ErrorResponse;
import ua.chmutov.response.ResponseInterface;
import ua.chmutov.response.SuccessResponse;
import ua.chmutov.response.UploadSuccess;
import ua.chmutov.service.FileStorageService;

import static ua.chmutov.constants.ErrorMessages.*;

/**
 * Main controller
 */
@RequestMapping("file")
@RestController
public class FileStorageRESTController {

    final FileStorageService fileStorageService;

    public FileStorageRESTController(FileRepository repository, FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
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
        long id =fileStorageService.upload(file);
        if(id==-1)
            return new ResponseEntity<>(new ErrorResponse(WRONG_NAME), HttpStatus.BAD_REQUEST);
        if(id==-2)
            return new ResponseEntity<>(new ErrorResponse(WRONG_SIZE), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new UploadSuccess(id), HttpStatus.OK);
    }

    /**
     * delete file
     * @param fileId file id in String
     * @return  json
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseInterface> delete(@PathVariable("id") String fileId){
        if (!fileStorageService.delete(fileId))
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
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
        if (fileStorageService.assignTags(fileId,tags))
            return new ResponseEntity<>(new ErrorResponse(FILE_NOT_FOUND), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }


    /**
     * delete tags from file
     * @param fileId file id
     * @param tags array of String tags values
     * @return json
     */
    @DeleteMapping("{id}/tags")
    public ResponseEntity<ResponseInterface> deleteTags(@PathVariable("id") String fileId,@RequestBody String[] tags){
        if (!fileStorageService.deleteTags(fileId,tags))
            return new ResponseEntity<>(new ErrorResponse(TAG_NOT_FOUND_ON_FILE), HttpStatus.NOT_FOUND);
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


        return new ResponseEntity<>(fileStorageService.getPage(tags,page,size,wildcard),HttpStatus.OK);
    }



}
