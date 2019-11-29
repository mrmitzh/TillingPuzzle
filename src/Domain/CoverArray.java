package Domain;

import java.util.ArrayList;
import java.util.List;

public class CoverArray {
    int columnNum = 0;
    int rowNum = 0;
    public int[][] coverArray;
    int tileNum = 0;

    public CoverArray(List<Tile> tiles, Tile board){
        List<int[]> arrays = new ArrayList<>();
        this.tileNum = tiles.size();
        columnNum = board.area + tiles.size();
        for(int i = 0; i < tiles.size(); i ++){
            Tile t = tiles.get(i);

            for(int j = 0; j < t.spinDataArrays.size(); j ++){
                check(t.spinDataArrays.get(j), board.data, arrays, i, tiles.size());
            }
            for(int j = 0; j < t.flipDataArrays.size(); j ++){
                check(t.flipDataArrays.get(j), board.data, arrays, i, tiles.size());
            }
        }
        coverArray = new int[arrays.size()][arrays.get(0).length];
        for(int i = 0; i < coverArray.length; i ++){
            int[] row = arrays.get(i);
            for(int j = 0; j < row.length; j ++){
                if(row[j] == 1)coverArray[i][j] = 1;
            }
        }
        rowNum = coverArray.length;
    }

    public void check(char[][] data, char[][] boardData, List<int[]> arrays, int index, int tileCount){
        int tileHeight = data.length;
        int boardHeight = boardData.length;
        int tileWidth = data[0].length;
        int boardWidth = boardData[0].length;
        for(int i = 0; i < boardHeight-tileHeight+1; i ++){
            for(int j = 0; j < boardWidth-tileWidth+1; j ++){
                if(isValid(data, boardData, i, j)){
                    int[] curRow = new int[columnNum];
                    curRow[index] = 1;
                    for(int k = 0; k < tileHeight; k ++){
                        for(int l = 0; l < tileWidth; l ++){
                            if(data[k][l] != ' '){
                                curRow[tileCount+(i+k)*boardWidth+(l+j)]=1;
                            }
                        }
                    }
                    arrays.add(curRow);
                }
            }
        }
    }

    public boolean isValid(char[][] data, char[][] boardData, int x, int y){
        int height = data.length;
        int width = data[0].length;
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                if(boardData[x+i][y+j] != data[i][j])return false;
            }
        }
        return true;
    }
}
