package GUI;

import Domain.Tile;
import Utils.ReadFile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Draw extends Component {
    private JFrame jFrame;
    private JPanel panel1;
    private JButton select;
    private JMenuBar menuBar;
    private JMenu menu;
    private final Color bgColor = Color.WHITE;
    private JPanel controlPanel;
    private ReadFile readFile;
    private JPanel showAllTiles;
    private JPanel showTileSolution;

    public Draw()
    {
        jFrame = new JFrame();
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        JMenuItem menuOpen = new JMenuItem("Open");
        JMenuItem menuExit = new JMenuItem("Exit");
        menuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                if(fc.showOpenDialog(Draw.this) == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    readFile = new ReadFile(file.getPath());
                }
            }
        });
        menuExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuOpen);
        menu.add(menuExit);
        menuBar.add(menu);

        controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control"));
        controlPanel.setOpaque(true);
        controlPanel.setVisible(true);
        controlPanel.setFocusable(true);

        JCheckBox enableSpin = new JCheckBox("Enable Rotation");
        enableSpin.setBackground(Color.white);
        enableSpin.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        enableSpin.setSelected(false);
        enableSpin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
            }
        });

        JCheckBox enableSpinFlip = new JCheckBox("Rotation + Reflection");
        enableSpinFlip.setBackground(Color.WHITE);
        enableSpinFlip.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        enableSpinFlip.setSelected(false);
        enableSpinFlip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
            }
        });



        JCheckBox eliminateDuplicate = new JCheckBox("Eliminate Duplicate");
        eliminateDuplicate.setBackground(Color.white);
        eliminateDuplicate.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        eliminateDuplicate.setSelected(false);
        eliminateDuplicate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
            }
        });

        JPanel selctionPanel = new JPanel();
        selctionPanel.setLayout(new GridLayout(3,1));
        selctionPanel.setBackground(Color.WHITE);
        selctionPanel.add(enableSpin);
        selctionPanel.add(enableSpinFlip);
        selctionPanel.add(eliminateDuplicate);
        selctionPanel.setVisible(true);


        ///

        JPanel buttonPanel = new JPanel();

        JButton getSolutionButton = new JButton("Get All Solution");
        getSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
            }
        });

        JButton showSolution = new JButton("Show Solution");
        showSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
            }
        });

        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.setVisible(true);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(getSolutionButton);
        buttonPanel.add(showSolution);


        controlPanel.setLayout(new GridLayout(2,1));
        controlPanel.add(selctionPanel);
        controlPanel.add(buttonPanel);

        // Middle Layout
        showTileSolution = new JPanel();
        showTileSolution.setBorder(BorderFactory.createTitledBorder("Solution"));
        showTileSolution.setBackground(Color.WHITE);
        showTileSolution.setVisible(true);


        // East Layout
        showAllTiles = new JPanel();
        showAllTiles.setBorder(BorderFactory.createTitledBorder("All the tiles"));
        showAllTiles.setBackground(Color.WHITE);
        showAllTiles.setVisible(true);


        jFrame.setJMenuBar(menuBar);
        jFrame.setLayout(new BorderLayout());
        jFrame.add("West",controlPanel);
        jFrame.add("Center",showTileSolution);
        jFrame.add("East", showAllTiles);


        jFrame.setVisible(true);
        jFrame.setSize(new Dimension(800,600));
    }


    private void showTileList(List<Tile> tiles)
    {
        ArrayList<JPanel> allPanelList = new ArrayList<>();
        int counter = 0;
        for(Tile tile:tiles)
        {
            counter++;
            JPanel currentPanel = new JPanel();
            currentPanel.setLayout(new GridLayout(tile.data.length,tile.data[0].length));
            for(int i = 0; i < tile.data.length;i++)
            {
                for(int j = 0; j < tile.data[0].length;j++)
                {
                    if(tile.data[i][j] != ' ')
                        System.out.print(tile.data[i][j]);
                    else
                        System.out.print("@");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
            for(int i = 0; i < tile.data.length;i++)
            {
                for(int j = 0; j < tile.data[i].length;j++)
                {
                    JPanel temp = new JPanel();
                    temp.setBackground(Color.WHITE);
                    if(tile.data[i][j] != ' ')
                    {
                        // has data
                        JLabel label = new JLabel(Character.toString(tile.data[i][j]));
                        label.setVisible(true);
                        temp.add(label);
                        temp.setVisible(true);
                        currentPanel.add(temp);
                    }else
                    {
                        temp.setVisible(false);
                        currentPanel.add(temp);
                    }
                }
            }
            currentPanel.setVisible(true);
            currentPanel.setBackground(Color.WHITE);
            currentPanel.setBorder(BorderFactory.createTitledBorder("Tile " + Integer.toString(counter)));
            allPanelList.add(currentPanel);
        }
        showAllTiles.setLayout(new GridLayout((int) Math.ceil(1.0*tiles.size()/3),3));
        for(JPanel panel: allPanelList)
        {
            showAllTiles.add(panel);
        }
    }

    public static void main(String[] args)
    {
        ReadFile readFile = new ReadFile(args[0]);
        Draw draw = new Draw();
        draw.showTileList(readFile.tiles);
    }

}
