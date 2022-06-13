package com;

import java.util.ArrayList;

public class Tree<Node> {
    private TreeNode<Node> root;
    
    public Tree(Node rootData) {
        root = new TreeNode<Node>();
        root.data = rootData;
        root.children = new ArrayList<TreeNode<Node>>();
    }
}
