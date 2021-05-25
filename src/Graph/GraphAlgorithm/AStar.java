package src.Graph.GraphAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JLabel;

import java.awt.*;

import src.Graph.CustomException;
import src.Graph.Graph;
import src.Graph.Vector2D;

public final class AStar extends GraphAlgorithm<AStar.Yield, ArrayList<Graph.Node>> {
   public final class VisualizationGraph extends Visualization {
      private final int nodeSize;

      public VisualizationGraph(int nodeSize, Vector2D offset) {
         super(offset);
         this.nodeSize = nodeSize;
      }

      @Override
      public void paint(Graphics2D gr) {
         graph.forEachVertex((__, vertex) -> {
            Vector2D vertexP = vertex.vector.add(offset);
            Vector2D cirleCenterP = vertexP.add(-nodeSize / 2, -nodeSize / 2);

            gr.setColor(Color.BLACK);
            gr.setStroke(new BasicStroke(1));
            gr.fillOval(cirleCenterP.x, cirleCenterP.y, nodeSize, nodeSize);

            vertex.forEachNeighbour((___, edge) -> {
               Vector2D neighbourP = edge.neighbour.vector.add(offset);

               gr.drawLine(vertexP.x, vertexP.y, neighbourP.x, neighbourP.y);
            });
         });

         graph.forEachVertex((__, vertex) -> {
            Vector2D vertexP = vertex.vector.add(offset);
            Vector2D cirleCenterP = vertexP.add(-nodeSize / 2, -nodeSize / 2);

            if (vertex.id.equals(currentYield.currentNodeId)) {
               gr.setColor(Color.ORANGE);
               gr.setStroke(new BasicStroke(4));
               gr.drawOval(cirleCenterP.x, cirleCenterP.y, nodeSize, nodeSize);
            } else if (currentYield.openNodes.contains(vertex.id)) {
               gr.setColor(Color.CYAN);
               gr.setStroke(new BasicStroke(3));
               gr.drawOval(cirleCenterP.x, cirleCenterP.y, nodeSize, nodeSize);
            }
         });

         gr.setFont(new Font ("Courier New", Font.BOLD, 17));
         graph.forEachVertex((__, vertex) -> {
            Vector2D vertexP = vertex.vector.add(offset);

            gr.setColor(Color.WHITE);
            gr.drawString(vertex.id, vertexP.x, vertexP.y);

            AStarCell aStarNode = currentYield.graphNodeCells.get(vertex.id);

            if(aStarNode.g != Double.MAX_VALUE) {
               Vector2D aStarP = vertexP.add(nodeSize / 2, -nodeSize / 2);
               gr.setColor(Color.BLUE);
               gr.drawString(String.valueOf(aStarNode.g), aStarP.x, aStarP.y);
            }

            Vector2D aStarP = vertexP.add(nodeSize / 2, nodeSize / 2);
            gr.setColor(Color.DARK_GRAY);
            gr.drawString(String.valueOf(aStarNode.h), aStarP.x, aStarP.y);
         });
      }
   }
   
   public static final class AStarCell {
      public final double g;
      public final double h;
      public final Graph.Node parent;

      public AStarCell(double g, double h, Graph.Node parent) {
         this.g = g;
         this.h = h;
         this.parent = parent;
      }

      public double getF() {
         return g + h;
      }
   }

   public static enum Heuristic {
      MANHATTAN, DIAGONAL, EUCLIDEAN;
   }

   @FunctionalInterface
   public static interface IHeuristic {
      public double calc(Vector2D vec);
   }
   
   @FunctionalInterface
   public static interface IHeuristicApproximation {
      public double calc(Vector2D vecA, Vector2D vecB);
   }

   public static final class Yield {
      public final HashSet<String> openNodes;
      public final HashMap<String, AStarCell> graphNodeCells;
      public final String currentNodeId;

      public Yield(
         HashSet<String> openNodes,
         HashMap<String, AStarCell> graphNodeCells,
         String currentNodeId
      ) {
         this.openNodes = openNodes;
         this.graphNodeCells = graphNodeCells;
         this.currentNodeId = currentNodeId;
      }
   }

