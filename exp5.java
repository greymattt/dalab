ALGORITHM:
1. The Hadoop job reads input weather data from a specified input path.
2. The Mapper (MaxTemperatureMapper) processes each line of input data, checking for
hot and cold days and emitting appropriate key-value pairs.
3. The Hadoop framework sorts and groups the emitted key-value pairs by keys.
4. The Reducer (MaxTemperatureReducer) processes each group of key-value pairs,
extracting the first temperature value associated with each key.
5. The Reducer outputs the key and temperature value as the result.
6. The job writes the output to the specified output path.


// Highest mapper
import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
public class HighestMapper extends MapReduceBase implements Mapper < LongWritable, Text,
  Text, IntWritable > {
    public static final int MISSING = 9999;
    public void map(LongWritable key, Text value, OutputCollector < Text, IntWritable > output,
      Reporter reporter) throws IOException {
      String line = value.toString();
      String year = line.substring(15, 19);
      int temperature;
      if (line.charAt(87) == '+')
        temperature = Integer.parseInt(line.substring(88, 92));
      else
        temperature = Integer.parseInt(line.substring(87, 92));
      String quality = line.substring(92, 93);
      if (temperature != MISSING && quality.matches("[01459]"))
        output.collect(new Text(year), new IntWritable(temperature));
    }
  }


 // Highest reducer
import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
public class HighestReducer extends MapReduceBase implements Reducer < Text, IntWritable,
  Text, IntWritable > {
    public void reduce(Text key, Iterator < IntWritable > values, OutputCollector < Text, IntWritable >
      output, Reporter reporter) throws IOException {
      int max_temp = 0;;
      while (values.hasNext()) {
        int current = values.next().get();
        if (max_temp < current)
          max_temp = current;
      }
      output.collect(key, new IntWritable(max_temp / 10));
    }
}


// Highest driver
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
public class HighestDriver extends Configured implements Tool {
  public int run(String[] args) throws Exception {
    JobConf conf = new JobConf(getConf(), HighestDriver.class);
    conf.setJobName("HighestDriver");
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(IntWritable.class);
    conf.setMapperClass(HighestMapper.class);
    conf.setReducerClass(HighestReducer.class);
    Path inp = new Path(args[0]);
    Path out = new Path(args[1]);
    FileInputFormat.addInputPath(conf, inp);
    FileOutputFormat.setOutputPath(conf, out);
    JobClient.runJob(conf);
    return 0;
  }
  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new HighestDriver(), args);
    System.exit(res);
  }
}






