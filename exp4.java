Algorithm:
1. One block is processed by one mapper at a time. In the mapper, a developer can specify his own business logic 
	as per the requirements. In this manner, Map runs on all the nodes of the cluster and process the data blocks in parallel.
2. Output of Mapper also known as intermediate output is written to the local disk. An output of mapper is not stored 
	on HDFS as this is temporary data and writing on HDFS will create unnecessary many copies.
3. Output of mapper is shuffled to reducer node (which is a normal slave node but reduce phase will run here hence called 
		as reducer node). The shuffling/copying is a physical movement of data which is done over the network.
4. Once all the mappers are finished and their output is shuffled on reducer nodes then this intermediate output is 
	merged & sorted. Which is then provided as input to reduce phase.
5. Reduce is the second phase of processing where the user can specify his own custom business logic as per the 
	requirements. An input to a reducer is provided from all the mappers. An output of reducer is the final output, which is written on HDFS.




import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
public class wordCount {
  public static class Map extends Mapper < LongWritable, Text, Text, IntWritable > {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    public void map(LongWritable key, Text value, Context context) throws IOException,
    InterruptedException {
      String line = value.toString();
      StringTokenizer tokenizer = new StringTokenizer(line);
      while (tokenizer.hasMoreTokens()) {
        word.set(tokenizer.nextToken());
        context.write(word, one);
      }
    }
  }
  public static class Reduce extends Reducer < Text, IntWritable, Text, IntWritable > {
    public void reduce(Text key, Iterable < IntWritable > values, Context context)
    throws IOException,
    InterruptedException {
      int sum = 0;
      for (IntWritable val: values) {
        sum += val.get();
      }
      context.write(key, new IntWritable(sum));
    }
  }
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = new Job(conf, "wordcount");
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.waitForCompletion(true);
  }
}