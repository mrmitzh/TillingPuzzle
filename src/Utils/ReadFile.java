package Utils;

import Domain.Tile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    String filePath = "";
    char[][] fileContent;
    List<Tile> tiles;
    Tile board;
    int leftMost = Integer.MAX_VALUE;
    int upMost = Integer.MAX_VALUE;
    int rightMost = Integer.MIN_VALUE;
    int downMost = Integer.MIN_VALUE;

    public ReadFile(String path){
        filePath = path;
        File f = new File(filePath);
        BufferedReader bufferedReader = null;
        List<String> stringBuf = new ArrayList<>();
        try{
            bufferedReader = new BufferedReader(new FileReader(f));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                stringBuf.add(line);
            }
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        int row = stringBuf.size();
        int col = stringBuf.get(0).length();
        for(int i = 0; i < row; i ++){
            if(stringBuf.get(i).length() > col)col = stringBuf.get(i).length();
        }
        fileContent = new char[row][col];
        for(int i = 0; i < row; i ++){
            for(int j = 0; j < col; j ++){
                fileContent[i][j] = ' ';
            }
        }
        boolean[][] visited = new boolean[row][col];
        tiles = new ArrayList<>();
        for(int i = 0; i < row; i ++){
            for(int j = 0; j < stringBuf.get(i).length(); j ++){
                fileContent[i][j] = stringBuf.get(i).charAt(j);
            }
        }
        for(int i = 0; i < row; i ++){
            for(int j = 0; j < col; j ++){
                if(fileContent[i][j] == ' '){
                    visited[i][j] = true;
                    continue;
                }else{
                    if(!visited[i][j]){
                        find(fileContent, visited, i, j);
                        int width = rightMost-leftMost+1;
                        int height = downMost-upMost+1;
                        char[][] data = new char[height][width];
                        for(int ii = 0; ii < height; ii ++){
                            for(int jj = 0; jj < width; jj ++){
                                data[ii][jj] = fileContent[upMost+ii][leftMost+jj];
                            }
                        }
                        Tile t = new Tile(data);
                        tiles.add(t);
                        leftMost = Integer.MAX_VALUE;
                        upMost = Integer.MAX_VALUE;
                        rightMost = Integer.MIN_VALUE;
                        downMost = Integer.MIN_VALUE;
                    }

                }
            }
        }
        // find the board according to the area
        int index = 0;
        int maxArea = 0;
        for(int i = 0; i < tiles.size(); i ++){
            if(tiles.get(i).getArea() > maxArea){
                maxArea = tiles.get(i).getArea();
                index = i;
            }
        }
        board = tiles.get(index);
        tiles.remove(index);
    }

    public void find(char[][] a, boolean[][] visited, int i, int j){
        if(i < 0 || j < 0 || i >= a.length || j >= a[0].length || visited[i][j])return;
        if(a[i][j] == ' ')return;
        if(j > rightMost)rightMost = j;
        if(j < leftMost)leftMost = j;
        if(i > downMost)downMost = i;
        if(i < upMost)upMost = i;
        visited[i][j] = true;
        find(a, visited, i-1, j);
        find(a, visited, i+1, j);
        find(a, visited, i, j-1);
        find(a, visited, i, j+1);
    }
}
