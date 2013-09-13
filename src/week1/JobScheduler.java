import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.Arrays;

public class JobScheduler {
    private static String jobFile = "data/jobs.txt";

    private static void schedule(Job[] jobs, Comparator<Job> cmp) {
	Arrays.sort(jobs,cmp);
    }

    private static long sum(Job[] jobs) {
	long completion = 0;
	long result = 0;
	for (Job job : jobs) {
	    completion += job.length;
	    result += job.weight * completion;
	}
	return result;
    }

    public static void main(String[] args)  {
	System.out.println("hello");
	Path jobsData = Paths.get(jobFile);
	Charset charset = Charset.forName("US-ASCII");
	try (BufferedReader reader = Files.newBufferedReader(jobsData,charset)) {
		String line = reader.readLine();
		int size = Integer.parseInt(line);
		Job[] allJobs = new Job[size];
		for (int i = 0; i < size; i++) {
		    line = reader.readLine();
		    StringTokenizer st = new StringTokenizer(line," ");
		    int weight = Integer.parseInt(st.nextToken());
		    int length = Integer.parseInt(st.nextToken());
		    allJobs[i] = new Job(i,weight,length);
		}

		schedule(allJobs,new DiffComparator());
		System.out.println(sum(allJobs));

		schedule(allJobs,new RatioComparator());
		System.out.println(sum(allJobs));
	    } catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
    }
}


class Job {
    int id;
    int weight;
    int length;
    double ratio;
    int diff;

    public Job(int id, int weight, int length) {
	this.id = id;
	this.weight = weight;
	this.length = length;
	this.ratio = weight * 1.0 / length;
	this.diff = weight - length;
    }

    public String toString() {
	return "id: " + id + ", weight: " + weight + ", length: " + length + ", ratio: " + ratio;
    }
}

class RatioComparator implements Comparator<Job> {
    public int compare(Job o1, Job o2) {
	if (o1.ratio < o2.ratio) return 1;
	else if (o1.ratio == o2.ratio) return 0;
	else return -1;
    }
}

class DiffComparator implements Comparator<Job> {
    public int compare(Job j1, Job j2) {
	if (j1.diff < j2.diff) return 1;
	else if (j1.diff == j2.diff) return j2.weight - j1.weight;
	else return -1;
    }
}
