package src.Graph.GraphAlgorithm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JList;

import src.Graph.Graph;
import src.Graph.Vector2D;

public final class MinSpanTree extends GraphAlgorithm<MinSpanTree.Yield, Graph> {
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
            Graph.Node parentNode = currentYield.parentNodes.get(vertex.id);

            if (parentNode != null) {
               Vector2D parentP = parentNode.vector.add(offset);
               gr.setPaint(
                  new GradientPaint(
                     vertexP.toPoint2D(), 
                     new Color(89, 193, 115), 
                     parentP.toPoint2D(), 
                     new Color(93, 38, 193)
                  )
               );
               gr.setStroke(new BasicStroke(2));
               gr.drawLine(vertexP.x, vertexP.y, parentP.x, parentP.y);
            }
         });

         graph.forEachVertex((__, vertex) -> {
            Vector2D vertexP = vertex.vector.add(offset);
            Vector2D cirleCenterP = vertexP.add(-nodeSize / 2, -nodeSize / 2);

            gr.setColor(Color.BLACK);
            gr.setStroke(new BasicStroke(1));
            gr.fillOval(cirleCenterP.x, cirleCenterP.y, nodeSize, nodeSize);

            if (vertex.id.equals(currentYield.currentNodeId)) {
               gr.setColor(Color.ORANGE);
               gr.setStroke(new BasicStroke(4));
               gr.drawOval(cirleCenterP.x, cirleCenterP.y, nodeSize, nodeSize);
            } else if (currentYield.visitedNodes.get(vertex.id)) {
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

            Integer vertexKey = currentYield.vertexKeys.get(vertex.id);
            if (vertexKey != Integer.MAX_VALUE) {
               Vector2D vertexKeyP = vertexP.add(nodeSize / 2, -nodeSize / 2);
               
               gr.setColor(Color.BLUE);
               gr.drawString(vertexKey.toString(), vertexKeyP.x, vertexKeyP.y);
            }
         });
      }
   }

   public static final class Yield {
      public final HashMap<String, Boolean> visitedNodes;
      public final HashMap<String, Integer> vertexKeys;
      public final HashMap<String, Graph.Node> parentNodes;
      public final String currentNodeId;

      public Yield(
         HashMap<String, Boolean> visitedNodes, 
         HashMap<String, Integer> vertexKeys,
         HashMap<String, Graph.Node> parentNodes,
         String currentNodeId
      ) {
         this.visitedNodes = visitedNodes;
         this.vertexKeys = vertexKeys;
         this.parentNodes = parentNodes;
         this.currentNodeId = currentNodeId;
      }
   }

   private final Graph.Node startNode;

   public MinSpanTree(Graph graph, String startNodeId) {
      super(graph);
      this.startNode = graph.get(startNodeId);
   }

   @Override
   public Graph generate() throws InterruptedException {
      Graph minSpanTree = new Graph(graph.dimensions);
      HashMap<String, Boolean> visitedNodes = new HashMap<>();
      HashMap<String, Integer> vertexKeys = new HashMap<>();
      HashMap<String, Graph.Node> parentNodes = new HashMap<>();

      graph.forEachVertex((__, node) -> {
         visitedNodes.put(node.id, false);
         vertexKeys.put(node.id, Integer.MAX_VALUE);
         parentNodes.put(node.id, null);
         minSpanTree.add(new Graph.Node(node));
      });

      vertexKeys.put(startNode.id, 0);
      yieldNext(new Yield(visitedNodes, vertexKeys, parentNodes, null));
      
      for (int count = 0; count < graph.numVertices(); ++count) {
         String minVertexId = null;
         Integer minVertexKey = Integer.MAX_VALUE;

         for (Entry<String, Integer> entry : vertexKeys.entrySet())
            if (!visitedNodes.get(entry.getKey()) && entry.getValue() < minVertexKey) {
               minVertexKey = entry.getValue();
               minVertexId = entry.getKey();
            }

         if (minVertexId == null)
            break;
         Graph.Node minVertex = graph.get(minVertexId);
         visitedNodes.put(minVertex.id, true);

         minVertex.forEachNeighbour((__, edge) -> {
            String neighbourId = edge.neighbour.id;
            if (!visitedNodes.get(neighbourId) && vertexKeys.get(neighbourId) > edge.weight) {
               parentNodes.put(neighbourId, minVertex);
               vertexKeys.put(neighbourId, edge.weight);
            }
         });

         yieldNext(new Yield(visitedNodes, vertexKeys, parentNodes, minVertexId));
      }

      parentNodes.forEach((nodeId, parent) -> {
         if (parent != null)
            minSpanTree.connect(nodeId, parent.id, vertexKeys.get(parent.id));
      });

      return minSpanTree;
   }

   @Override
   public GeneratorVisualizer<Yield> visualize(String title, int nodeSize, Vector2D offset) {
      return new GeneratorVisualizer<>(
         title, 
         new VisualizationGraph(nodeSize, offset), 
         new InstructionSet() {
            @Override
            protected void build() {
               JList<String> list = new JList<>();
               list.setFont(new Font("Courier New", Font.ITALIC, 16));
               list.setListData(new String[] {
                  "A", 
                  "B"
               });
               add(list);
            }
         }, 
         new FetchNextAction()
      );
   }
}
