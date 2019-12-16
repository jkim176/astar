import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class EightPuzzle {
   Graph graph;
   int manhattanDistance;
   
   public EightPuzzle(Graph graph) {
      this.graph = graph;
      this.manhattanDistance = EightPuzzle.getManhattanDistance(graph);
   }
   
   public static int getManhattanDistance(Graph graph) {
      Map<Vertex, List<Vertex>> adjacencyList = graph.getAdjacencyList();
      int newManhattanDistance = 0;
      Set<Vertex> vertices = new HashSet<>();
      vertices.addAll(adjacencyList.keySet());    // Iterate all vertices in graph
      Iterator<Vertex> verticesIt = vertices.iterator();
      
      while(verticesIt.hasNext()) {    // calculate partial Manhattan distances for all vertices
         Vertex cur = verticesIt.next();
         if(cur.getTile() == 0) {      // do not count BLANK(0) Tile in Manhattan Distance 
            continue;
         }
         if(cur.getTile() == cur.getGoal()) {      // Tile is in goal state      
            continue;
         }
         // current Vertex is not 0, and not in goal state
         Set<Vertex> frontier = new HashSet<>();
         Set<Vertex> explored = new HashSet<>();
         explored.add(cur);
         frontier.addAll(adjacencyList.get(cur));   // add all edges connected to current Vertex        
         int partialDistance = 0;    // partial manhattan distance
         while(!frontier.isEmpty()) {
            Set<Vertex> nextDepthFrontier = new HashSet<>();   // holds next depth of frontier
            Iterator<Vertex> frontierIt = frontier.iterator(); // frontier.iterator only holds one depth at a time
            while(frontierIt.hasNext()) {    // iterate through graph one depth at a time
               Vertex currentEdge = frontierIt.next();
               if(currentEdge.getGoal() == cur.getTile()) {
                  nextDepthFrontier.clear();
                  break;
               } else {
                  explored.add(currentEdge);
                  for(Vertex v: adjacencyList.get(currentEdge)) {    // add current edge adjacent vertices for next depth
                     if(!explored.contains(v) && !frontier.contains(v)) {
                        nextDepthFrontier.add(v);
                     }
                  }
               }
            }
            partialDistance++;
            frontier = nextDepthFrontier;
         }
         newManhattanDistance += partialDistance;
         partialDistance = 0;
      }
      return newManhattanDistance;
   }
   
   public GraphState aStar() {
      Map<Vertex, List<Vertex>> adjacencyList = graph.getAdjacencyList();
      Vertex blank = null;
      for(Vertex v: adjacencyList.keySet()) {      // find blank Tile
         if(v.getTile() == 0)
            blank = v;
      }
      if(blank == null) {
         System.out.println("No vertex with Tile = 0");
         return null;
      }
      Set<Vertex> initializedFrontier = new HashSet<>();
      initializedFrontier.addAll(adjacencyList.get(blank));    // stores initial frontier
      TreeMap<Integer, Set<GraphState>> frontier = new TreeMap<>();      // stores frontier, the frontier after initial frontier   
      for(Vertex v: initializedFrontier) {     // create GraphState for ONLY initial paths
         int depth = 1;
         Graph newGraph = new Graph(adjacencyList);
         Vertex source = null;      // blank tile
         Vertex target = null;      // frontier tile
         for(Vertex vertex: newGraph.getAdjacencyList().keySet()) {  // find source and target Vertex in new graph
            if(vertex.equals(v))
               target = vertex;
            if(vertex.equals(blank))
               source = vertex;
         }
         if(source == null || target == null)
            break;
         int temp = target.getTile();     // swap Tiles
         target.setTile(source.getTile());
         source.setTile(temp);
         int manhattanDistance = EightPuzzle.getManhattanDistance(newGraph);      // f = g + h
         GraphState state = new GraphState(newGraph, depth, manhattanDistance, source, target);
         //System.out.println("f(x) = " + (state.depth + state.manhattanDistance));///////////////////////////////////////////////////////////////////
         //EightPuzzle.printGraph(state.graph);/////////////////////////////////////////////////////////////////DEBUG//////////////////////////////////
         int f = depth + manhattanDistance;
         if(frontier.get(f) == null) {
            frontier.put(f, new HashSet<>());
         }
         frontier.get(f).add(state);
         if(state.manhattanDistance == 0)    // return state if goal state reached in initial Frontier
            return state;
      }
      // choose smallest f from frontier; add all (f -> GraphState of connected Vertices) to Frontier
      GraphState goalState = null;
      boolean isGoal = false;
      do {
         while(frontier.get(frontier.firstKey()).isEmpty()) {     // remove all mappings with empty Set<GraphState>
            frontier.remove(frontier.firstKey());
         }
         Set<GraphState> minF = frontier.get(frontier.firstKey());
         Iterator<GraphState> minFIt = minF.iterator();
         GraphState current = minFIt.next();    // min f, first GraphState
         minFIt.remove();
         List<Vertex> adjacentEdges = current.graph.getAdjacencyList().get(current.blank);   // get adjacent edges to BLANK Tile for expansion
         for(Vertex v: adjacentEdges) {      // expand; create new GraphState for each adjacent Vertex that is not previous move
            if(!v.equals(current.previous)) {
               Graph newGraph = new Graph(current.graph.getAdjacencyList());
               // swap Blank Tile with adjacent vertex, v
               Vertex source = null;      // blank tile
               Vertex target = null;      // frontier tile
               for(Vertex vertex: newGraph.getAdjacencyList().keySet()) {  // find source and target Vertex in new graph
                  if(vertex.equals(v))
                     target = vertex;
                  if(vertex.getTile() == 0)
                     source = vertex;
               }
               if(source == null || target == null)
                  break;
               int temp = target.getTile();     // swap Tiles
               target.setTile(source.getTile());
               source.setTile(temp);
               int manhattanDistance = EightPuzzle.getManhattanDistance(newGraph);
               GraphState newGraphState = new GraphState(newGraph, current.depth + 1, manhattanDistance, source, target, current.path);
               //System.out.println("f(x) = " + (newGraphState.depth + newGraphState.manhattanDistance));///////////////////////////////////////////////////////////////////////////
               //EightPuzzle.printGraph(newGraphState.graph);/////////////////////////////////////////////////////////////////////DEBUG///////////////////////////////
               //add graphstate to set
               int f = newGraphState.depth + manhattanDistance;
               if(frontier.get(f) == null) {
                  frontier.put(f, new HashSet<>());
               }
               frontier.get(f).add(newGraphState);
               if(newGraphState.manhattanDistance == 0) {   // return state if goal state reached in initial Frontier
                  isGoal = true;
                  goalState = newGraphState;
                  return newGraphState;
               }
            }  
         }
      } while(!isGoal);
      return goalState;
   }
   
   public static void printGraph(Graph graph) {
      Map<Vertex, List<Vertex>> adjacencyList = graph.getAdjacencyList();
      int i = 1;
      while(i <= adjacencyList.size()) {
         for(Vertex v: adjacencyList.keySet()) {
            if(v.getLabel() == i - 1) {
               System.out.print("Vertex" + v.getLabel() + "(" + v.getTile() + ")\t\t");
            }
         }
         if(i % 3 == 0) {
            System.out.println();
         }
         i++;
      }
      System.out.println();
   }
   
   public static void printGraph2(Graph graph) {
      Map<Vertex, List<Vertex>> adjacencyList = graph.getAdjacencyList();
      for(int i = 0; i < adjacencyList.size(); i++) {
         Vertex cur = null;
         for(Vertex v: adjacencyList.keySet()) {
            if(v.getLabel() == i)
               cur = v;
         }
         System.out.print("Vertex" + cur.getLabel() + "(Tile: " + cur.getTile() + ",Goal: " + cur.getGoal() + ") -> ");
         for(Vertex v: adjacencyList.get(new Vertex(i))) {
            System.out.print("EdgeTo-" + v.getLabel() + ", ");
         }
         System.out.println();
      }
      System.out.println();
   }
   
   public static void main(String[] args) {
      // Edit problem here
      int[] current = {2,8,3,1,6,4,7,0,5};   // initial state      
      int[] goal = {1,2,3,8,0,4,7,6,5};      // goal state
      // create graph
      int vertexCount = 9;                   // number of vertices
      Graph graph = new Graph();          
      for(int i = 0; i < vertexCount; i++) { 
         graph.addVertex(i, current[i], goal[i]); 
      }                         
      graph.addEdge(0, 1);       //    (0) -- (1) -- (2)
      graph.addEdge(0, 3);       //     |      |      |
      graph.addEdge(1, 2);       //     |      |      |
      graph.addEdge(1, 4);       //    (3) -- (4) -- (5)
      graph.addEdge(2, 5);       //     |      |      |
      graph.addEdge(3, 4);       //     |      |      |
      graph.addEdge(3, 6);       //    (6) -- (7) -- (8)
      graph.addEdge(4, 5);
      graph.addEdge(4, 7);
      graph.addEdge(5, 8);
      graph.addEdge(6, 7);
      graph.addEdge(7, 8);
      
      EightPuzzle myProgram = new EightPuzzle(graph);
      GraphState goalState = myProgram.aStar();
      goalState.printPath();
      
   }
}
