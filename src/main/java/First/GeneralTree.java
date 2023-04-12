/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package First;

import java.util.Vector;

/**
 *
 * @author kihakik
 */
public class GeneralTree {
    static class Node{
        int unit;
        int target;
        int eval;
        Vector<Node> child = new Vector<>();
    };
    //uztaisa jaunu Node
    static Node newNode(int unit, int target){
        Node temp= new Node();
        temp.unit=unit;
        temp.target=target;
        temp.child = new Vector<>();
        return temp;
        
    }
    static Node setEval(int eval, Node node){
        node.eval=eval;
        return node;
    }
    static int getUnit(Node node){
        return node.unit;
    }
        
    
}
