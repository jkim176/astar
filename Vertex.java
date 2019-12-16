public class Vertex {
   private int label;
   private int tile;
   private int goal;
   
   public Vertex(int label) {
      this.label = label;
   }
   
   public Vertex(int label, int tile, int goal) {
      this.label = label;
      this.tile = tile;
      this.goal = goal;
   }
   
   public int getLabel() {
      return this.label;
   }
   
   public int getTile() {
      return this.tile;
   }
   
   public void setTile(int tile) {
      this.tile = tile;
   }
   
   public int getGoal() {
      return this.goal;
   }
   
   @Override
   public boolean equals(Object o) {
      if(o == this)
         return true;
      if(!(o instanceof Vertex))
         return false;
      Vertex comparedVertex = (Vertex) o;
      if(comparedVertex.label == this.label)
         return true;
      else
         return false;
   }
   
   @Override
   public int hashCode() {
      int hash = 7;
      hash = 31 * hash + label;
      return hash;
   }
}
