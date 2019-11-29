package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile {
    public char[][] data;
    public char[][] flipData;
    public List<char[][]> spinDataArrays = new ArrayList<>();
    public List<char[][]> flipDataArrays = new ArrayList<>();

    int height;
    int width;
    int spin;

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    int area;

    public Tile(char[][] t){
        height = t.length;
        width = t[0].length;
        data = new char[height][width];
        flipData = new char[height][width];
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                data[i][j] = t[i][j];
                flipData[i][width-1-j] = t[i][j];
                if(t[i][j] != ' ')area ++;
            }
        }
        spinDataArrays.add(data.clone());
        spinDataArrays.add(generateRotate90(data));
        spinDataArrays.add(generateRotate180(data));
        spinDataArrays.add(generateRotate270(data));
        flipDataArrays.add(flipData.clone());
        flipDataArrays.add(generateRotate90(flipData));
        flipDataArrays.add(generateRotate180(flipData));
        flipDataArrays.add(generateRotate270(flipData));

        // remove duplicate
        for(int i = 0; i < spinDataArrays.size(); i ++){
            for(int j = i+1; j < spinDataArrays.size(); j ++){
                if(equal(spinDataArrays.get(i), spinDataArrays.get(j))){
                    spinDataArrays.remove(j);
                }
            }
        }

        for(int i = 0; i < flipDataArrays.size(); i ++){
            for(int j = i+1; j < flipDataArrays.size(); j ++){
                if(equal(flipDataArrays.get(i), flipDataArrays.get(j))){
                    flipDataArrays.remove(j);
                }
            }
        }
    }

    public boolean equal(Tile t){
        if(t.area != area)return false;
        if(t.data.length != data.length)return false;
        for(int i = 0; i < data.length; i ++) {
            if (!Arrays.equals(data[i], t.data[i])) return false;
        }
        return true;
    }

    public boolean equalSpin(Tile t){
        if(t.area != area)return false;
        for(int i = 0; i < spinDataArrays.size(); i ++){
            if(equal(spinDataArrays.get(i), t.spinDataArrays.get(0)))return true;
        }
        return false;
    }

    public boolean equalFlip(Tile t){
        if(t.area != area)return  false;
        for(int i = 0; i < flipDataArrays.size(); i++){
            if(equal(flipDataArrays.get(i), t.flipDataArrays.get(0)))return true;
        }
        return false;
    }

    public boolean equal(char[][] a, char[][] b){
        if(a.length != b.length || a[0].length != b[0].length)return false;
        for(int i = 0; i < a.length; i ++){
            if(!Arrays.equals(a[i], b[i]))return false;
        }
        return true;
    }

     private char[][] generateRotate90(char[][] data){
        char[][] res = new char[width][height];
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
               res[j][height-i-1] = data[i][j];
            }
        }
        return res;
     }

     private char[][] generateRotate180(char[][] data){
         char[][] res = new char[height][width];
         for(int i = 0; i < height; i ++){
             for(int j = 0; j < width; j ++){
                 res[i][j] = data[height-i-1][width-j-1];
             }
         }
         return res;
     }

     private char[][] generateRotate270(char[][] data){
        char[][] res = new char[width][height];
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                res[width-j-1][i] = data[i][j];
            }
        }
        return res;
     }
}
