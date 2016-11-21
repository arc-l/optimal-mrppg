package projects.multipath.ILP;

import java.io.FileWriter;
import java.util.SortedSet;
import java.util.TreeSet;

import projects.multipath.advanced.Graph;
import projects.multipath.advanced.Problem;

public class Main {

	static String basePath = "C:\\temp"; 
	public static void main(String argv[]) {
		int option = (argv.length == 0)?1:Integer.parseInt(argv[0]);
		MultiagentGraphSolverGurobiTime.bDebugInfo = false;
		MultiagentGraphSolverGurobiTime.bPrintPaths = true;
		Problem p = null;
		switch(option){
		case 0:
			run20x15PerformanceMetric();
			break;
		case 1:
			System.out.println("Solving a randomly generated 16-puzzle, generally solvable in 60 seconds on an intel dual core x64 machine.\n");
			p = Problem.createN2Puzzle(4);
			PathPlanner.printStartAndGoals(p.graph, p.sg[0], p.sg[1]);
			System.out.println("\nSolution path set:\n");
			solveProblem(p, true, -1);
			break;
		case 2:
			System.out.println("Solving a randomly generated multi-agent path planning problem on a 15x15 grid with 20% obstacles and 15 agents, with 60 second time limit.\n");
			p = Problem.createGridProblem(15, 15, 0.2, 15);
			System.out.println("Printing adjacency list of the graph:");
			p.graph.printGraphAsAdjacenyList();
			System.out.println("\nSolution path set:\n");
			solveProblem(p, true, 60);
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
			solveProblem(p, true, -1);
			break;
		case 4:
			System.out.println("Solving problem from file: " + argv[1] + " with 1000 seconds limit.\n");
			p = Problem.readFromFile(argv[1]);
			PathPlanner.printStartAndGoals(p.graph, p.sg[0], p.sg[1]);
			System.out.println("\n");
			solveProblem(p, true, 1000);
			break;
		case 5:
			p = Problem.createGridProblem(16, 16, 0, 60);
			PathPlanner.printStartAndGoals(p.graph, p.sg[0], p.sg[1]);
			solveProblem(p, true, 7200);
		}
	}
	
	/**
	 * Solve a problem
	 * @param p
	 * @param timeLimit
	 * @return
	 */
	private static long[] solveProblem(Problem p, boolean solveLP, double timeLimit){
		// Solve the problem
		PathPlanner ms = new PathPlanner();
		long time = System.currentTimeMillis();
		int[][] paths = ms.planPathsAdvanced(p.graph, p.sg[0], p.sg[1], solveLP, 0, true, timeLimit);
		time = System.currentTimeMillis() - time;
		int makespanLb = PathFinder.getMakespanLowerBound(p.graph, p.sg[0], p.sg[1]);
		if(paths != null){
			return new long[]{paths[0].length - 1, time, PathFinder.getTotalDistance(paths), makespanLb - 1};
		}
		else{
			return null;
		}
	}
	
	/**
	 * Solve a n^2 puzzle
	 * @param p
	 * @param timeLimit
	 * @return
	 */
	private static long[] solvePuzzleProblem(int instance, Problem p, double timeLimit){
		// Solve the problem
		long time = System.currentTimeMillis();
		int paths[][] = null;
		paths = PuzzleSolver.solve(p, timeLimit);
		time = System.currentTimeMillis() - time;
		int makespanLb = PathFinder.getMakespanLowerBound(p.graph, p.sg[0], p.sg[1]);
		if(paths != null){
			writePuzzleResult(instance, time, p.graph, paths);
			PathPlanner.printPaths(p.graph, paths);
			return new long[]{paths[0].length - 1, time, PathFinder.getTotalDistance(paths), makespanLb - 1};
		}
		return null;
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
	static int makeSpanTotal = 0;
	
	private static void resetStats(String test){
		currentTest = test;
		subTotal = 0;
		pathLengths = 0;
		solvedInstances = 0;
		successCount = 0;
		makeSpanTotal = 0;
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
			makeSpanTotal += stat[3];
			System.out.println("Total time spent: " + totalTime + " seconds.\n");
		}
	}
	
	private static void writeStats(){
		try {
			Double[] ft = finishingTimeSet.toArray(new Double[0]);
			String outString = currentTest + "Total success: " + successCount + ", ave time: " + subTotal/successCount + ", median:" + ft[ft.length/2] + ", min: " + ft[0] + ", max: " + ft[ft.length - 1] + ", ave time steps: " + (pathLengths*1./successCount) + ", min possible: " + (makeSpanTotal*1./successCount) +"\n";
			FileWriter fw = new FileWriter(basePath + "\\n2puzzle-result.txt", true);
			fw.append(outString);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void writePuzzleStats(int instance, double time){
		try {
			String outString = "Instance: " + instance + ", time: " + time + "\n";
			FileWriter fw = new FileWriter(basePath + "\\n2puzzle-result.txt", true);
			fw.append(outString);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void writePuzzleResult(int instance, double time, Graph g, int[][] paths){
		try {
			String outString = "Instance: " + (instance  + 1) + ", time: " + time/1000 + "\n";
			FileWriter fw = new FileWriter(basePath + "\\25puzzle.txt", true);
			fw.append(outString);
			fw.append(PathPlanner.printPathToString(g, paths));
			fw.append("\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void runN2PuzzlePerformanceMetric(){
		try{
			totalTime = 0;
			for(int n = 4; n <= 4; n ++){
				resetStats("" + n*n + "-puzzle, 100 runs. ");
				for(int i = 0; i < 100 ; i ++){
					Problem p = Problem.readFromFile(basePath + "\\n2puzzle\\" + (n*n) + "-puzzle-" + (10001 + i) + ".txt");
					System.out.println("Working on " +  + (n*n) + "-puzzle-" + (10001 + i) + ".txt");
					long stat[] = solvePuzzleProblem(i, p, -1);
					if(stat != null){
						processStats(stat);
						writePuzzleStats(i + 1, (stat[1]/1000.));
					}
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
		for(int obs = 5; obs <= 30; obs=obs+5){
			for(int n = 10; n < 60; n=n+10){
				resetStats("20x15, " + obs + "% obstacles, " + n + " agents, 10 runs. ");
				for(int i = 0; i < 10; i ++){
					System.out.println("Working on " + "20x15 grid performance testing, problem: " + n + "-agt-"+ (obs) + "-pct-obs-" + (1001 + i) + ".txt");
					Problem p = Problem.readFromFile(basePath + "\\20x15-grid\\" + n + "-agt-"+ (obs) + "-pct-obs-" + (1001 + i) + ".txt");
					long stat[] = solveProblem(p, false, 1000);
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
				Problem p = Problem.readFromFile(basePath + "\\32x32-grid\\20-pct-obs-" + a + "-agts-" + (1001 + i) + ".txt");
				System.out.println("Working on " + "32x32 grid performance testing, problem: 20-pct-obs-" + a + "-agts-" + (1001 + i) + ".txt");
				long stat[] = solveProblem(p, true, 3600);
				processStats(stat);
			}
			writeStats();
		}
	}
	
}
