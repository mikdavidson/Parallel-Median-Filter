import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;
import java.io.FileReader;
public class MedianFilter{
	
	static long startTime = 0;
	static Double[] output = new Double[0];
	static final ForkJoinPool fjPool = new ForkJoinPool();
	
	//Record current time
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	//Return time taken to execute block of code
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	//MedianFilter static method creating ForkJoinPool
	static void medianFilter(List<Double> list, int min, int max, int filter){
	  fjPool.invoke(new ParallelMedianFilter(list,  min,  max, filter));
	}

	public static void main(String[] args) throws FileNotFoundException{
		List<Double> list = new ArrayList<Double>();

		String inputFile = args[0];
		int filter = Integer.parseInt(args[1]);
		String outputFile = args[2];
		
		//Reading input from file
		Scanner scan = new Scanner(new FileReader(inputFile));
		int arraySize = scan.nextInt();
		scan.nextLine();
		while(scan.hasNext()){
			String line = scan.nextLine();
			String[] data = line.split("\\s+");
			list.add(Double.parseDouble(data[1]));
		}
		scan.close();
		
		Double[] sequentialOutput = new Double[list.size()];
		output = new Double[list.size()];
		int border = (filter-1)/2;
		
		//Timing parallel median filter method and garbage collection
		System.gc();
		tick();
		medianFilter(list, 0, arraySize, filter);
		float time = tock();
		System.out.println("Parallel median filtering took: " + time + "s");
		
		try{
		    PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
		    writer.println(arraySize);
		    for(int i=0; i<arraySize; i++){
		    	writer.println(i+ " " + output[i]);
		    }
		    writer.close();
		} catch (IOException e) {
			
		}
		
		//Timing sequential median filter method and garbage collection
		System.gc();
		tick();
		for(int i =0; i<list.size(); i++){
			List<Double> temp = new ArrayList<Double>();
			if(i<border || i>(list.size()-border-1)){
				sequentialOutput[i] = list.get(i);
			}
			else{
				for(int j=i-border; j<border+i+1;j++){
					temp.add(list.get(j));
				}
				Collections.sort(temp);
				sequentialOutput[i] = temp.get((temp.size()-1)/2);
			}
			temp.clear();
		}
		
		float sTime = tock();
		System.out.println("Sequential median filtering took: " + sTime+"s");
		System.out.println("Speedup: " + sTime/time);
	}
}

