package src.Graph;

import javax.swing.JFrame;

import src.Graph.GraphAlgorithm.AStar;
import src.Graph.GraphAlgorithm.MinSpanTree;

public final class Main {
   public static void main(String[] args) {
      Graph graph = new Graph(new Vector2D(1000, 500));

      graph.add(new Graph.Node("0", new Vector2D(0, 250)));
      graph.add(new Graph.Node("1", new Vector2D(250, 0)));
      graph.add(new Graph.Node("2", new Vector2D(500, 0)));
      graph.add(new Graph.Node("3", new Vector2D(750, 0)));
      graph.add(new Graph.Node("4", new Vector2D(1000, 250)));
      graph.add(new Graph.Node("5", new Vector2D(750, 500)));
      graph.add(new Graph.Node("6", new Vector2D(500, 500)));
      graph.add(new Graph.Node("7", new Vector2D(250, 500)));
      graph.add(new Graph.Node("8", new Vector2D(500, 250)));

      graph.connect("0", "1", 4);
      graph.connect("0", "7", 8);
      graph.connect("1", "2", 8);
      graph.connect("1", "7", 11);
      graph.connect("2", "3", 7);
      graph.connect("2", "8", 2);
      graph.connect("2", "5", 4);
      graph.connect("3", "4", 9);
      graph.connect("3", "5", 14);
      graph.connect("4", "5", 10);
      graph.connect("5", "6", 2);
      graph.connect("6", "7", 1);
      graph.connect("6", "8", 6);
      graph.connect("7", "8", 7);

      graph.draw(new JFrame("Graph"), 50, new Vector2D(50, 50));

      // MinSpanTree spanTreeGen = new MinSpanTree(graph, "0");
      // for (var yield : spanTreeGen) {
      // Console.log("Parent Nodes :\n");
      // Console.println(yield.parentNodes);
      // Console.log("Vertex Keys :\n");
      // Console.println(yield.vertexKeys);
      // Console.log("Visited Nodes :\n");
      // Console.println(yield.visitedNodes);
      // }

      // Graph spanTree = spanTreeGen.fetchReturn();
      // spanTree.draw(new JFrame("Span Tree"), 50, new Vector(50, 50));

      MinSpanTree spanTreeGen = new MinSpanTree(graph, "0");
      var viz = spanTreeGen.visualize("A* Algorithm - Visualization", 50, new Vector2D(50, 50));
      viz.setVisible(true);
   }
}
