import Domain.ColumnNode;
import Domain.CoverArray;
import Domain.LinkArray;
import Domain.Node;
import Utils.ReadFile;

import java.util.List;
import java.util.Stack;

public class TilingPuzzle {

    LinkArray linkArray;
    Stack<Node> stack;
    Stack<Node> solution;
    public static void main(String[] args) {
        if(args.length != 1)
        {
            System.out.println("Usage: TilingPuzzle FileName");
            System.exit(0);
        }
        ReadFile readFile = new ReadFile(args[0]);
        CoverArray coverArray = new CoverArray(readFile.tiles, readFile.board);
        TilingPuzzle tilingPuzzle = new TilingPuzzle();
        tilingPuzzle.stack = new Stack<>();
        tilingPuzzle.solution = new Stack<>();
        tilingPuzzle.linkArray = new LinkArray(coverArray);
        tilingPuzzle.solve(tilingPuzzle.linkArray);
    }

    public void solve(LinkArray linkArray){
        if(linkArray.h.right == linkArray.h || linkArray.h.left.col < linkArray.tileNum){
            //Finished
        }
        ColumnNode nextColumn = findStartColumn(linkArray);
        cover(nextColumn);
        for(Node nextNode = nextColumn.down; nextNode != nextColumn; nextNode = nextNode.down){
            solution.push(nextNode);
            for(Node rightNode = nextNode.right; rightNode != nextNode; rightNode = rightNode.right){
                cover(rightNode.head);
            }
            solve(linkArray);
            nextNode = solution.pop();
            nextColumn = nextNode.head;
            for(Node rightNode = nextNode.right; rightNode != nextNode; rightNode = rightNode.right){
                uncover(rightNode.head);
            }
        }
        uncover(nextColumn);
        return;
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

    public void uncover(ColumnNode columnNode){
        Node colNode =columnNode.up;
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
        while(cur != head){
            if(cur.size < minSize){
                minSize = cur.size;
                minNode = cur;
            }
        }
        return minNode;
    }
}
