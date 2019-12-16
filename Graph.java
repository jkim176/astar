import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

public class Graph {
   private Map<Vertex, List<Vertex>> adjacencyList;
   
   public Graph() {
      this.adjacencyList = new HashMap<>();
   }
   
   public Graph(Map<Vertex, List<Vertex>> adjacencyList) {
      Map<Vertex, List<Vertex>> original = new HashMap<>(adjacencyList);
      Map<Vertex, List<Vertex>> newMap = new HashMap<Vertex, List<Vertex>>();
      Set<Vertex> keys = original.keySet();
      for(Vertex key: keys) {
         List<Vertex> values = original.get(key);
         Vertex keyCopy = new Vertex(key.getLabel(), key.getTile(), key.getGoal());
         newMap.put(keyCopy, new ArrayList<>());
         for(Vertex value: values) {
            Vertex valueCopy = new Vertex(value.getLabel(), value.getTile(), value.getGoal());
            newMap.get(keyCopy).add(valueCopy);
         }
      }
      this.adjacencyList = newMap;
   }
   
   public void addVertex(int label, int tile, int goal) {
      adjacencyList.putIfAbsent(new Vertex(label, tile, goal), new ArrayList<>());
   }
   
   public void removeVertex(int label) {
      Vertex v = new Vertex(label);
      adjacencyList.values().stream().forEach(e -> e.remove(v));
      adjacencyList.remove(v);
   }
   
   public void addEdge(int label1, int label2) {
      Vertex v1 = new Vertex(label1);
      Vertex v2 = new Vertex(label2);
      Vertex realVertex1 = null;
      Vertex realVertex2 = null;
      if(!adjacencyList.containsKey(v1) || !adjacencyList.containsKey(v2))
         return;
      Set<Vertex> set = adjacencyList.keySet();
      Iterator<Vertex> it = set.iterator();
      while(it.hasNext()) {
         Vertex cur = it.next();
         if(cur.equals(v1)) {
            realVertex1 = cur;
            continue;
         }
         if(cur.equals(v2)) {
            realVertex2 = cur;
            continue;
         }
         if(realVertex1 != null && realVertex2 != null)
            break;
      }  
      adjacencyList.get(v1).add(realVertex2);   // undirected Graph
      adjacencyList.get(v2).add(realVertex1);
   }
   
   public void removeEdge(int label1, int label2) {
      Vertex v1 = new Vertex(label1);
      Vertex v2 = new Vertex(label2);
      List<Vertex> list1 = adjacencyList.get(v1);
      List<Vertex> list2 = adjacencyList.get(v2);
      if(list1 != null)    // remove both edges 1 to 2, 2 to 1
         list1.remove(v2);
      if(list2 != null)
         list2.remove(v1);
   }
   
   public Map<Vertex, List<Vertex>> getAdjacencyList() {
      return adjacencyList;
   }
}
