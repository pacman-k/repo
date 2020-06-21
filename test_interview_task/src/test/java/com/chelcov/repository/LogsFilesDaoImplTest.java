package com.chelcov.repository;

import com.chelcov.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class})
public class LogsFilesDaoImplTest {

    @Autowired
    LogsFilesDao logsFilesDao;

    @Test
    public void getExistingFile() {
         Optional<Path> file = logsFilesDao.getFile("log1.json");
        assertTrue(file.isPresent());
    }

    @Test
    public void getNonExistingFile() {
        Optional<Path> file = logsFilesDao.getFile("random.json");
        assertFalse(file.isPresent());
    }

    @Test
    public void getAllFiles() {
         List<Path> allFiles = logsFilesDao.getAllFiles();
        assertEquals(allFiles.size(), 5);
    }
}