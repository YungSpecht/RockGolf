package com.rock.golf.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class BFS extends Bot{
    
    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(list.toArray()));
    }

    
    static Integer[][] adjacencyMatrix = {
        {0,0,0,1},
        {0,0,0,1},
        {0,0,0,1},
        {1,1,1,0}
    };

    static List<Integer[]> adjacencyList = Arrays.asList(adjacencyMatrix);

    

    // With breadth-first search you need a starting vertex
    // Begins explores all its neighbors before exploring vertices on next depth level
 
    public List<Integer> BFS(List<Integer[]> list, Integer startnode) {
        Queue<Integer> visited = new LinkedList<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startnode);

        while(!queue.isEmpty()){
        
            Integer currentNode = queue.poll();
            
            if(!visited.contains(currentNode)){
                visited.add(currentNode);
                queue.addAll(adjacencyList.neighbors(currentNode));
                for(Integer adjacent : adjacencyList.neighbors(currentNode)){
                    if(!visited.contains(adjacent)) queue.add(adjacent);
                }
            }
            
        }
        return (List<Integer>) visited;
    }

    

    @Override
    public double[] getMove() {
        // TODO Auto-generated method stub
        return null;
    }

    
}
