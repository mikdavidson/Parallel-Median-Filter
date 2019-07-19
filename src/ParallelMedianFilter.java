import java.util.concurrent.RecursiveAction;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
@SuppressWarnings("serial")
class ParallelMedianFilter extends RecursiveAction{
	final int filter;
	int max;
	int min;
	final int border;
	List<Double> list = new ArrayList<Double>();
	
	public ParallelMedianFilter(List<Double> list, int min, int max, int filter){
		this.max = max;
		this.min = min;
		this.list = list;
		this.filter = filter;
		border = (filter-1)/2;
	}

	public void compute(){
		
		if((max-min)<200000){
			for(int i=min; i<max;i++){
				List<Double> temp = new ArrayList<Double>();
				if(i<border || i>(list.size()-border-1)){
					MedianFilter.output[i] = list.get(i);
				}
				else{
					for(int j=(i-border); j<border+i+1;j++){
						temp.add(list.get(j));
					}
					Collections.sort(temp);
					MedianFilter.output[i] = temp.get((temp.size()-1)/2);
				}
			}
		}

		else{
			ParallelMedianFilter left = new ParallelMedianFilter(list, min, (max+min)/2, filter);
			ParallelMedianFilter right = new ParallelMedianFilter(list, (max+min)/2, max, filter);
			left.fork();
			right.compute();
			left.join();
		}
	}
}

