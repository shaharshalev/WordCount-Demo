package com.zoominfo;

import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log
public class GithubExtractor {

    private final AtomicInteger counter;
    static final String DirectoryName="github";
    public GithubExtractor(){
        counter=new AtomicInteger(0);
    }

    public void extract(@NonNull List<String> repos) throws IOException {
        final String dirPath= FilesUtils.createDir(DirectoryName);

        GitHubReader gitHubReader= new GitHubReader();
        repos.stream()
                .map(repo -> getReadmeFileContent(gitHubReader, repo))
                .forEach(content -> writeToGithubFolder(content,dirPath));
    }

    private void writeToGithubFolder(@NonNull String content,@NonNull String dirPath) {
        try {
            final String fileName= "readme" + counter.intValue();
            log.info("saving content with the name: "+fileName);
            FilesUtils.stringToFile(fileName, content, dirPath);
            counter.incrementAndGet();
        }catch (IOException e){
            log.severe(e.toString());
        }
    }

    private String getReadmeFileContent(@NonNull  GitHubReader gitHubReader,@NonNull String repo) {
        try {
            return gitHubReader.getContent(repo, "README.md");
        }catch (IOException ex){
            log.severe(ex.toString());
        }
        return null;
    }

}
