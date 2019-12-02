package Domain;

import java.util.ArrayList;
import java.util.List;

public class LinkArray {
    int[][] coverArray;
    public int tileNum = 0;
    public Node[][] nodes;
    public ColumnNode h;

    public LinkArray(CoverArray ca){
        this.coverArray = ca.coverArray;
        this.tileNum = ca.tileNum;
        List<ColumnNode> columnNodes = new ArrayList<>();
        nodes = new Node[ca.rowNum][ca.columnNum];

        h = new ColumnNode();
        for(int i = 0; i < ca.columnNum; i ++){
            ColumnNode columnNode = new ColumnNode();
            columnNode.down = columnNode;
            columnNode.up = columnNode;
            columnNode.row = -1;
            columnNode.col = i;
            if(i == 0){
                columnNode.left = h;
                h.right = columnNode;
            }else if(i != ca.columnNum-1){
                columnNode.left = columnNodes.get(i-1);
                columnNodes.get(i-1).right = columnNode;
            }else{
                columnNode.right = h;
                h.left = columnNode;
            }
            columnNodes.add(columnNode);
        }

        //initialize all the nodes
        for(int i = 0; i < ca.columnNum; i ++){
            int size = 0;
            for(int j = 0; j < ca.rowNum; j ++){
                if(ca.coverArray[j][i] == 1){
                    Node n = new Node();
                    nodes[j][i] = n;
                    n.row = j;
                    n.col = i;
                    size ++;
                }else{
                    nodes[j][i] = null;
                }
            }
            if(i > tileNum && size == 0){

            }
            columnNodes.get(i).size = size;
        }

        //initialize the rows
        for(int i = 0; i < ca.rowNum; i ++){
            Node prev = null;
            Node head = null;
            for(int j = 0; j < ca.columnNum; j ++){
                if(nodes[i][j] == null)continue;
                Node cur = nodes[i][j];
                if(head == null){
                    head = cur;
                    prev = cur;
                }else{
                    cur.left = prev;
                    prev.right = cur;
                    prev = cur;
                }
            }
            prev.right = head;
            head.left = prev;
        }

        for(int i = 0; i < ca.columnNum; i ++){
            Node prev = null;
            ColumnNode head = columnNodes.get(i);
            Node cur = head;
            for(int j = 0; j < ca.rowNum; j ++){
                if(nodes[j][i] == null)continue;
                cur = nodes[j][i];
                cur.head = head;
                if(prev == null){
                    cur.up = head;
                    head.down = cur;
                }else{
                    cur.up = prev;
                    prev.down = cur;
                }
                prev = cur;
            }
            cur.down = head;
            head.up = cur;
        }
    }


}
