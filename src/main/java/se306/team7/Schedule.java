package se306.team7;

import se306.team7.Digraph.Link;
import se306.team7.Digraph.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Schedule {

    public int _numOfProcessors;
    private Queue<Task> _tasks;
    private HashSet<Node> _nodesInSchedule;
    private int _processorsToScheduleOn;
    private Task _lastTaskScheduled;

    /**
     * Instantiates a PartialSchedule instance.
     * @param numOfProcessors the number of processors available for tasks to be scheduled on
     */
    public Schedule(int numOfProcessors) {
        _numOfProcessors = numOfProcessors;
        _tasks = new LinkedList<Task>();
        _nodesInSchedule = new HashSet<Node>();
		_processorsToScheduleOn = 1;
    }

    public Schedule(Schedule schedule) {
        _numOfProcessors = schedule._numOfProcessors;
        _tasks = new LinkedList<Task>(schedule._tasks);
        _nodesInSchedule = new HashSet<Node>();
		_processorsToScheduleOn = schedule.getNumberOfProcessorsToScheduleOn();
        for (Task t: schedule.getTasks()) {
            _nodesInSchedule.add(t.getNode());
        }
    }

    /**
     * Schedules the specified task on a specified processor.
     * @param processor processor on which the specified task is scheduled
     * @param node task to be scheduled on the specified processor
     */
    public Task scheduleTask(int processor, Node node) {

    	if (processor >= _processorsToScheduleOn - 1) {
			_processorsToScheduleOn++;
		}
    	int startTime = calculateTaskStartTime(processor, node);
        Task newTask = new Task(node, processor,startTime);
        _tasks.add(newTask);
		_lastTaskScheduled = newTask;
        _nodesInSchedule.add(node);
        
        return newTask;
    }

    public Queue<Task> getTasks () {
        return _tasks;
    }

    /**
     * Gets the number of processors
     * @return
     */
    public int getNumberOfProcessors() {
        return _numOfProcessors;
    }

    /**
     * Gets the nodes in the schedule
     * @return
     */
    public HashSet<Node> getNodesInSchedule () {
        return _nodesInSchedule;
    }

	/**
	 * Gets the number of the highest processor with tasks scheduled
	 * @return
	 */
	public int getNumberOfProcessorsToScheduleOn () {
    	return _processorsToScheduleOn;
	}

	/**
	 *
	 * @return
	 */
	public Task getLastTaskScheduled () {
		return _lastTaskScheduled;
	}

    public List<String> scheduleToStringList() {
        ArrayList<String> output = new ArrayList<String>();
        for (Task task : _tasks) {
            Node n = task.getNode();
            String line = "\t" + n.getName() + "\t\t[Weight=" + n.getCost() + ",Start=" + task.getStartTime() +
                    ",Processor=" + task.getProcessor() + "];\n";
            output.add(line);

            List<Link> incomingLinks = n.getIncomingLinks();
            for (Link link : incomingLinks) {
                Node parent = link.getOriginNode();
                Node child = link.getDestinationNode();
                int transferCost = link.getTransferCost();
                String linkString = "\t" + parent.getName() + " -> " + child.getName() + "\t[Weight=" + transferCost + "];\n";
                output.add(linkString);
            }
        }

        output.add("Final time: " + endTime());
        return output;
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

	private int precedenceConstraint(Node node, int processor) {
		int startTime = 0;

		for (Link incomingLink : node.getIncomingLinks()) {
			for (Task task : _tasks) {

				// Find tasks that are on node's incoming links
				if (incomingLink.getOriginNode() == task.getNode()) {
					if (task.getProcessor() == processor) {
						// If preceding task on same processor don't use transfer cost
						startTime = Math.max(startTime, task.getEndTime());
					} else {
						// If preceding task on different process then do use transfer cost
						startTime = Math.max(startTime, task.getEndTime() + incomingLink.getTransferCost());
					}
				}
			}
		}

		return startTime;
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
    		
    		int latestParentEndTime = 0;
    		
    		// check the latest scheduled parent node
    		for (Task task : _tasks){
    			if (parentNodes.contains(task.getNode())){
    				
    				int parentEndTime = task.getEndTime();
    				
    				if (task.getProcessor() != processor){
    					
    					for (Link link : incomingLinks){
    		    			if (link.getOriginNode().equals(task.getNode())){
    	    					parentEndTime = parentEndTime + link.getTransferCost();
    		    				break;
    		    			}
    		    		}
    		    		    	
    	    		}
    				
    				if ( parentEndTime > latestParentEndTime){
    					
    					latestParentEndTime =  parentEndTime ;
    				}
    			}
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
