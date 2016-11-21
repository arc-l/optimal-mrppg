package projects.multipath.ILP;

import gurobi.GRBException;
import projects.multipath.advanced.Graph;
import projects.multipath.advanced.Vertex;

/**
 * Note that there is a 10000 vertex limitation in the current implementation. 
 * @author Jingjin Yu
 *
 */
public class PathPlanner {
	
	public static void printPaths(Graph g, int [][] paths){
		for(int a = 0; a < paths.length; a ++){
			System.out.print("Agent " + a + ":");
			for(int t = 0; t < paths[0].length; t ++){
				System.out.print(" " + t + ":");
				g.vertices[paths[a][t]].printVertex();
			}
			System.out.println();
		}
	}
	
	public static String printPathToString(Graph g, int [][] paths){
		StringBuffer buffer = new StringBuffer();
		for(int a = 0; a < paths.length; a ++){
			buffer.append("Agent " + (a < 9? " " : "") + (a+1) + ":");
			for(int t = 0; t < paths[0].length; t ++){
				buffer.append(" " + t + ":");
				g.vertices[paths[a][t]].printVertex(buffer);
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	protected int[][] planPathsAdvanced(Graph g, int start[], int goal[], boolean solveRelaxed, int extraSteps, boolean setOptimal, double timeLimit) {
		MultiagentGraphSolverGurobiTime solver = new MultiagentGraphSolverGurobiTime();
		try {
			int paths[][] =  solver.solve(g, start, goal, solveRelaxed, setOptimal, extraSteps, timeLimit != -1 , timeLimit);
			if(paths != null){
				if(!isPathSetValid(paths, g, start, goal)){
					return null;
				}
			}
			else{
				if(MultiagentGraphSolverGurobiTime.bDebugInfo){
					System.out.println("Maximum allowed time reached, optimization stopped.");
				}
			}
			return paths;
		} catch (GRBException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected static void printStartAndGoals(Graph g, int start[], int goal[]) {
		System.out.println("Start and goal vertices: " + start.length
				+ " pairs");
		for (int i = 0; i < start.length; i++) {
			Vertex v = g.idVertexMap.get(start[i]);
			v.printVertex();
			System.out.print(" ");
		}
		System.out.println(" ");
		for (int i = 0; i < goal.length; i++) {
			Vertex v = g.idVertexMap.get(goal[i]);
			v.printVertex();
			System.out.print(" ");
		}
		System.out.println(" ");
	}

	public static boolean isPathSetValid(int paths[][], Graph graph,  int starts[], int goals[]){
		// Check start/goal and individual path validity
		for(int t = 0; t < paths[0].length; t ++){
			for(int a = 0; a < paths.length; a++){
				// Check start/goal location
				if(paths[a][0] != starts[a]){
					System.out.println("Start location for agent " + a + " is incorrect in the path set.");
					return false;
				}
				if(paths[a][paths[a].length - 1] != goals[a]){
					System.out.println("Goal location for agent " + a + " is incorrect in the path set.");
					return false;
				}
				
				// Check next vertex is adjacent to the current one
				if(t > 0){
					int lastV = paths[a][t - 1];
					int curV = paths[a][t];
					if(!graph.adjacencySetMap.get(lastV).contains(curV)){
						System.out.println("Path " + a + " is not valid at t = " + (t-1));
						return false;
					}
				}
			}
		}
		
		// Do pair wise meet/head-on collision check
		for(int p = 0; p < paths.length - 1; p ++){
			for(int q = p + 1; q < paths.length; q++){
				for(int t = 0; t < paths[0].length; t ++){
					// Check meet collision
					if(paths[p][t] == paths[q][t]){
						System.out.println("Path " + p + " and " + q + " meet on vertex " + paths[p][t] + " at time step " + t);
						return false;
					}
					// Check head on collision
					if(t > 0 && (paths[p][t] == paths[q][t-1]) && (paths[p][t-1] == paths[q][t])){
						System.out.println("Path " + p + " and " + q + " cross the same edge at time step " + t);
						return false;
					}
				}
			}
		}
		return true;
	}
}
