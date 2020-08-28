package ua.chmutov.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ua.chmutov.DTO.FileDTO;
import ua.chmutov.entity.MyFile;
import ua.chmutov.repository.FileRepository;
import ua.chmutov.response.ErrorResponse;
import ua.chmutov.response.GetResponse;
import ua.chmutov.response.TagsContainer;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static ua.chmutov.constants.DefaultTagsName.*;
import static ua.chmutov.constants.DefaultTagsName.IMAGE_TAG;
import static ua.chmutov.constants.ErrorMessages.*;

@Service
public class FileStorageService {

    final FileRepository repository;
    private AtomicLong counter;

    public FileStorageService(FileRepository repository) {
        this.repository = repository;
        // choosing value for counter
        Iterable<MyFile> list = repository.findAll();
        if (list.spliterator().getExactSizeIfKnown() > 0) {
            MyFile maxIdFile = repository.findFirstByOrderByIdDesc();
            if (maxIdFile != null)
                counter = new AtomicLong(maxIdFile.getId() + 1);
        } else
            counter = new AtomicLong();
    }

    public long upload(FileDTO file) {
        if (file.getName() == null)
            return -1;

        if (file.getSize() <= 0)
            return -2;

        //check for default extensions and add
        String[] tags = checkForExtensionAndAddToFile(file.getName());

        MyFile fileInDB = repository.save(new MyFile(counter.getAndIncrement(), file.getName(), file.getSize(), tags));
        return fileInDB.getId();
    }

    public boolean delete(String fileId){
        long id = getCorrectId(fileId);
        if (id==-1)
            return false;
        repository.deleteById(id);
        return true;

    }
    public  boolean assignTags(String fileId,String[] tags){
        long id = getCorrectId(fileId);
        if (id==-1)
            return false;
        setTags(tags, id);
        return true;
    }

    public  boolean deleteTags(String fileId,String[] tags){
        long id = getCorrectId(fileId);
        if (id==-1)
            return false;

        MyFile file = repository.findById(id);
        String[] oldTags =file.getTags();
        String[] modifyTags;
        if (oldTags != null){
            Set<String> set = new LinkedHashSet<>(Arrays.asList(oldTags));
            set.removeAll(Arrays.asList(tags));
            modifyTags = set.toArray(new String[0]);
        }else
            return false;
        if (!Arrays.equals(oldTags,modifyTags)){
            file.setTags(modifyTags);
            repository.save(file);
        }else{
            return false;
        }
        return true;
    }
    public GetResponse getPage(String[] tags,int page,int size,String wildcard){
        Page<MyFile> pageObj = repository.getPage(new TagsContainer(tags,wildcard), PageRequest.of(page,size));
        return new GetResponse((int) pageObj.getTotalElements(),pageObj.getContent().toArray(new MyFile[0]));
    }


    /**
     * adding tags to file
     *
     * @param name file name
     * @return tags array
     */
    private String[] checkForExtensionAndAddToFile(String name) {
        String extension;

        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i + 1);
            switch (extension) {
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
}
