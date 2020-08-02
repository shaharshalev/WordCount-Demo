package com.zoominfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;


public class FilesUtils {

    public static String createDir(String name) throws IOException {
        String current  = new java.io.File( "." ).getCanonicalPath();
        String filePath= current+"\\"+name;
        Path path = Paths.get(name);

        //java.nio.file.Files;
        Files.createDirectories(path);

        System.out.println("Directory is created!");
        return filePath;

    }

    public static void stringToFile(String name,String content,String path) throws IOException {
        name=name.replace("/","_");
        name=name.replace(".","_");
        String fileName=path+"/"+name;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }
    
}


