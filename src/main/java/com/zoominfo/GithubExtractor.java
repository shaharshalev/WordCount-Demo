package com.zoominfo;

import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GithubExtractor is a thread safe module that extract in parallel all readme files content
 * from github repositories and write it to a const Directory named {DirectoryName}
 */
@Slf4j
public class GithubExtractor {

    private final AtomicInteger counter;
    static final String DirectoryName="github";
    public GithubExtractor(){
        counter=new AtomicInteger(0);
    }

    public void extract(@NonNull List<String> repos) throws IOException {
        log.info("Extracting the following repos {} to directory {}",repos,DirectoryName);
        final String dirPath= FilesUtils.createDir(DirectoryName);
        repos.parallelStream()
                .map(this::getReadmeFileContent)
                .forEach(content -> writeToGithubFolder(content,dirPath));
    }

    private void writeToGithubFolder(@NonNull String content,@NonNull String dirPath) {
        try {
            final String fileName= "readme" + counter.get();
            log.info("saving content with the name: "+fileName);
            FilesUtils.stringToFile(fileName, content, dirPath);
            counter.incrementAndGet();
        }catch (IOException e){
            log.error(e.toString());
        }
    }

    private String getReadmeFileContent(@NonNull String repo) {
        try {
            return new GitHubReader().getContent(repo, "README.md");
        }catch (IOException ex){
            log.error(ex.toString());
        }
        return null;
    }

}
