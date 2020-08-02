package com.zoominfo;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

@Log
public class GitHubReader {
    private final OkHttpClient client;

    public GitHubReader() {
        this.client = new OkHttpClient();
    }

    public String getContent(@NonNull String repositoryName, @NonNull String fileName) throws IOException {
        String githubUrlToFile = "https://api.github.com/repos/"+repositoryName+"/contents/"+fileName;
        log.info("sending request to github url: "+githubUrlToFile);
        Request request = new Request.Builder()
                .url(githubUrlToFile)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        try {
            log.info("extracting content from response body");
            JSONObject jsonObject = new JSONObject(response.body().string());
            String encodedString = jsonObject.getString("content");
            log.info("successfully extracted content from jsonObject trying to decode the content");
            String decodedString = new String(Base64.decodeBase64(encodedString.getBytes()));
            log.info("successfully decoded: \n"+decodedString);
            return decodedString;
        }catch (JSONException err) {
            log.severe("failed to extract the content");
            throw err;
        }
    }


}
