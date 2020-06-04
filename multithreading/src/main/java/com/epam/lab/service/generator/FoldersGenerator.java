package com.epam.lab.service.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FoldersGenerator {

    static void generateFoldersTree(Path sourceDirectory, int subfoldersCount, int averageDepth) throws IOException {
        Files.createDirectory(sourceDirectory);
        int rootSubfoldersCount = subfoldersCount / averageDepth;
        int restSubfoldersCount = subfoldersCount - rootSubfoldersCount;
        for (int x = 1; x <= rootSubfoldersCount; x++) {
            Files.createDirectory(Paths.get(sourceDirectory.toString() + FileSystems.getDefault().getSeparator()
                    + sourceDirectory.getFileName().toString() + "_" + x));
        }
        File[] rootSubFolders = new File(sourceDirectory.toString())
                .listFiles(File::isDirectory);
        generateRandomSubfolders(rootSubFolders, restSubfoldersCount);
    }


    private static void generateRandomSubfolders(File[] rootSubfolders, int count) throws IOException {
        if (count == 0) return;
        int randomNumber = (int) Math.round(Math.random() * (rootSubfolders.length - 1));
        File rootSubfolder = rootSubfolders[randomNumber];
        Path deepestFolder = getDeepestFolder(rootSubfolder);
        Files.createDirectory(Paths.get(deepestFolder.toString() + FileSystems.getDefault().getSeparator()
                + deepestFolder.getFileName().toString() + "_" + count));
        generateRandomSubfolders(rootSubfolders, --count);

    }

    private static Path getDeepestFolder(File subfolder) {
        File[] files = subfolder.listFiles(File::isDirectory);
        return files != null
                ? files.length == 0
                    ? Paths.get(subfolder.getPath())
                    : getDeepestFolder(files[0])
                : Paths.get(subfolder.getPath());

    }
}
