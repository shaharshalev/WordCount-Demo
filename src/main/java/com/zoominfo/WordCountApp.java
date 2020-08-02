package com.zoominfo;


import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.*;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class WordCountApp {

    private static final String TOKENIZER_PATTERN = "[^\\p{L}]+";


    public static void main(String[] args) throws IOException {
        PipelineOptionsFactory.register(WordCountReposOptions.class);
        WordCountReposOptions options =
                PipelineOptionsFactory.fromArgs(args).as(WordCountReposOptions.class);
        new GithubExtractor().extract(parseReposList(options.getRepos()));
        runWordCount(options, GithubExtractor.DirectoryName + "/*");

    }

    /**
     * Concept #2: You can make your pipeline assembly code less verbose by defining your DoFns
     * statically out-of-line. This DoFn tokenizes lines of text into individual words; we pass it to
     * a ParDo in the pipeline.
     */
    static class ExtractWordsFn extends DoFn<String, String> {

        @ProcessElement
        public void processElement(@Element String element, OutputReceiver<String> receiver) {
            // Split the line into words.
            String[] words = element.split(TOKENIZER_PATTERN, -1);

            // Output each word encountered into the output PCollection.
            for (String word : words) {
                if (!word.isEmpty()) {
                    receiver.output(word);
                }
            }
        }
    }

    /**
     * A SimpleFunction that converts a Word and Count into a printable string.
     */
    public static class FormatAsTextFn extends SimpleFunction<KV<String, Long>, String> {
        @Override
        public String apply(KV<String, Long> input) {
            return input.getKey() + ": " + input.getValue();
        }
    }

    /**
     * A PTransform that converts a PCollection containing lines of text into a PCollection of
     * formatted word counts.
     *
     * <p>Concept #3: This is a custom composite transform that bundles two transforms (ParDo and
     * Count) as a reusable PTransform subclass. Using composite transforms allows for easy reuse,
     * modular testing, and an improved monitoring experience.
     */
    public static class CountWords
            extends PTransform<PCollection<String>, PCollection<KV<String, Long>>> {
        @Override
        public PCollection<KV<String, Long>> expand(PCollection<String> lines) {

            // Convert lines of text into individual words.
            PCollection<String> words = lines.apply(ParDo.of(new ExtractWordsFn()));
            // Count the number of times each word occurs.
            return words.apply(Count.perElement());
        }
    }

    /**
     * Options supported by {@link WordCountApp}.
     *
     * <p>Concept #4: Defining your own configuration options. Here, you can add your own arguments to
     * be processed by the command-line parser, and specify default values for them. You can then
     * access the options values in your pipeline code.
     *
     * <p>Inherits standard configuration options.
     */
    public interface WordCountReposOptions extends PipelineOptions {

        @Description("List of repositories in github separated by ','")
        @Default.String("Zoominfo/api-auth-python-client")
        String getRepos();

        void setRepos(String input);

        /**
         * Set this required option to specify where to write the output.
         */
        @Description("Path of the file to write to")
        @Validation.Required
        String getOutput();

        void setOutput(String value);
    }

    static void runWordCount(WordCountReposOptions options, String filePattern) {
        Pipeline p = Pipeline.create(options);

        // Concepts #2 and #3: Our pipeline applies the composite CountWords transform, and passes the
        // static FormatAsTextFn() to the ParDo transform.
        p.apply("ReadLines", TextIO.read().from(filePattern))
                .apply(new CountWords())
                .apply(MapElements.via(new FormatAsTextFn()))
                .apply("WriteCounts", TextIO.write().to(options.getOutput()));

        p.run().waitUntilFinish();
    }


    private static List<String> parseReposList(String repos) {
        return Arrays.asList(repos.split(","));
    }


}
