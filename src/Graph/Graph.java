package src.Graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.function.BiConsumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Graph {
   public static final class Node {
      public final String id;
      public final Vector2D vector;
      private final HashMap<String, Edge> neighbours;

      public Node(String id, Vector2D vector) {
         this.id = id;
         this.vector = vector;
         this.neighbours = new HashMap<>();
      }

      public Node(Node prevNode) {
         this.id = new String(prevNode.id);
         this.vector = new Vector2D(prevNode.vector);
         this.neighbours = new HashMap<>();
      }

      public void add(Node neighbour, int weight) {
         neighbours.put(neighbour.id, new Edge(weight, neighbour));
      }

      public boolean has(String neighbourId) {
         return neighbours.containsKey(neighbourId);
      }

      public void forEachNeighbour(BiConsumer<? super String, ? super Edge> action) {
         neighbours.forEach(action);
      }

      @Override
      public String toString() {
         return id + " -> " + vector;
      }
   }

   public static final class Edge {
      public final int weight;
      public final Node neighbour;

      public Edge(int weight, Node neighbour) {
         this.weight = weight;
         this.neighbour = neighbour;
      }
   }

   public final class GridMap extends JPanel {
      private final int nodeSize;
      private final Vector2D offset;

      public GridMap(int nodeSize, Vector2D offset) {
         this.nodeSize = nodeSize;
         this.offset = offset;
      }

      @Override
      public void paint(Graphics g) {
         super.paint(g);
         Graphics2D gr = (Graphics2D) g;

         vertices.forEach((__, vertex) -> {
            Vector2D vertexP = vertex.vector.add(offset);

            gr.setColor(Color.BLACK);
            Vector2D cirleCenterP = vertexP.add(-nodeSize / 2, -nodeSize / 2);
            gr.fillOval(cirleCenterP.x, cirleCenterP.y, nodeSize, nodeSize);

            vertex.neighbours.forEach((___, edge) -> {
               Vector2D neighbourP = edge.neighbour.vector.add(offset);

               gr.drawLine(vertexP.x, vertexP.y, neighbourP.x, neighbourP.y);
            });
         });

         gr.setFont(new Font("Courier New", Font.BOLD, 17));
         vertices.forEach((__, vertex) -> {
            Vector2D vertexP = vertex.vector.add(offset);

            gr.setColor(Color.WHITE);
            gr.drawString(vertex.id, vertexP.x, vertexP.y);

            vertex.neighbours.forEach((___, edge) -> {
               Vector2D neighbourP = edge.neighbour.vector.add(offset);
               Vector2D edgeMidP = Vector2D.splitSection(vertexP, neighbourP, 1, 1);

               gr.setColor(Color.BLUE);
               gr.drawString(String.valueOf(edge.weight), edgeMidP.x, edgeMidP.y);
            });
         });
      }

      @Override
      public Dimension getPreferredSize() {
         return dimensions.add(offset.multiply(2)).asDimension();
      }
   }

   public final Vector2D dimensions;
   private final HashMap<String, Node> vertices;

   public Graph(Vector2D dimensions) {
      this.dimensions = dimensions;
      this.vertices = new HashMap<>();
   }

   public int numVertices() {
      return vertices.size();
   }

   public void add(Node node) {
      if (!node.vector.isContainedIn(new Vector2D(0, 0), dimensions))
         throw new CustomException("Invalid Coordinates : ",
               "Canvas " + dimensions + " should contain the Node " + node.vector);
      vertices.put(node.id, node);
   }

   public Node get(String id) {
      Node vertex = vertices.get(id);
      if (vertex == null)
         throw new CustomException("Unknown Id Exception : ", "Graph does not contain a Node with ID = '" + id + "'");
      return vertex;
   }

   public boolean hasNode(String id) {
      return vertices.containsKey(id);
   }

   public void connect(String nodeId, String neighbourId, int weight) {
      Node node = get(nodeId);
      Node neighbour = get(neighbourId);
      if (!node.has(neighbourId))
         node.add(get(neighbourId), weight);
      if (!neighbour.has(nodeId))
         neighbour.add(node, weight);
   }

   public void forEachVertex(BiConsumer<? super String, ? super Node> action) {
      vertices.forEach(action);
   }

   public void draw(JFrame frame, int nodeSize, Vector2D offset) {
      GridMap gridMap = new GridMap(nodeSize, offset);
      frame.add(gridMap);
      frame.pack();
      frame.setVisible(true);
   }
}
