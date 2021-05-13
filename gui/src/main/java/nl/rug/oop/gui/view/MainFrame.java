package nl.rug.oop.gui.view;

import lombok.Getter;
import lombok.SneakyThrows;
import nl.rug.oop.gui.model.AppCore;

import javax.swing.*;
import java.awt.*;

@Getter
public class MainFrame extends JFrame {
    AppCore model;
    JTable jTable;
    public MainFrame(AppCore model) {
        super("Fantasy Database");
        this.model = model;
        init();
    }

    @SneakyThrows
    public void init() {
        setPreferredSize(new Dimension(500, 1000));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuBar();
        add(new TablePanel(model), BorderLayout.NORTH);
        add(new DetailsPanel(model), BorderLayout.WEST);
        add(new QueryPanel(model), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    public void menuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem export = new JMenuItem("Export");
        menu.add(export);
        bar.add(menu);
        setJMenuBar(bar);
    }

    public void setTable(JTable jTable) {
        this.jTable = jTable;
    }
}
