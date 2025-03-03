package service;

import com.safetynet.repository.DataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.service.DataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    @InjectMocks
    private DataService dataService;

    @Mock
    private Resource resourceFile;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void testLoadData_success() throws IOException {
        DataRepository mockDataRepo = new DataRepository();
        File mockFile = mock(File.class);
        when(resourceFile.getFile()).thenReturn(mockFile);
        when(objectMapper.readValue(any(File.class), eq(DataRepository.class))).thenReturn(mockDataRepo);

        dataService.LoadData();
        assertNotNull(dataService.getData(), "DataRepository should not be null on successful load.");
    }

    @Test
    void testLoadData_fileNotFound() throws IOException {
        when(resourceFile.getFile()).thenThrow(new IOException("File not found"));
        dataService.LoadData();
        assertNull(dataService.getData(), "DataRepository should be null when file is not found.");
    }

    @Test
    void testLoadData_invalidJson() throws IOException {
        File mockFile = mock(File.class);
        when(resourceFile.getFile()).thenReturn(mockFile);
        lenient().when(objectMapper.readValue(any(File.class), eq(DataRepository.class)))
                .thenThrow(new IOException("Invalid JSON"));
        dataService.LoadData();
        assertNull(dataService.getData(), "DataRepository should be null when JSON is invalid.");
    }
}
