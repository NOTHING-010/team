import java.io.IOException;
import java.util.regex.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class MsgSizeAggregateMapReduce extends Configured implements Tool {

  public static class AMapper extends Mapper<Object, Text, Text, IntWritable> {
    private static final Pattern logPattern = Pattern.compile(".*\".* (/[^\\s]*) HTTP.*\" \\d+ (\\d+)");
    public void map(Object key, Text value, Context ctx) throws IOException, InterruptedException {
      Matcher m = logPattern.matcher(value.toString());
      if (m.matches()) {
        int size = Integer.parseInt(m.group(2));
        ctx.write(new Text("msgSize"), new IntWritable(size));
      }
    }
  }

  public static class AReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context ctx) throws IOException, InterruptedException {
      int sum = 0, count = 0, min = Integer.MAX_VALUE, max = 0;
      for (IntWritable val : values) {
        int v = val.get();
        sum += v; count++;
        min = Math.min(min, v);
        max = Math.max(max, v);
      }
      ctx.write(new Text("Mean"), new IntWritable(sum / count));
      ctx.write(new Text("Max"), new IntWritable(max));
      ctx.write(new Text("Min"), new IntWritable(min));
    }
  }

  public int run(String[] args) throws Exception {
    if (args.length != 2) return -1;
    Job job = Job.getInstance(getConf(), "MsgSizeAggregator");
    job.setJarByClass(MsgSizeAggregateMapReduce.class);
    job.setMapperClass(AMapper.class);
    job.setReducerClass(AReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    System.exit(ToolRunner.run(new Configuration(), new MsgSizeAggregateMapReduce(), args));
  }
}
