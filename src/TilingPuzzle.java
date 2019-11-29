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
        Node node = columnNode.down;
        while(node != columnNode){
            Node innerNode = node.right;
            while(innerNode != node){
                innerNode.down.up = innerNode.up;
                innerNode.up.down = innerNode.down;
                innerNode.head.size --;
            }
        }
    }

    public void uncover(ColumnNode columnNode){
        Node node =columnNode.up;
        while(node != columnNode){
            Node innerNode = node.left;
            while(innerNode != node){
                innerNode.head.size++;
                innerNode.down.up = innerNode;
                innerNode.up.down = innerNode;
            }
        }
        columnNode.right.left = columnNode;
        columnNode.left.right = columnNode;
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
