package com.zoominfo;

import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@Slf4j
public class FilesUtils {

    public static String createDir(@NonNull String name) throws IOException {
        String current  = new java.io.File( "." ).getCanonicalPath();
        String filePath= current+"\\"+name;
        Path path = Paths.get(name);
        Files.createDirectories(path);
        log.info("Directory {} was created!",name);
        return filePath;

    }

    public static void stringToFile(@NonNull String name, String content,@NonNull String path) throws IOException {
        final String normalizedName=name.replace("/","_").replace(".","_");
        final String fileName=path+"/"+normalizedName;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }
    
}


