package Domain;

import Utils.ReadFile;

import java.util.*;

public class TilingPuzzle {

    public LinkArray linkArray;
    public Stack<Node> stack;
    public Stack<Node> solution;
    public CoverArray coverArray;
    public Tile board;
    public List<Tile> tiles;
    public ArrayList<char[][]> result;
    int level = 0;
    public Boolean firstTime = true;
    public long endTime = 0;

    public static void main(String[] args) {
        if(args.length != 1)
        {
            System.out.println("Usage: Domain.TilingPuzzle FileName");
            System.exit(0);
        }
        ReadFile readFile = new ReadFile(args[0]);
        CoverArray coverArray = new CoverArray(readFile.tiles, readFile.board, true, true);
//        coverArray.printArray();
        TilingPuzzle tilingPuzzle = new TilingPuzzle();
        tilingPuzzle.board = readFile.board;
        tilingPuzzle.tiles = readFile.tiles;
        tilingPuzzle.coverArray = coverArray;
        tilingPuzzle.stack = new Stack<>();
        tilingPuzzle.solution = new Stack<>();
        tilingPuzzle.linkArray = new LinkArray(coverArray);
        tilingPuzzle.result = new ArrayList<>();
        tilingPuzzle.solve(tilingPuzzle.linkArray,true);

        System.out.println(tilingPuzzle.result.size());
    }

    public char[][] generateSolution(Stack<Node> solution)
    {
        Node[] arr = new Node[solution.size()];
        Stack<Node> copySolution = (Stack<Node>) solution.clone();
        char[][] res = new char[board.data.length][board.data[0].length];
        for(int i = 0; i < res.length;i++)
        {
            for(int j = 0; j < res[0].length;j++)
            {
                res[i][j] = ' ';
            }
        }
        for(int i = arr.length - 1 ; i >= 0; i--)
        {
            arr[i] = copySolution.pop();
        }
        for(Node node:arr)
        {
            int[] row = coverArray.coverArray[node.row];
            int index = 0;
            for(int i = 0; i < tiles.size(); i ++){
                if(row[i] == 1){
                    index = i;
                    break;
                }
            }
            char c= (char)('a' + index);
            for(int i = tiles.size(); i < row.length; i ++){
                if(row[i] == 1){
                    int position = i-tiles.size();
                    for(int x = 0; x < coverArray.boardidx.length; x ++){
                        for(int y = 0; y < coverArray.boardidx[0].length; y ++){
                            if(coverArray.boardidx[x][y] == position){
                                res[x][y] = c;
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    public void putSolution(char[][] solution,boolean eliminateDuplicate)
    {
        if(eliminateDuplicate)
        {
            TransformMatrix transformMatrix = new TransformMatrix(solution);
            List<char[][]> allData = transformMatrix.getAllDataArrays();
            for(int i = 0; i < result.size(); i++)
            {
                for(int j = 0; j < allData.size();j++)
                {
                    if(TransformMatrix.equal(result.get(i),allData.get(j)))
                    {
                        return;
                    }
                }
            }
        }
        result.add(solution);
    }

    public void printMatrix(char[][] res)
    {
        for(int i = 0; i < res.length;i++)
        {
            for(int j = 0; j < res[0].length;j++)
            {
                System.out.print(res[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void solve(LinkArray linkArray,boolean eliminateDuplicate){
        level ++;
        if(linkArray.h.right == linkArray.h || linkArray.h.left.col < linkArray.tileNum){
            //Finished
            if(firstTime)
            {
                endTime = System.nanoTime();
                firstTime = false;
            }
            char[][] generatedSolution = generateSolution(solution);
//            printMatrix(generatedSolution);
            putSolution(generatedSolution,eliminateDuplicate);
            return;
        }
        ColumnNode nextColumn = findStartColumn(linkArray);
        cover(nextColumn);
        for(Node nextNode = nextColumn.down; nextNode != nextColumn; nextNode = nextNode.down){
            solution.push(nextNode);
            for(Node rightNode = nextNode.right; rightNode != nextNode; rightNode = rightNode.right){
                cover(rightNode.head);
            }
            solve(linkArray,eliminateDuplicate);
            nextNode = solution.pop();
            nextColumn = nextNode.head;
            for(Node leftNode = nextNode.left; leftNode != nextNode; leftNode = leftNode.left){
                uncover(leftNode.head);
            }
        }
        uncover(nextColumn);
        level --;
    }

    public void cover(ColumnNode columnNode){
        columnNode.left.right = columnNode.right;
        columnNode.right.left = columnNode.left;
        for(Node row = columnNode.down; row != columnNode; row = row.down)
        {
            for(Node rightNode = row.right; rightNode != row; rightNode = rightNode.right)
            {
                rightNode.up.down = rightNode.down;
                rightNode.down.up = rightNode.up;
                rightNode.head.size--;
            }
        }
    }

    public void uncover(ColumnNode colNode){
        for(Node rowNode = colNode.up; rowNode != colNode; rowNode = rowNode.up)
        {
            for(Node leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left)
            {
                leftNode.up.down = leftNode;
                leftNode.down.up = leftNode;
                leftNode.head.size ++;
            }
        }
        colNode.right.left = colNode;
        colNode.left.right = colNode;
    }

    public ColumnNode findStartColumn(LinkArray linkArray){
        ColumnNode head = linkArray.h;
        int minSize = Integer.MAX_VALUE;
        ColumnNode minNode = head;
        ColumnNode cur = head.right;
        // check area to see if there are extra tiles
        int boardArea = board.getArea();
        int total = 0;
        for(int i = 0; i < tiles.size(); i ++){
            total += tiles.get(i).getArea();
        }
        while(cur != head){
            if(total > boardArea && cur.col < tiles.size()){
                cur = cur.right;
                continue;
            }
            if(cur.size < minSize){
                minSize = cur.size;
                minNode = cur;
            }
            cur = cur.right;
        }
        return minNode;
    }
}
