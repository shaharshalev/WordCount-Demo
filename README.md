
# WordCount  
  
  The WordCount examples demonstrate how to set up a processing pipeline that can read text, tokenize the text lines into individual words, and perform a frequency count on each of those words.

## How to run

You can run the application with the following maven command template:
```
mvn compile exec:java -Dexec.mainClass=com.zoominfo.WordCountApp -Dexec.args="--repos=<repo1,repo2...repoN> --output=<output_filename_prefix>" -Pdirect-runner
```

for example you can run the following in-order to count the number of words in the repositories Zoominfo/api-auth-java-client & Zoominfo/api-auth-python-client Readme.md files:

```
mvn compile exec:java -Dexec.mainClass=com.zoominfo.WordCountApp -Dexec.args="--repos=Zoominfo/api-auth-java-client,Zoominfo/api-auth-python-client --output=counts" -Pdirect-runner
```

## Inspect the results
on the project folder run the following command ( after executing the app): 
```
cat counts*
```

## The application supports running with the following distributed processing back-ends:
-   Apache Flink  ![Apache Flink logo](https://beam.apache.org/images/logos/runners/flink.png)
-   Apache Samza 
-   Apache Spark  ![Apache Spark logo](https://beam.apache.org/images/logos/runners/spark.png)
-   Google Cloud Dataflow  ![Google Cloud Dataflow logo](https://beam.apache.org/images/logos/runners/dataflow.png)
-   Hazelcast Jet  ![Hazelcast Jet logo](https://beam.apache.org/images/logos/runners/jet.png)

