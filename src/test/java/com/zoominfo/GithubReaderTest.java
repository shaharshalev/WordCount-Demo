package com.zoominfo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class GithubReaderTest {

    @Test
    public void testingExtract() throws IOException {

        String actual=new GitHubReader().getContent("githubtraining/hellogitworld","README.txt");
        String expected="This is a sample project students can use during Matthew's Git class.\n" +
                "\n" +
                "Here is an addition by me\n" +
                "\n" +
                "We can have a bit of fun with this repo, knowing that we can always reset it to a known good state.  We can apply labels, and branch, then add new code and merge it in to the master branch.\n" +
                "\n" +
                "As a quick reminder, this came from one of three locations in either SSH, Git, or HTTPS format:\n" +
                "\n" +
                "* git@github.com:matthewmccullough/hellogitworld.git\n" +
                "* git://github.com/matthewmccullough/hellogitworld.git\n" +
                "* https://matthewmccullough@github.com/matthewmccullough/hellogitworld.git\n" +
                "\n" +
                "We can, as an example effort, even modify this README and change it as if it were source code for the purposes of the class.\n" +
                "\n" +
                "This demo also includes an image with changes on a branch for examination of image diff on GitHub.\n";
        Assert.assertEquals(expected,actual);

    }

}