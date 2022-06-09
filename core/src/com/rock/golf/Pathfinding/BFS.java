package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public Queue<Integer> BFS(Graph graph, int startnode) {
        Queue<Integer> visited = new LinkedList<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startnode);
        while(!queue.isEmpty()){
            int currentNode = queue.poll();
            
            if(!visited.contains(currentNode)){
                visited.add(currentNode);
                queue.addAll(graph.neighbors(currentNode));
                for(int adjacent : graph.neighbors(currentNode)){
                    if(!visited.contains(adjacent)) queue.add(adjacent);
                }
            }
            
        }
        return visited;
    }

}
