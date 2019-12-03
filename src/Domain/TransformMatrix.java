package Domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransformMatrix {
    private char[][] data;
    private char[][] flipData;
    public List<char[][]> spinDataArrays = new ArrayList<>();
    public List<char[][]> flipDataArrays = new ArrayList<>();
    public List<char[][]> allDataArrays = new ArrayList<>();
    private int width,height;

    public List<char[][]> getAllDataArrays()
    {
        return allDataArrays;
    }

    public TransformMatrix(char t[][])
    {
        height = t.length;
        width = t[0].length;
        data = new char[height][width];
        flipData = new char[height][width];
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                data[i][j] = t[i][j];
                flipData[i][width-1-j] = t[i][j];
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
                    j--;
                }
            }
        }

        for(int i = 0; i < flipDataArrays.size(); i ++){
            for(int j = i+1; j < flipDataArrays.size(); j ++){
                if(equal(flipDataArrays.get(i), flipDataArrays.get(j))){
                    flipDataArrays.remove(j);
                    j--;
                }
            }
        }

        allDataArrays.addAll(flipDataArrays);
        allDataArrays.addAll(spinDataArrays);
        for(int i = 0; i < allDataArrays.size(); i ++){
            for(int j = i+1; j < allDataArrays.size(); j ++){
                if(equal(allDataArrays.get(i), allDataArrays.get(j))){
                    allDataArrays.remove(j);
                    j--;
                }
            }
        }
    }

    public static boolean equal(char a[][], char b[][]){
        if(a.length != b.length)
            return false;
        if(a[0].length != b[0].length)
            return false;
        for(int i = 0; i < a.length;i++)
        {
            for(int j = 0; j < a[0].length;j++)
            {
                if(a[i][j] != b[i][j])
                    return false;
            }
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
