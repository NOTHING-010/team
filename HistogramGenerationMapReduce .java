import java.io.IOException;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class HistogramGenerationMapReduce extends Configured implements Tool {

  public static class AMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
    private static final Pattern logPattern = Pattern.compile(".*\

\[(.+)\\]

.*");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMMM/yyyy:hh:mm:ss z");
    private static final IntWritable one = new IntWritable(1);

    public void map(Object key, Text value, Context ctx) throws IOException, InterruptedException {
      try {
        Matcher m = logPattern.matcher(value.toString());
        if (m.find()) {
          Date time = sdf.parse(m.group(1));
          Calendar cal = Calendar.getInstance();
          ctx.write(new IntWritable(cal.get(Calendar.HOUR_OF_DAY)), one);
        }
      } catch (ParseException e) {}
    }
  }

  public static class AReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context ctx) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) sum += val.get();
      ctx.write(key, new IntWritable(sum));
    }
  }

  public int run(String[] args) throws Exception {
    if (args.length < 2) return -1;
    Job job = Job.getInstance(getConf(), "HistogramByHour");
    job.setJarByClass(HistogramGenerationMapReduce.class);
    job.setMapperClass(AMapper.class);
    job.setReducerClass(AReducer.class);
    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);
    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    System.exit(ToolRunner.run(new Configuration(), new HistogramGenerationMapReduce(), args));
  }
}
