import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

public class GraphState {
   Graph graph;
   int depth;
   int manhattanDistance;
   Vertex previous;
   Vertex blank;
   List<Vertex> path;
   
   public GraphState(Graph graph, int depth, int manhattanDistance, Vertex previous, Vertex blank) {
      this.graph = graph;
      this.depth = depth;
      this.manhattanDistance = manhattanDistance;
      this.previous = previous;
      this.blank = blank;
      List<Vertex> p = new LinkedList<>();
      p.add(previous);
      p.add(blank);
      this.path = p;
   }
   
   public GraphState(Graph graph, int depth, int manhattanDistance, Vertex previous, Vertex blank, List<Vertex> oldPath) {
      this.graph = graph;
      this.depth = depth;
      this.manhattanDistance = manhattanDistance;
      this.previous = previous;
      this.blank = blank;
      List<Vertex> p = new LinkedList<>();
      ListIterator<Vertex> it = oldPath.listIterator();
      while(it.hasNext()) {
         Vertex v = it.next();
         p.add(v);
      }
      p.add(blank);
      this.path = p;
   }
   
   public void printPath() {
      ListIterator<Vertex> it = path.listIterator();
      while(it.hasNext()) {
         System.out.print("Vertex" + it.next().getLabel() + " -> "); 
      }
      System.out.println();
   }
}