   private final Graph.Node startNode;
   private final Graph.Node endNode;
   private final IHeuristic heuristic;

   public AStar(Graph graph, String startNodeId, String endNodeId, Heuristic chosenHeuristic) {
      super(graph);
      this.startNode = graph.get(startNodeId);
      this.endNode = graph.get(endNodeId);

      this.heuristic = vec -> {
         double xDiff = vec.x - this.endNode.vector.x, yDiff = vec.y - this.endNode.vector.y;
         switch (chosenHeuristic) {
            case MANHATTAN:
               return Math.abs(xDiff) + Math.abs(yDiff);
            case DIAGONAL:
               return Math.max(Math.abs(xDiff), Math.abs(yDiff));
            case EUCLIDEAN:
               return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
            default:
               return 0;
         }
      };
   }

   public AStar(Graph graph, String startNodeId, String endNodeId, IHeuristicApproximation heuristic) {
      super(graph);
      this.startNode = graph.get(startNodeId);
      this.endNode = graph.get(endNodeId);

      this.heuristic = vec -> heuristic.calc(vec, this.endNode.vector);
   }

   private ArrayList<Graph.Node> tracePath(HashMap<String, AStarCell> graphNodeCells) {
      ArrayList<Graph.Node> path = new ArrayList<>();
      Graph.Node currentNode = endNode;
      while (true) {
         path.add(new Graph.Node(currentNode));
         AStarCell currentCell = graphNodeCells.get(currentNode.id);

         if (currentNode.id.equals(currentCell.parent.id))
            return path;
         currentNode = currentCell.parent;
      }
   }

   @Override
   public ArrayList<Graph.Node> generate() throws InterruptedException {
      HashSet<String> openNodes = new HashSet<>();
      HashMap<String, AStarCell> graphNodeCells = new HashMap<>();

      graph.forEachVertex((__, vertex) -> {
         graphNodeCells.put(vertex.id, new AStarCell(Double.MAX_VALUE, 0, null));
      });

      openNodes.add(startNode.id);
      graphNodeCells.put(startNode.id, new AStarCell(0, heuristic.calc(startNode.vector), startNode));

      while (openNodes.size() > 0) {
         String minScoreNodeId = null;
         double minFScore = Double.MAX_VALUE;

         for (String nodeId : openNodes) {
            double nodeFScore = graphNodeCells.get(nodeId).getF();
            if (minFScore > nodeFScore) {
               minScoreNodeId = nodeId;
               minFScore = nodeFScore;
            }
         }

         if (minScoreNodeId == null)
            throw new CustomException("A* : ", "F Scores determined to be Infinity");
         Graph.Node currentNode = graph.get(minScoreNodeId);

         if (currentNode.id.equals(endNode.id))
            break;
         
         openNodes.remove(currentNode.id);

         currentNode.forEachNeighbour((__, edge) -> {
            double tentativeGScore = graphNodeCells.get(currentNode.id).g + edge.weight;

            if (tentativeGScore < graphNodeCells.get(edge.neighbour.id).g) {
               graphNodeCells.put(
                  edge.neighbour.id,
                  new AStarCell(tentativeGScore, heuristic.calc(edge.neighbour.vector), currentNode)
               );
               openNodes.add(edge.neighbour.id);
            }
         });
         
         yieldNext(new Yield(openNodes, graphNodeCells, currentNode.id));
      }

      yieldNext(new Yield(openNodes, graphNodeCells, endNode.id));
      return tracePath(graphNodeCells);
   }

   @Override
   public GeneratorVisualizer<Yield> visualize(String title, int nodeSize, Vector2D offset) {      
      return new GeneratorVisualizer<>(
         title, 
         new VisualizationGraph(nodeSize, offset), 
         new InstructionSet() {
            @Override
            protected void build() {
               setFont(new Font ("Courier New", Font.ITALIC, 16));
               add(new JLabel("knakvabkabn labalbn abn"));
               add(new JLabel("knakvabkabn labalbn abn"));
               add(new JLabel("knakvabkabn labalbn abn"));
               add(new JLabel("knakvabkabn labalbn abn"));
            }
         } , 
         new FetchNextAction()
      );
   }
}
