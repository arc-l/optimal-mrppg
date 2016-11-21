package projects.multipath.advanced;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class Problem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Graph graph = null;
	public int sg[][] = null;
	
	public  Map<Integer, Integer> vidNewIdMap = null;
	public  Map<Integer, Integer> newIdVidMap = null;

	public Problem() {}
	
	public Problem(Graph graph, int[][] sg) {
		super();
		this.graph = graph;
		this.sg = sg;
	}
	
	public static void writeToFile(Problem p, String fileName){
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(p.graph);
			out.writeObject(p.sg);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Problem readFromFile(String fileName){
		ObjectInputStream in;
		Problem p = new Problem();
		try {
			in = new ObjectInputStream(new FileInputStream(fileName));
			p.graph = (Graph)(in.readObject());
			p.sg = (int[][])(in.readObject());
			in.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	/**
	 * Create a col x row grid problem with fracObs obstacles and n agents
	 * @param col
	 * @param row
	 * @param fracObs
	 * @param n
	 * @return
	 */
	public static Problem createGridProblem(int col, int row, double fracObs, int n){
		Problem p = new Problem();
		p.graph = Graph.createGeneric2DGridGraphWithHoles(col, row, fracObs);
		p.sg = Graph.getRandomStartGoal(p.graph, n);
		p.graph = Graph.convertGraph(p.graph, p.sg[0], p.sg[1]);
		return p;
	}

	/**
	 * Create a col x row grid problem with fracObs obstacles and n agents
	 * @param col
	 * @param row
	 * @param fracObs
	 * @param n
	 * @return
	 */
	public static Problem createGridProblem8Connected(int col, int row, double fracObs, int n){
		Problem p = new Problem();
		p.graph = Graph.createGeneric2DGridGraphWithHoles8Connected(col, row, fracObs);
		p.sg = Graph.getRandomStartGoal(p.graph, n);
		p.graph = Graph.convertGraph(p.graph, p.sg[0], p.sg[1]);
		return p;
	}

	/**
	 * Create a n^-puzzle
	 * @param n
	 * @return
	 */
	public static Problem createN2Puzzle(int n){
		Problem p = new Problem();
		p.graph = Graph.create2DGridGraph(n, n, true);
		p.sg = Graph.getRandomStartGoalMany(p.graph, n*n);
		return p;
	}
	
	public static void main(String argv[]){
		// Allow the files to close properly
		try {
			// createCrowdedHardProblems();
			create32x32Problems1008Connected(60);			
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void createN2Puzzles(){
		for(int i = 0; i < 100; i++){
			for(int n = 3; n < 6; n ++){
				Problem p = Problem.createN2Puzzle(n);
				Problem.writeToFile(p, "Z:\\n2puzzle\\" + (n*n) + "-puzzle-" + (10001 + i) + ".txt");
			}
		}
		
	}
	
	protected static void create20x15PerformanceTestingProblems(){
		for(int obs = 5; obs < 35; obs=obs+5){
			for(int n = 10; n < 60; n=n+10){
				for(int i = 0; i < 10; i ++){
					Problem p = Problem.createGridProblem(20, 15, obs/100., n);
					Problem.writeToFile(p, "Z:\\20x15-grid\\" + n + "-agt-"+ (obs) + "-pct-obs-" + (1001 + i) + ".txt");
				}
			}
		}
	}
	
	protected static void create32x32Problems(){
		for(int a = 10; a < 60; a = a + 10){
			for(int i = 0; i < 10; i ++){
				Problem p = Problem.createGridProblem(32, 32, 0.2, a);
				Problem.writeToFile(p, "Z:\\32x32-grid\\20-pct-obs-" + a + "-agts-" + (1001 + i) + ".txt");
			}
		}
	}
	
	protected static void create32x32Problems100(int agent){
		for(int i = 0; i < 100; i ++){
			Problem p = Problem.createGridProblem(32, 32, 0.2, agent);
			Problem.writeToFile(p, "C:\\TEMP\\32x32-grid-" + agent + "\\20-pct-obs-" + (1001 + i) + ".txt");
		}
	}

	protected static void create32x32Problems1008Connected(int agent){
		for(int i = 0; i < 100; i ++){
			Problem p = Problem.createGridProblem8Connected(32, 32, 0.2, agent);
			Problem.writeToFile(p, "Z:\\32x32-grid-8c\\20-pct-obs-" + agent + "-agts-" + (1001 + i) + ".txt");
		}
	}

	protected static void createCrowdedHardProblems(){
		for(int i = 0; i < 10; i ++){
			for(int a = 10; a < 61; a = a + 10){
				Problem p = Problem.createGridProblem(8, 8, 0, a);
				Problem.writeToFile(p, "Z:\\8x8-grid\\" + a + "-agts-" + (1001 + i) + ".txt");
			}
		}
		for(int i = 0; i < 10; i ++){
			for(int a = 10; a < 251; a = a + 10){
				Problem p = Problem.createGridProblem(16, 16, 0, a);
				Problem.writeToFile(p, "Z:\\16x16-grid\\" + a + "-agts-" + (1001 + i) + ".txt");
			}
		}
	}

	protected static void createCrowdedHardProblems8Connected(){
		for(int i = 0; i < 10; i ++){
			for(int a = 10; a < 61; a = a + 10){
				Problem p = Problem.createGridProblem8Connected(8, 8, 0, a);
				Problem.writeToFile(p, "Z:\\8x8-grid-8c\\" + a + "-agts-" + (1001 + i) + ".txt");
			}
		}
		for(int i = 0; i < 10; i ++){
			for(int a = 10; a < 251; a = a + 10){
				Problem p = Problem.createGridProblem8Connected(16, 16, 0, a);
				Problem.writeToFile(p, "Z:\\16x16-grid-8c\\" + a + "-agts-" + (1001 + i) + ".txt");
			}
		}
	}

	protected static Problem getA9Puzzle(){
		Graph g = new Graph();
		g.addVertex(0, new int[]{0, 1, 3});
		g.addVertex(1, new int[]{0, 1, 2, 4});
		g.addVertex(2, new int[]{1, 2, 5});
		g.addVertex(3, new int[]{0, 3, 4, 6});
		g.addVertex(4, new int[]{1, 3, 4, 5, 7});
		g.addVertex(5, new int[]{2, 4, 5, 8});
		g.addVertex(6, new int[]{3, 6, 7});
		g.addVertex(7, new int[]{4, 6, 7, 8});
		g.addVertex(8, new int[]{5, 7, 8});
		g.finishBuildingGraph();
		int sg[][] = new int[][]{{3, 0, 2, 8, 1, 4, 7, 5, 6},{0, 1, 2, 3, 4, 5, 6, 7, 8}};
		
		Problem p = new Problem();
		p.graph = g;
		p.sg = sg;
		return p;
	}
	
	public static Problem getLongStraightWithOneGarageProblem(){
		Graph g = new Graph();
		g.addVertex(0, new int[]{0, 1});
		g.addVertex(1, new int[]{0, 1, 2});
		g.addVertex(2, new int[]{1, 2, 3});
		g.addVertex(3, new int[]{2, 3, 4});
		g.addVertex(4, new int[]{3, 4, 5, 9});
		g.addVertex(5, new int[]{4, 5, 6});
		g.addVertex(6, new int[]{5, 6, 7});
		g.addVertex(7, new int[]{6, 7, 8});
		g.addVertex(8, new int[]{7, 8});
		g.addVertex(9, new int[]{4, 9});
		g.finishBuildingGraph();
		int sg[][] = new int[][]{{0, 8},{8, 0}};
		
		Problem p = new Problem();
		p.graph = g;
		p.sg = sg;
		return p;
	}
}
