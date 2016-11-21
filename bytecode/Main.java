package projects.multipath.advanced;

import java.io.FileWriter;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {

	public static void main(String argv[]) {
		int option = (argv.length == 0)?1:Integer.parseInt(argv[0]);
		MultiagentGraphSolverGurobiTime.bDebugInfo = false;
		Problem p = null;
		switch(option){
		case 1:
			System.out.println("Solving a randomly generated 16-puzzle, generally solvable in 60 seconds on an intel dual core 64bit machine.\n");
			p = Problem.createN2Puzzle(4);
			PathPlanner.printStartAndGoals(p.graph, p.sg[0], p.sg[1]);
			System.out.println("\nSolution path set:\n");
			solveProblem(p, -1);
			break;
		case 2:
			System.out.println("Solving a randomly generated multi-agent path planning problem on a 15x15 grid with 20% obstacles and 15 agents, with 60 second time limit.\n");
			p = Problem.createGridProblem(15, 15, 0.2, 15);
			System.out.println("Printing adjacency list of the graph:");
			p.graph.printGraphAsAdjacenyList();
			System.out.println("\nSolution path set:\n");
			solveProblem(p, 60);
			break;
		case 3:
			System.out.println("Create a simple graph manually and then solve a problem on this graph.\n");
			Graph g = new Graph();
			// Note that vertex ids must start from 0 and are consecutive. The resulting
			// graph must be connected.
			g.addVertex(0, new int[]{1});
			g.addVertex(1, new int[]{0, 2, 3});
			g.addVertex(2, new int[]{1});
			g.addVertex(3, new int[]{1});
			g.finishBuildingGraph();
			System.out.println("Adjacency list:");
			g.printGraphAsAdjacenyList();
			System.out.println("\nSolving the problem, solution path set: ");
			p = new Problem();
			p.graph = g;
			p.sg = new int[][]{{0, 3}, {3, 0}};
			solveProblem(p, -1);
			break;
		case 4:
			System.out.println("Solving problem from file: " + argv[1] + " with 1000 seconds limit.\n");
			p = Problem.readFromFile(argv[1]);
			PathPlanner.printStartAndGoals(p.graph, p.sg[0], p.sg[1]);
			System.out.println("\n");
			solveProblem(p, 1000);
			break;
		}
	}
	
	
	/**
	 * Solve a problem
	 * @param p
	 * @param timeLimit
	 * @return
	 */
	private static long[] solveProblem(Problem p, double timeLimit){
		// Solve the problem
		PathPlanner ms = new PathPlanner();
		long time = System.currentTimeMillis();
		int[][] paths = ms.planPathsAdvanced(p.graph, p.sg[0], p.sg[1], true, timeLimit);
		time = System.currentTimeMillis() - time;
		if(paths != null){
			return new long[]{paths[0].length, time, PathFinder.getTotalDistance(paths)};
		}
		else{
			return null;
		}
	}
	
	private static long[] solveProblemWithHeuristic(Problem p){
		PathFinder.bHeuristic = true;
		// Solve the problem
		PathPlanner ms = new PathPlanner();
		long time = System.currentTimeMillis();
		PathPlanner.printStartAndGoals(p.graph, p.sg[0], p.sg[1]);
		int[][] paths = ms.planHeuristicPaths(p.graph, p.sg[0], p.sg[1]);
		time = System.currentTimeMillis() - time;
		if(paths != null){
			int goalsReached = PathPlanner.numberOfGoalsReached(paths, p.sg[1]);
			// PathPlanner.printPaths(p.graph, paths);
			if(!PathPlanner.isPathSetValid(paths, p.graph, p.sg[0], p.sg[1])){
				System.out.println("Path set is INVALID.");
			}
			
			// Find out path lengths
			long length = 0, leastPossibleLength = 0;
			PathFinder.bHeuristic = false;
			Path[] hp = PathFinder.findShortestPaths(p.graph, p.sg[0], p.sg[1]);
			PathFinder.bHeuristic = true;
			for(int a = 0; a < p.sg[0].length; a++){
				leastPossibleLength += (hp[a].vertexList.size() - 1);
				for(int i = 0; i < paths[a].length - 1; i ++){
					if(paths[a][i] != paths[a][i+1]){
						length++;
					}
				}
			}
			
			return new long[]{paths[0].length, time, goalsReached, 
					(goalsReached==p.sg[0].length)?length:0, (goalsReached==p.sg[0].length)?leastPossibleLength:0};
		}
		else{
			return null;
		}
	}
	
	static String currentTest = null;
	static double subTotal = 0;
	static int pathLengths = 0;
	static int solvedInstances = 0;
	static double totalTime = 0;
	static int successCount = 0;
	static long goalsReached = 0;
	static long pathLength = 0;
	static long expPathLength = 0;
	static SortedSet<Double> finishingTimeSet = new TreeSet<Double>(); 
	
	private static void resetStats(String test){
		currentTest = test;
		subTotal = 0;
		pathLengths = 0;
		solvedInstances = 0;
		successCount = 0;
		finishingTimeSet = new TreeSet<Double>(); 
	}
	
	private static void resetHeuristicStats(String test){
		currentTest = test;
		goalsReached = 0;
		subTotal = 0;
		pathLengths = 0;
		solvedInstances = 0;
		successCount = 0;
		pathLength = 0;
		expPathLength = 0;
		finishingTimeSet = new TreeSet<Double>(); 
	}
	
	private static void processStats(long[] stat){
		if(stat != null){
			int len = (int)stat[0];
			long time = stat[1];
			successCount += 1;
			pathLengths += len;
			finishingTimeSet.add(time/1000.0);
			System.out.println("Time spent = " + time/1000.0 + " seconds.");
			subTotal += time/1000.0;
			totalTime += time/1000.0;
			System.out.println("Total time spent: " + totalTime + " seconds.\n");
		}
	}
	
	private static void processHeuristicStats(long[] stat, int n){
		if(stat != null){
			int len = (int)stat[0];
			long time = stat[1];
			pathLengths += len;
			finishingTimeSet.add(time/1000.0);
			goalsReached += stat[2];
			System.out.println("Goals reached = " + goalsReached);
			if(stat[2] == n){successCount += 1;}
			System.out.println("Time spent = " + time/1000.0 + " seconds.");
			subTotal += time/1000.0;
			totalTime += time/1000.0;
			System.out.println("Total time spent: " + totalTime + " seconds.\n");
			pathLength += stat[3];
			expPathLength += stat[4];
		}
	}
	
	private static void writeStats(){
		try {
			Double[] ft = finishingTimeSet.toArray(new Double[0]);
			String outString = currentTest + "Total success: " + successCount + ", ave time: " + subTotal/successCount + ", median:" + ft[ft.length/2] + ", min: " + ft[0] + ", max: " + ft[ft.length - 1] + ", ave time steps: " + pathLengths*1./successCount + "\n";
			FileWriter fw = new FileWriter("Z:\\n2puzzle-result.txt", true);
			fw.append(outString);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void writeHeuristicStats(){
		try {
			Double[] ft = finishingTimeSet.toArray(new Double[0]);
			String outString = currentTest + "Total success: " + successCount + ", goals reached: " + goalsReached + ", ave time: " + subTotal/successCount + ", median:" + ft[ft.length/2] + ", min: " + ft[0] + ", max: " + ft[ft.length - 1] + ", steps: " + pathLength + ", exp: " + expPathLength + ", ratio: " + pathLength*1./expPathLength + "\n";
			FileWriter fw = new FileWriter("Z:\\n2puzzle-result.txt", true);
			fw.append(outString);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void runN2PuzzlePerformanceMetric(){
		try{
			totalTime = 0;
			for(int n = 3; n < 6; n ++){
				resetStats("" + n*n + "-puzzle, 100 runs. ");
				for(int i = 0; i < 100; i++){
					Problem p = Problem.readFromFile("Z:\\n2puzzle\\" + (n*n) + "-puzzle-" + (10001 + i) + ".txt");
					System.out.println("Working on " +  + (n*n) + "-puzzle-" + (10001 + i) + ".txt");
					long stat[] = solveProblem(p, -1);
					processStats(stat);
				}
				writeStats();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected static void run20x15PerformanceMetric(){
		totalTime = 0;
		for(int obs = 5; obs < 35; obs=obs+5){
			for(int n = 10; n < 60; n=n+10){
				resetStats("20x15, " + obs + "% obstacles, " + n + " agents, 10 runs. ");
				for(int i = 0; i < 10; i ++){
					System.out.println("Working on " + "20x15 grid performance testing, problem: " + n + "-agt-"+ (obs) + "-pct-obs-" + (1001 + i) + ".txt");
					Problem p = Problem.readFromFile("Z:\\20x15-grid\\" + n + "-agt-"+ (obs) + "-pct-obs-" + (1001 + i) + ".txt");
					long stat[] = solveProblem(p, 1000);
					processStats(stat);
				}
				writeStats();
			}
		}
	}
	
	protected static void run32x32PerformanceMetric(){
		totalTime = 0;
		for(int a = 10; a < 60; a = a + 10){
			resetStats("32x32, 20% obstacles, " + a + " agents, 10 runs. ");
			for(int i = 0; i < 10; i ++){
				Problem p = Problem.readFromFile("Z:\\32x32-grid\\20-pct-obs-" + a + "-agts-" + (1001 + i) + ".txt");
				System.out.println("Working on " + "32x32 grid performance testing, problem: 20-pct-obs-" + a + "-agts-" + (1001 + i) + ".txt");
				long stat[] = solveProblem(p, 3600);
				processStats(stat);
			}
			writeStats();
		}
	}
	
	protected static void run32x32PerformanceMetricHeuristic(){
		totalTime = 0;
		for(int n = 25; n < 175; n = n + 25){
			resetHeuristicStats("32x32, 20% obstacles, "+ n + " agents, 100 runs. ");
			for(int i = 0; i < 100; i ++){
				System.out.println("Working on " + "32x32 grid performance testing, problem: 20-pct-obs-" + (1001 + i) + ".txt");
				Problem p = Problem.readFromFile("Z:\\32x32-grid-" + n + "\\20-pct-obs-" + (1001 + i) + ".txt");
				long stat[] = solveProblemWithHeuristic(p);
				processHeuristicStats(stat, n);
			}
			writeHeuristicStats();
		}
	}
	
}
