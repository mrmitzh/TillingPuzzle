package GUI;

import Domain.CoverArray;
import Domain.LinkArray;
import Domain.Tile;
import Domain.TilingPuzzle;
import Utils.ReadFile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class Draw extends Component {
    private JFrame jFrame;
    private JPanel panel1;
    private JButton select;
    private JMenuBar menuBar;
    private JMenu menu;
    private final Color bgColor = Color.WHITE;
    private JPanel controlPanel;
    private ReadFile readFile;
    private JPanel allTiles;
    private JPanel tileSolution;
    private ArrayList<Color> colorMapping;
    private List<Tile> tiles;
    private Tile board;
    private JButton getNextSolution;
    private JButton getPrevSolution;
    private List<char[][]> res;
    private BufferedImage image;
    private Graphics boardGraph;
    private int blockHeight = 20;
    private int blockWidth = 20;
    private int boardDisplayHeight = 600;
    private int boardDisplayWidth = 600;
    int solutionIndex = 0;
    boolean enableSpin = true;
    boolean enableSpinFlip = true;
    boolean enableEliminateDuplicate = false;

    public Draw(List<Tile> tiles, Tile board)
    {
        this.tiles = tiles;
        this.board = board;
        colorMapping = new ArrayList<>();
        colorMapping.addAll(Arrays.asList(Color.red, Color.green, Color.blue,
                Color.yellow, Color.cyan, new Color(46, 139, 87),
                new Color(148, 0, 211), new Color(135, 51, 36), Color.magenta,
                Color.gray, Color.pink, new Color(175, 255, 225),
                new Color(130, 175, 190)));
        int[] H = new int[] {0, 20, 30, 40, 50, 60, 90, 160, 190,
                205, 220, 235, 260, 285, 305, 330};
        int[] S = new int[] {100, 50, 100, 75, 100};
        int[] B = new int[] {100, 100, 70, 100, 85};
        float h, s, b;
        int i = 0;
        while (colorMapping.size() < tiles.size()) {
            List<Color> color = new ArrayList<Color>();
            for (int j = 0; j < H.length; j++) {
                // skip some indistinct colors
                if (i == 0 && (j == 0 || j == 3 || j == 5 || j == 11 || j == 14)) continue;
                if (i == 1 && (j == 1 || j == 2 || j == 3 || j == 4)) continue;
                if (i == 2 && (j == 10 || j == 11 || j == 12 || j == 13)) continue;
                if (i == 4 && (j == 11 || j == 12)) continue;
                h = H[j] / 360.f;
                s = S[i % S.length] / 100.f;
                b = B[i % B.length] / 100.f;
                color.add(Color.getHSBColor(h, s, b));
            }
            Collections.shuffle(color);
            colorMapping.addAll(color);
            i++;
        }



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
                    Draw.this.tiles = readFile.tiles;
                    Draw.this.board = readFile.board;
                    //Draw draw = new Draw(readFile.tiles, readFile.board);
                    Draw.this.showTileList();
                    Draw.this.showBoard(-1);
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

        JCheckBox enableSpinCheckBox = new JCheckBox("Enable Rotation");
        enableSpinCheckBox.setBackground(Color.white);
        enableSpinCheckBox.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        enableSpinCheckBox.setSelected(true);
        enableSpinCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
                enableSpin = enableSpinCheckBox.isSelected();
            }
        });

        JCheckBox enableSpinFlipCheckBox = new JCheckBox("Rotation + Reflection");
        enableSpinFlipCheckBox.setBackground(Color.WHITE);
        enableSpinFlipCheckBox.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        enableSpinFlipCheckBox.setSelected(true);
        enableSpinFlipCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
                enableSpinFlip = enableSpinFlipCheckBox.isSelected();
            }
        });



        JCheckBox eliminateDuplicate = new JCheckBox("Eliminate Duplicate");
        eliminateDuplicate.setBackground(Color.white);
        eliminateDuplicate.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        eliminateDuplicate.setSelected(false);
        eliminateDuplicate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableEliminateDuplicate = eliminateDuplicate.isSelected();
            }
        });

        JPanel selctionPanel = new JPanel();
        selctionPanel.setLayout(new GridLayout(3,1));
        selctionPanel.setBackground(Color.WHITE);
        selctionPanel.add(enableSpinCheckBox);
        selctionPanel.add(enableSpinFlipCheckBox);
        selctionPanel.add(eliminateDuplicate);
        selctionPanel.setVisible(true);


        ///

        JPanel buttonPanel = new JPanel();

        JButton getSolutionButton = new JButton("Get All Solution");
        getSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:
                TilingPuzzle tilingPuzzle = new TilingPuzzle();
                CoverArray coverArray = new CoverArray(tiles, board, enableSpin, enableSpinFlip);
                tilingPuzzle.board = board;
                tilingPuzzle.tiles = tiles;
                tilingPuzzle.coverArray = coverArray;
                tilingPuzzle.stack = new Stack<>();
                tilingPuzzle.solution = new Stack<>();
                tilingPuzzle.linkArray = new LinkArray(coverArray);
                tilingPuzzle.result = new ArrayList<>();
                long start = System.nanoTime();
                tilingPuzzle.solve(tilingPuzzle.linkArray,enableEliminateDuplicate);
                long end = System.nanoTime();
                res = tilingPuzzle.result;
                if(res.size() > 1)getNextSolution.setEnabled(true);
                if(res.size() == 0)JOptionPane.showMessageDialog(jFrame, "No solution");
                else
                {
                    JOptionPane.showMessageDialog(jFrame, "Find total solution size: " + String.valueOf(res.size()) +
                            " With time: " + String.valueOf((double)(end-start)/1000000.0)+" ms");
                    showBoard(solutionIndex);
                }
                jFrame.revalidate();
            }
        });

        getNextSolution = new JButton("Next");
        getNextSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                showBoard(++solutionIndex);
                if(solutionIndex == res.size()-1){
                    getNextSolution.setEnabled(false);
                }
                getPrevSolution.setEnabled(true);
            }
        });

        getPrevSolution = new JButton("Prev");
        getPrevSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBoard(--solutionIndex);
                if(solutionIndex <= res.size()-1){
                    getNextSolution.setEnabled(true);
                }
                if(solutionIndex == 0){
                    getPrevSolution.setEnabled(false);
                }
            }
        });

        getNextSolution.setEnabled(false);
        getPrevSolution.setEnabled(false);
        buttonPanel.setLayout(new GridLayout(2,1));
        buttonPanel.setVisible(true);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(getSolutionButton);
        JButton temp = new JButton();
        temp.setVisible(false);
        buttonPanel.add(temp);
        buttonPanel.add(getPrevSolution);
        buttonPanel.add(getNextSolution);


        controlPanel.setLayout(new GridLayout(2,1));
        controlPanel.add(selctionPanel);
        controlPanel.add(buttonPanel);
        controlPanel.setPreferredSize(new Dimension(300, 600));

        // Middle Layout
        tileSolution = new JPanel();
        tileSolution.setBorder(BorderFactory.createTitledBorder("Solution"));
        tileSolution.setBackground(Color.WHITE);
        tileSolution.setVisible(true);
        tileSolution.setPreferredSize(new Dimension(boardDisplayWidth, boardDisplayHeight));


        // East Layout
        allTiles = new JPanel();
        allTiles.setBorder(BorderFactory.createTitledBorder("All the tiles"));
        allTiles.setBackground(Color.WHITE);
        allTiles.setVisible(true);
        allTiles.setPreferredSize(new Dimension(400, 600));


        jFrame.setJMenuBar(menuBar);
        jFrame.setLayout(new BorderLayout());
        jFrame.add("West",controlPanel);
        jFrame.add("Center",tileSolution);
        jFrame.add("East", allTiles);


        jFrame.setVisible(true);
        //jFrame.setSize(new Dimension(800,600));
        jFrame.pack();
    }



    private void showTileList()
    {
        allTiles.removeAll();
        int tileListHeight = boardDisplayHeight;
        int tileListWidth = 400;
        int[][] tileMatrix;
        // tile with longest width
        int horizonUpBound = 12;
        for(Tile tile: tiles){
            if(tile.data[0].length > horizonUpBound){
                horizonUpBound = tile.data[0].length;
            }
        }
        int verticalUpBound = 1;
        int horizonIndex = 1;
        int curRowMaxHeight = 0;
        for(Tile tile:tiles){
            // in the same row
            if(horizonIndex + tile.data[0].length < horizonUpBound + 2){
                horizonIndex = horizonIndex + tile.data[0].length + 1;
                if(tile.data.length > curRowMaxHeight)curRowMaxHeight = tile.data.length;
            }else{
                verticalUpBound = verticalUpBound + curRowMaxHeight + 1;
                horizonIndex = 1;
                curRowMaxHeight = 0;
                horizonIndex = horizonIndex + tile.data[0].length + 1;
                if(tile.data.length > curRowMaxHeight)curRowMaxHeight = tile.data.length;
            }
        }
        if(curRowMaxHeight > 0)verticalUpBound = verticalUpBound + curRowMaxHeight + 1;
        int blockWidth = tileListWidth / (horizonUpBound+2);
        int blockHeight = tileListHeight / verticalUpBound;
        int length = blockHeight > blockWidth ? blockWidth:blockHeight;
        if(length > 30)length = 30;
        int horizonSize = tileListWidth / length;
        int verticalSize = tileListHeight / length;
        tileMatrix = new int[verticalSize][horizonSize];
        for(int[] row: tileMatrix){
            //fill with -1
            Arrays.fill(row, -1);
        }
        horizonIndex = 1;
        int verticalIndex = 1;
        for(int index = 0; index < tiles.size(); index ++){
            char[][] data = tiles.get(index).data;
            if(horizonIndex + data[0].length < horizonSize){
                for(int i = 0; i < data.length; i ++){
                    for(int j = 0; j < data[0].length; j ++){
                        if(data[i][j] != ' ')tileMatrix[verticalIndex+i][horizonIndex+j] = index;
                    }
                }
                horizonIndex = horizonIndex + data[0].length + 1;
                if(data.length > curRowMaxHeight)curRowMaxHeight = data.length;
            }else{
                verticalIndex = verticalIndex + curRowMaxHeight + 1;
                horizonIndex = 1;
                curRowMaxHeight = 0;
                index --;
            }
        }

        ArrayList<JPanel> allPanelList = new ArrayList<>();
        allTiles.setLayout(new GridLayout(tileMatrix.length, tileMatrix[0].length,1,1));
        allTiles.setPreferredSize(new Dimension(tileListWidth, tileListHeight));
        for(int i = 0; i < tileMatrix.length; i ++){
            for(int j = 0; j < tileMatrix[0].length; j ++){
                JPanel cur = new JPanel();
                if(tileMatrix[i][j] >= 0){
                    cur.setBackground(colorMapping.get(tileMatrix[i][j]));
                    cur.setVisible(true);
                }else{
                    cur.setVisible(false);
                }
                allTiles.add(cur);
            }
        }
        allTiles.revalidate();
        allTiles.repaint();
//        int counter = 0;
//        for(Tile tile:tiles)
//        {
//            counter++;
//            JPanel currentPanel = new JPanel();
//            currentPanel.setLayout(new GridLayout(tile.data.length,tile.data[0].length,2,2));
//            for(int i = 0; i < tile.data.length;i++)
//            {
//                for(int j = 0; j < tile.data[i].length;j++)
//                {
//                    JPanel temp = new JPanel();
//                    temp.setBackground(colorMapping.get(counter-1));
//                    if(tile.data[i][j] != ' ')
//                    {
//                        // has data
//                        JLabel label = new JLabel(Character.toString(tile.data[i][j]));
//                        label.setVisible(true);
//                        temp.add(label);
//                        temp.setVisible(true);
//                        currentPanel.add(temp);
//                    }else
//                    {
//                        temp.setVisible(false);
//                        currentPanel.add(temp);
//                    }
//                }
//            }
//            currentPanel.setVisible(true);
//            currentPanel.setBackground(Color.WHITE);
//            currentPanel.setBorder(BorderFactory.createTitledBorder("Tile " + Integer.toString(counter)));
//            allPanelList.add(currentPanel);
//        }
//        allTiles.setLayout(new GridLayout((int) Math.ceil(1.0*tiles.size()/3),3));
//        for(JPanel panel: allPanelList)
//        {
//            allTiles.add(panel);
//        }
    }

    public void showBoard(int index){
        tileSolution.removeAll();
        int width = boardDisplayWidth;
        int height = boardDisplayHeight;
        int blockWidth = width / board.data[0].length;
        int blockHeight = height / board.data.length;
        int length = blockWidth > blockHeight ? blockHeight : blockWidth;
        if(length > 45)length = 45;
        int widthSize = width / length;
        int heightSize = height / length;
        int vOffset = (heightSize - board.data.length) / 2;
        int hOffset =  (widthSize - board.data[0].length) / 2;
        tileSolution.setLayout(new GridLayout(heightSize, widthSize, 1, 1));
        for(int i = 0; i < heightSize; i ++){
            for(int j = 0; j < widthSize; j ++){
                JPanel temp = new JPanel();
                if(i >= vOffset && i < vOffset + board.data.length && j >= hOffset && j < hOffset + board.data[0].length){
                    if(board.data[i-vOffset][j-hOffset] != ' '){
                        if(index != -1){
                            temp.setBackground(colorMapping.get(res.get(index)[i-vOffset][j-hOffset]-'a'));
                        }else temp.setBackground(Color.gray);
                        temp.setVisible(true);
                    }else{
                        temp.setVisible(false);
                    }
                }else{
                    temp.setVisible(false);
                }
                tileSolution.add(temp);
            }
        }
        tileSolution.revalidate();
        tileSolution.repaint();
    }

    public void showSolution(int index){
        tileSolution.removeAll();

    }

    public void setupBoard(){
        int height = board.data.length;
        int width = board.data[0].length;
//        JPanel boardPanel = new JPanel();
//        boardPanel.setSize(new Dimension(40 * width, 40 * height));
//        boardPanel.setBackground(Color.BLACK);
//        boardPanel.setVisible(true);
//        tileSolution.add("Center", boardPanel);
    }

    public static void main(String[] args)
    {
        ReadFile readFile = new ReadFile(args[0]);
        Draw draw = new Draw(readFile.tiles, readFile.board);
        draw.showTileList();
        draw.showBoard(-1);
    }

}
