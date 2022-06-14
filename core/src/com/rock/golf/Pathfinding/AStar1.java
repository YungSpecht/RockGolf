package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

import com.rock.golf.RockGolf;

public class AStar1 {
    

    /**
     * Euclidean distance is used for heuristic function
     * @param current
     * @param goal
     * @return
     */
    private static double heuristic(Node current, Node goal){
        return current.calculateEuclidean(goal);
    }

    private static double manhattanDistance(Node current, Node goal){
        return Math.abs(current.row-goal.row)+Math.abs(current.column-goal.column);
    }

    public static ArrayList<Node> findPath(Node start, Node goal, Graph graph){
        PriorityQueue<Node> open = new PriorityQueue<Node>(graph);
        HashMap<Node, Double> costMap = new HashMap<Node,Double>();
        HashMap<Node, Node> predecessors = new HashMap<Node, Node>();
        open.add(start);
        predecessors.put(start, null);
        costMap.put(start, 0.0);
        
        while(!open.isEmpty()){
            Node current =open.remove();
            if (current==goal){
                break;
            }
            for (Node next : graph.neighbors(current)) {
                double currentCost = costMap.get(current) + manhattanDistance(current, next)+heuristic(next, goal)-heuristic(current, goal);
                if(!costMap.containsKey(next) || currentCost<costMap.get(next)){
                    costMap.put(next, currentCost);
                    predecessors.put(next, current);
                    open.add(next);
                }
            }
        }
        Node current = goal;
        ArrayList<Node> path = new ArrayList<Node>();
        while(current!=start){
            current.isPath=true;
            path.add(current);
            current=predecessors.get(current);
        }
        path.add(start);
        start.isPath=true;
        Collections.reverse(path);
        return path;
    }

   

  
}
