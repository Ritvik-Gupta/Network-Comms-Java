package src.Graph.GraphAlgorithm;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public final class GeneratorVisualizer<YieldType> extends JFrame {
   private final GraphAlgorithm<YieldType, ?>.Visualization visualization;
   private final JScrollPane instructionSet;
   private final GraphAlgorithm<YieldType, ?>.FetchNextAction fetchNextAction;

   public GeneratorVisualizer(
      String title, GraphAlgorithm<YieldType, ?>.Visualization visualization,
      GraphAlgorithm<YieldType, ?>.InstructionSet instructionSet,
      GraphAlgorithm<YieldType, ?>.FetchNextAction fetchNextAction
   ) {
      super(title);
      this.visualization = visualization;
      this.instructionSet = new JScrollPane(instructionSet);
      this.fetchNextAction = fetchNextAction;

      this.fetchNextAction.add(this.visualization);

      this.buildGui();
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.pack();
   }

   private void buildGui() {
      JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
      tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

      tabbedPane.addTab("Graph", visualization);
      tabbedPane.addTab("Instructions", instructionSet);

      add(tabbedPane, BorderLayout.NORTH);

      JPanel southPanel = new JPanel();
      southPanel.setLayout(new BorderLayout());
      southPanel.setBorder(new TitledBorder(new EtchedBorder(), " Event Actions "));

      JPanel buttons = new JPanel();
      buttons.setLayout(new GridLayout(1, 1));
      buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      JButton fetchNextBtn = new JButton("Fetch Next");
      fetchNextBtn.addActionListener(fetchNextAction);
      buttons.add(fetchNextBtn);
      
      southPanel.add(buttons, BorderLayout.EAST);
      add(southPanel, BorderLayout.SOUTH);
   }
}
