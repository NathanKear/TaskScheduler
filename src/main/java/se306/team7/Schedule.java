package se306.team7;

import se306.team7.Digraph.Link;
import se306.team7.Digraph.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schedule implements Comparable {

    public int _numOfProcessors;
    private Queue<Task> _tasks;
    private int _estimatedCost;

    /**
     * Instantiates a PartialSchedule instance.
     * @param numOfProcessors the number of processors available for tasks to be scheduled on
     */
    public Schedule(int numOfProcessors, int estimatedCost) {
        _numOfProcessors = numOfProcessors;
        _tasks = new LinkedList<Task>();
        _estimatedCost = estimatedCost;
    }

    /**
     * Schedules the specified task on a specified processor.
     * @param processor processor on which the specified task is scheduled
     * @param node task to be scheduled on the specified processor
     */
    public Task scheduleTask(int processor, Node node) {
    	
    	int startTime = calculateTaskStartTime(processor, node);
        Task newTask = new Task(node, processor,startTime);
        _tasks.add(newTask);
        
        return newTask;
    }

    public Queue<Task> getTasks () {
        return _tasks;
    }

    public int compareTo(Object o) {
        if(o == null) {
            throw new NullPointerException();
        }

        if(!o.getClass().equals(Schedule.class)){
           throw new IllegalArgumentException();
        }

        Schedule otherSchedule = (Schedule)(o);

       return  _estimatedCost - otherSchedule._estimatedCost;
    }

	/**
	 * Get the end time of the final task in the schedule, i.e. how long the entire schedule takes to run
	 * @return
	 */
	public int endTime() {
		int endTime = 0;

		// Get latest end time
		for (Task task : getTasks()) {
			endTime = Math.max(endTime, task.getEndTime());
		}

		return endTime;
	}
    
    private int calculateTaskStartTime(int processor, Node node){
    	int startTime;
    	if (_tasks.isEmpty()){
    		startTime = 0;
    	}else {
    		
    		List<Node> parentNodes = new ArrayList<Node>();
    		List<Link> incomingLinks = node.getIncomingLinks();
    		
    		for (Link link : incomingLinks){
    			parentNodes.add(link.getOriginNode());
    		}
    		
    		Node latestParent = null;
    		int latestParentEndTime = 0;
    		int latestParentProcessor = 0;
    		
    		
    		// check the latest scheduled parent node
    		for (Task task : _tasks){
    			if (parentNodes.contains(task.getNode())){
    				
    				if ( task.getEndTime() > latestParentEndTime){
    					
    					latestParentEndTime =  task.getEndTime() ;
    					latestParent = task.getNode();
    					latestParentProcessor = task.getProcessor();
    				}
    			}
    		}
    		
    		int transferCostFromLatestParent = 0;
    		for (Link link : incomingLinks){
    			if (link.getOriginNode().equals(latestParent)){
    				transferCostFromLatestParent = link.getTransferCost();
    				break;
    			}
    		}
    		    		
    		if (latestParentProcessor != processor){
    			latestParentEndTime = latestParentEndTime + transferCostFromLatestParent;
    		}
    		
    		// latest node end time on the specified processor
    		int processorEndTime = 0;
    		for (Task task : _tasks){
    			if ((task.getProcessor() == processor) && (task.getEndTime() > processorEndTime)){
    				processorEndTime = task.getEndTime();
    			}
    		}
    		
    		startTime = Math.max(latestParentEndTime, processorEndTime);
    	}
    	return startTime;
    }
}
