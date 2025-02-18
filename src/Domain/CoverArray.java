package Domain;

import java.util.ArrayList;
import java.util.List;

public class CoverArray {
    int columnNum = 0;
    int rowNum = 0;
    public int[][] coverArray;
    public int[][] boardidx;
    int tileNum = 0;

    public CoverArray(List<Tile> tiles, Tile board, boolean enableSpin, boolean enableSpinFlip){
        List<int[]> arrays = new ArrayList<>();
        this.tileNum = tiles.size();
        columnNum = board.area + tiles.size();
        buildBoardIdxArray(board);
        for(int i = 0; i < tiles.size(); i ++){
            Tile t = tiles.get(i);

//            for(int j = 0; j < t.spinDataArrays.size(); j ++){
//                check(t.spinDataArrays.get(j), board.data, arrays, i, tiles.size());
//            }
//            for(int j = 0; j < t.flipDataArrays.size(); j ++){
//                check(t.flipDataArrays.get(j), board.data, arrays, i, tiles.size());
//            }
            if(enableSpinFlip){
                for(int j = 0; j < t.allDataArrays.size(); j ++){
                    check(t.allDataArrays.get(j), board.data, arrays, i, tiles.size());
                }
            }else if(enableSpin){
                for(int j = 0; j < t.spinDataArrays.size(); j ++){
                    check(t.spinDataArrays.get(j), board.data, arrays, i, tiles.size());
                }
            }else{
                check(t.data, board.data, arrays, i, tiles.size());
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

    private void buildBoardIdxArray(Tile board) {
        boardidx = new int[board.data.length][board.data[0].length];
        int cnt = 0;
        for (int r = 0; r < board.data.length; r++) {
            for (int c = 0; c < board.data[0].length; c++) {
                if (board.data[r][c] != ' ') {
                    boardidx[r][c] = cnt;
                    cnt++;
                } else {
                    boardidx[r][c] = -1;
                }
            }
        }
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
                                curRow[tileCount+boardidx[i+k][j+l]]=1;
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
                if(data[i][j] != ' ' && boardData[x+i][y+j] != data[i][j])return false;
            }
        }
        return true;
    }

    public void printArray(){
        for(int i = 0; i < rowNum; i ++){
            for(int j = 0; j < columnNum; j ++){
                System.out.print(coverArray[i][j]);
            }
            System.out.println();
        }
    }
}
