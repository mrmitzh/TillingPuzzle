package GUI;

import Domain.Tile;
import Utils.ReadFile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class Draw extends Component {
    private JFrame jFrame;
    private JPanel panel1;
    private JButton select;
    private JPanel pTileList;
    private JMenuBar menuBar;
    private JMenu menu;
    private final Color bgColor = Color.WHITE;
    private JPanel controlPanel;
    private ReadFile readFile;

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

        controlPanel.setLayout(new GridLayout(3,1));
        controlPanel.add(enableSpin);
        controlPanel.add(enableSpinFlip);
        controlPanel.add(eliminateDuplicate);

        jFrame.setJMenuBar(menuBar);
        jFrame.setLayout(new BorderLayout());
        jFrame.add("West",controlPanel);

        jFrame.setVisible(true);
        jFrame.setSize(new Dimension(800,600));
    }

    private void tileListPanel()
    {
        pTileList = new JPanel();
        pTileList.setBackground(bgColor);
        pTileList.setLayout(null);
        pTileList.setOpaque(true);
        pTileList.setVisible(true);
        pTileList.setFocusable(true);
        pTileList.setBorder(BorderFactory.createTitledBorder("Tiles"));
    }

    private void showTileList(List<Tile> tiles)
    {
        tileListPanel();
        int maxHeight = 0;
        int maxWidth = 0;
        for(Tile tile:tiles)
        {
            maxHeight = Math.max(maxHeight,tile.data.length);
            maxWidth = Math.max(maxWidth,tile.data[0].length);
        }
        for(Tile tile:tiles)
        {
            JPanel subPanel = new JPanel();
            for(int i = 0; i < tile.data.length;i++)
            {
                for(int j = 0; j < tile.data[0].length;j++)
                {
                    if(tile.data[i][j] != ' ')
                    {
                        JPanel block = new JPanel();
                        block.setBorder(new LineBorder(Color.black));
                        block.setSize(30,40);
                        JLabel l = new JLabel(Character.toString(tile.data[i][j]));
                        l.setVisible(true);
                        block.add(l);
                        block.setVisible(true);
                        subPanel.add(block);
                    }
                }
            }
            subPanel.setVisible(true);
            pTileList.add(subPanel);
        }
        pTileList.setVisible(true);
        jFrame.add(pTileList);
    }

    public static void main(String[] args)
    {
        ReadFile readFile = new ReadFile(args[0]);
        Draw draw = new Draw();
        draw.showTileList(readFile.tiles);

    }

}
