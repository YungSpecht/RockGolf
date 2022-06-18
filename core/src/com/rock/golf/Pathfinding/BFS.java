package com.rock.golf.Pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.rock.golf.Cell;
import com.rock.golf.RockGolf;
import com.rock.golf.Bot.StochasticBot;
import com.rock.golf.Physics.Engine.PhysicsEngine;

public class BFS {
    StochasticBot bot = new StochasticBot((PhysicsEngine) RockGolf.engine, 1000);
    int sizeX = (int) RockGolf.width;
    int sizeY = (int) RockGolf.height;
    float originX = sizeX / 2;
    float originY = sizeY / 2;
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

    public double[] BFSBot(Graph graph, Node startnode, Node stopnode) {
        ArrayList<Node> path = BFSSearch(graph, startnode, stopnode);
        for (int i = 0; i < path.size(); i++) {
            double nodeX = (path.get(i).row * 10 - originX) / metertoPixelRatio;
            double nodeY = (path.get(i).column * 10 - originY) / metertoPixelRatio;
            double[] nodePosition = new double[] { nodeX, nodeY };
            double[] move = bot.getMoveTarget(nodePosition);
            if (move[2] < 0.05) {
                return move;
            } else {
                System.out.println("Node " + (i + 1) + " not shootable. Retrying...");
            }
        }
        return new double[] { 0, 0 };
    }
}
