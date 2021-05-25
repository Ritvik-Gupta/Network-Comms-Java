package src.Graph.GraphAlgorithm;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;

import services.Generator.Generator;
import src.Graph.Graph;
import src.Graph.Vector2D;

public abstract class GraphAlgorithm<YieldType, ReturnType> extends Generator<YieldType, ReturnType> {
   public static interface IUpdatable<YieldType> {
      public void updateNext(YieldType nextYield);
   }

   public final class FetchNextAction implements ActionListener {
      protected final Iterator<YieldType> itr = iterator();
      protected final HashSet<IUpdatable<YieldType>> updatableItemSet = new HashSet<>();

      public final void add(IUpdatable<YieldType> updatable) {
         this.updatableItemSet.add(updatable);
      }

      @Override
      public final void actionPerformed(ActionEvent e) {
         if (itr.hasNext()) {
            YieldType nextYield = itr.next();
            updatableItemSet.forEach(updatable -> updatable.updateNext(nextYield));
         } else {
            assert e.getSource() instanceof JButton;
            JButton sourceBtn = (JButton) e.getSource();
            sourceBtn.setEnabled(false);
         }
      }
   }

   public abstract class InstructionSet extends JPanel {
      public InstructionSet() {
         this.build();
      }

      protected abstract void build();
   }

   public abstract class Visualization extends JPanel implements IUpdatable<YieldType> {
      protected YieldType currentYield;
      protected final Vector2D offset;

      public Visualization(Vector2D offset) {
         this.currentYield = null;
         this.offset = offset;
      }

      @Override
      public final void updateNext(YieldType nextYield) {
         currentYield = nextYield;
         repaint();
      }

      public abstract void paint(Graphics2D gr);

      @Override
      public final void paint(Graphics g) {
         if (currentYield == null)
            return;

         super.paint(g);
         Graphics2D gr = (Graphics2D) g;

         paint(gr);
      }

      @Override
      public final Dimension getPreferredSize() {
         return graph.dimensions.add(offset.multiply(2)).asDimension();
      }
   }

   protected final Graph graph;

   public GraphAlgorithm(Graph graph) {
      this.graph = graph;
   }

   @Override
   public abstract ReturnType generate() throws InterruptedException;

   public abstract GeneratorVisualizer<YieldType> visualize(String title, int nodeSize, Vector2D offset);
}
