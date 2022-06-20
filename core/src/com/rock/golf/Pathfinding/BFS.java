package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import com.rock.golf.RockGolf;

public class BFS {
    
    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    float originX = sizeX / 2;
    float originY = sizeY / 2;
    Random rn = new Random();
    float metertoPixelRatio = RockGolf.metertoPixelRatio;

    public ArrayList<Node> BFSSearch(Graph graph, Node startnode, Node stopnode) {
        Queue<Node> visited = new LinkedList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(startnode);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if(!visited.contains(currentNode)){
                visited.add(currentNode);
                for (Node node : graph.neighbors(currentNode)) {
                    queue.add(node);
                    if (!visited.contains(node))
                        queue.add(node);
                }
            }

            if (visited.contains(stopnode)) {
                break;
            }
        }
        
        Node parentNode = stopnode;
        ArrayList<Node> path = new ArrayList<>();
        
        while (!(parentNode.parent == startnode)) {
            path.add(parentNode);
            parentNode.isPath = true;
            parentNode = parentNode.parent;
        }

        path.add(parentNode);
        parentNode.isPath = true;
        path.add(startnode);
        startnode.isPath = true;

        return path;
    }

    public ArrayList<Node> BFSWalkSearch(Graph graph, Node startnode, Node stopnode) {
        Queue<Node> visited = new LinkedList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(startnode);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if(rn.nextDouble() <= 0.5 && queue.size() > 1) {
                currentNode = queue.poll();
                currentNode = queue.poll();
            }

            if(!visited.contains(currentNode)){
                visited.add(currentNode);
                for (Node node : graph.neighbors(currentNode)) {
                    if (!visited.contains(node))
                        queue.add(node);
                }
            }

            if (visited.contains(stopnode)) {
                break;
            }
        }
        
        Node parentNode = stopnode;
        ArrayList<Node> path = new ArrayList<>();
        
        while (!(parentNode.parent == startnode)) {
            path.add(parentNode);
            parentNode.isPath = true;
            parentNode = parentNode.parent;
        }

        path.add(parentNode);
        parentNode.isPath = true;
        path.add(startnode);
        startnode.isPath = true;

        return path;
    }
}
