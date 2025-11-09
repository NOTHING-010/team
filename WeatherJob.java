import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class WeatherJob {

  public static class WeatherMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    public void map(LongWritable key, Text value, Context ctx) throws IOException, InterruptedException {
      try {
        double max = Double.parseDouble(value.toString().substring(103, 108));
        double min = Double.parseDouble(value.toString().substring(111, 116));
        ctx.write(new Text("MAX"), new DoubleWritable(max));
        ctx.write(new Text("MIN"), new DoubleWritable(min));
      } catch (Exception e) {}
    }
  }

  public static class WeatherReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    public void reduce(Text key, Iterable<DoubleWritable> values, Context ctx) throws IOException, InterruptedException {
      double result = key.toString().equals("MAX") ? Double.MIN_VALUE : Double.MAX_VALUE;
      for (DoubleWritable val : values) {
        double v = val.get();
        result = key.toString().equals("MAX") ? Math.max(result, v) : Math.min(result, v);
      }
      ctx.write(key, new DoubleWritable(result));
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 2) System.exit(1);
    Job job = Job.getInstance(new Configuration(), "WeatherJob");
    job.setJarByClass(WeatherJob.class);
    job.setMapperClass(WeatherMapper.class);
    job.setReducerClass(WeatherReducer.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(DoubleWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
