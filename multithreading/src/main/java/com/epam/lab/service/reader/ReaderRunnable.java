package com.epam.lab.service.reader;

import com.epam.lab.service.NewsService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ReaderRunnable implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ReaderRunnable.class);
    private static final long WAIT_TIME = 10;

    private LinkedBlockingQueue<File> jsonFiles;
    private NewsService newsService;
    private Path afterProcessDir;
    private Thread threadForInterrupting;

    ReaderRunnable(Path rootDir, NewsService newsService, Thread threadForInterrupting, Path... excludeDirs) {
        this.jsonFiles = new LinkedBlockingQueue<>();
        this.newsService = newsService;
        this.threadForInterrupting = threadForInterrupting;
        this.afterProcessDir = excludeDirs.length != 0
                ? excludeDirs[0]
                : rootDir.getParent();
        File rootDirectory = new File(rootDir.toString());
        new Daemon(jsonFiles, rootDirectory, excludeDirs).start();
    }

    @Override
    public void run() {

        try {
            File newsFile = jsonFiles.poll(WAIT_TIME, TimeUnit.SECONDS);
            if (newsFile == null) {
                if (toTerminateJob()) threadForInterrupting.interrupt();
                return;
            }

            boolean isSaved = newsService.saveNews(newsFile);
            if (isSaved) {
                deleteFile(newsFile);
            } else {
                saveToFolder(newsFile, afterProcessDir);
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    private static boolean toTerminateJob() {
        Scanner scanner = new Scanner(System.in);
        String request = "There are no new files for 10sec. Terminate job (Y/N)? ";
        System.out.print(request);
        while (!scanner.hasNext("[NYny]")) {
            System.out.print(request);
            scanner.next();
        }
        String answer = scanner.next();
        return answer.equalsIgnoreCase("y");
    }


    private static void deleteFile(File file) throws IOException {
        Files.delete(Paths.get(file.getPath()));
    }

    private static void saveToFolder(File file, Path errorFolder) throws IOException {
        Files.move(Paths.get(file.getPath())
                , Paths.get(errorFolder.toString() + FileSystems.getDefault().getSeparator() + file.getName())
                , StandardCopyOption.ATOMIC_MOVE);
    }

    private static Collection<File> getOrEmpty(File[] files) {
        return Objects.nonNull(files)
                ? new LinkedList<>(Arrays.asList(files))
                : Collections.emptyList();
    }


    private static class Daemon extends Thread {

        private LinkedBlockingQueue<File> jsonFiles;
        private Queue<File> directories;
        private Map<File, Set<File>> dirsWithFiles;

        Daemon(LinkedBlockingQueue<File> jsonFiles, File rootDirectory, Path[] excludeDirs) {
            setDaemon(true);
            this.jsonFiles = jsonFiles;
            this.directories = new LinkedList<>(Collections.singletonList(rootDirectory));
            this.dirsWithFiles = new HashMap<>();
            putEmptyNode(rootDirectory);
            Arrays.stream(excludeDirs)
                    .map(Path::toString)
                    .map(File::new)
                    .forEach(this::putEmptyNode);

        }

        @Override
        public void run() {
            while (true) {
                File directory = directories.remove();
                Collection<File> subDirs = getOrEmpty(directory.listFiles(File::isDirectory));
                Collection<File> subFiles = getOrEmpty(directory.listFiles(File::isFile));
                addUnregisteredDirs(subDirs);

                jsonFiles.addAll(getUnregisteredFiles(subFiles, directory));
                dirsWithFiles.put(directory, new HashSet<>(subFiles));
                directories.add(directory);
            }
        }

        private Set<File> getUnregisteredFiles(Collection<File> files, File directory) {
            Set<File> unregistered = new HashSet<>(files);
            unregistered.removeAll(
                    Optional
                            .ofNullable(dirsWithFiles.get(directory))
                            .orElseGet(Collections::emptySet));
            return unregistered;
        }

        private void addUnregisteredDirs(Collection<File> dirs) {
            dirs.stream()
                    .filter(this::notRegisteredDir)
                    .forEach(directories::add);
        }

        private boolean notRegisteredDir(File dir) {
            return !dirsWithFiles.containsKey(dir);
        }

        private void putEmptyNode(File dir) {
            dirsWithFiles.put(dir, Collections.emptySet());
        }


    }
}
