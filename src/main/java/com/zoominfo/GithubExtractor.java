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
    private String dirPath;
    public GithubExtractor(){
        counter=new AtomicInteger(0);
        try {
            dirPath=FilesUtils.createDir(DirectoryName);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    public void extract(@NonNull List<String> repos) {

        log.info("Extracting the following repos {} to directory {}",repos,DirectoryName);
        repos.parallelStream()
                .map(repo->getReadmeFileContent(repo))
                .forEach(content -> writeToGithubFolder(content));
    }

    private void writeToGithubFolder(@NonNull String content) {
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
