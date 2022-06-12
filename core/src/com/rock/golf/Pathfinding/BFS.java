package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public static ArrayList<Node> BFSSearch(Graph graph, Node startnode, Node stopnode) {
        
        Queue<Node> visited = new LinkedList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(startnode);
        while(!queue.isEmpty()){
            Node currentNode = queue.poll();
            
            if(!visited.contains(currentNode)){
                visited.add(currentNode);
                for(Node node : graph.neighbors(currentNode)) {
                    queue.add(node);
                    if(!visited.contains(node)) queue.add(node);
                }
            }       

            if(visited.contains(stopnode)) {
                break;
            }
        }
        Node parentNode = stopnode;
        ArrayList<Node> path = new ArrayList<>();
        while(!(parentNode.parent == startnode)){
            path.add(parentNode);
            parentNode.isPath = true;
            parentNode = parentNode.parent;
        }
        
        path.add(parentNode);
        parentNode.isPath = true;
        path.add(startnode);
        startnode.isPath = true;

        Collections.reverse(path);

        return path;
    }

}
