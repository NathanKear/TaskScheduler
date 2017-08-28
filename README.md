# TaskScheduler
Softeng306 project 1

Project overview found in wiki

Algorithm used (A* DFS Hybrid)

Project Management:
Task board created in Taiga
Found at:
https://tree.taiga.io/project/ccar845-task-scheduler/kanban

Packages:
src:
+ se306.team7 // Main package, includes main class and schedule stuff
    + Algorithm // Algorithm implementation including cost estimating
    + Digraph // Digraph elements, reading and parsing, as well as links and nodes
    + utility // Utility classes for IO and timing
    + visual // Code related to creating and updating JavaFX algorithm visualisation

test:
+ se306.team7 // Unit test code
    + Digraph // Unit tests for classes in se306.team7.Digraph packages
    + PerformanceTests // For testing speed of algorithm against different inputs
    + utility // Unit tests for classes in se306.team7.utility packages

Build:
Build as runnable jar, with TaskSchedule.main() as program entry point.
Include all libraries in the jar file.

Run:
As per instructions e.g.

java −jar scheduler.jar INPUT.dot P [OPTION]
    INPUT.dot a task graph with integer weights in dot format
    P number of processors to schedule the INPUT graph on
    Optional :
    − p N use N cores for execution in parallel (default is sequential)
    − v visualise the search
    − o OUPUT output file is named OUTPUT (default is INPUT−output.dot)


