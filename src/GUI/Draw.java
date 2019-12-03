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
                    System.out.println(file.toString());
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
        jFrame.setJMenuBar(menuBar);
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
