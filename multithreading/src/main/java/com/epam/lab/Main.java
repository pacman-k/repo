package com.epam.lab;

import com.epam.lab.service.NewsService;
import com.epam.lab.service.generator.FilesGenerators;
import com.epam.lab.service.generator.MainGenerator;
import com.epam.lab.service.reader.MainReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;


public class Main {


    public static void main(String[] args) {

        ApplicationContext ap = new AnnotationConfigApplicationContext(Config.class);
        Environment environment = ap.getEnvironment();
        int filesPerFolder = Integer.parseInt(Objects.requireNonNull(environment.getProperty("FILES_COUNT")));
        int totalFoldersCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty("SUBFOLDERS_COUNT"))) + 1;
        int newsPerFile = FilesGenerators.OBJECTS_PER_FILE;

        NewsService newsService = ap.getBean(NewsService.class);
        long countNewsInDBBeforeProcess = newsService.getCountOfNews();
        MainGenerator mainGenerator = ap.getBean(MainGenerator.class);
        MainReader mainReader = ap.getBean(MainReader.class);
        Path path = mainGenerator.generate("root");
        Path errorPath = mainReader.read(path);
        long countNewsInDBAfterProcess = newsService.getCountOfNews();
        long totalCountOfProcessedNews = countNewsInDBAfterProcess + getCountNewsFromFolder(errorPath) - countNewsInDBBeforeProcess;
        long expectedNewsCount = filesPerFolder * totalFoldersCount * newsPerFile;


        assert (totalCountOfProcessedNews == expectedNewsCount) : String.format("expected - %d, but actually - %d", expectedNewsCount, totalCountOfProcessedNews);
        System.out.println("All files have been processed");


    }

    private static long getCountNewsFromFolder(Path errorFolder) {
        return Optional.ofNullable(new File(errorFolder.toString()).listFiles(File::isFile)).orElse(new File[0]).length * 3;
    }

}
