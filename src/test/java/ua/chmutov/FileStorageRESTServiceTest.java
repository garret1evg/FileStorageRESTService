package ua.chmutov;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.chmutov.DTO.FileDTO;
import ua.chmutov.entity.MyFile;
import ua.chmutov.response.GetResponse;
import ua.chmutov.response.UploadSuccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileStorageRESTServiceTest extends AbstractTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void uploadAndDeleteFile() throws Exception {
        String uri = "/file";
        String name ="aa.img";
        int size= 5;
        String inputJson = super.mapToJson(new FileDTO(name,size));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        UploadSuccess id = super.mapFromJson(content, UploadSuccess.class);
        assertTrue(id.getId()>=0);
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri+"/"+id.getId())).andReturn();
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
    @Test
    public void getFilesList() throws Exception {
        String uri = "/file";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        GetResponse filelist = super.mapFromJson(content, GetResponse.class);
        assertTrue(filelist.getPage().length > 0);
    }
}
