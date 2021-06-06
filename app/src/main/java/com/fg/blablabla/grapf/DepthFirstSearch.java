package com.fg.blablabla.grapf;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DepthFirstSearch {

    public DepthFirstSearch(OnFinish onFinish) {
        this.onFinish = onFinish;
    }

    private final OnFinish onFinish;

    public static class Vertex
    {
        public int data;
        public boolean visited;
        List<Vertex> edge;

        public Vertex(int data)
        {
            this.data=data;
            this.edge =new ArrayList<>();

        }
        public void addEdge(Vertex vertex)
        {
            this.edge.add(vertex);
        }

        public void removeEdge(Vertex vertex)
        {
            this.edge.remove(vertex);
        }
        public List<Vertex> getEdge() {
            return edge;
        }
        public void setEdges(List<Vertex> edge) {
            this.edge = edge;
        }
    }

    public final List<Integer> directions = new ArrayList<>();
    public int endPos = 0;



    // Recursive DFS
    public  void dfs(Vertex node)
    {
        Log.e("QQ",node.data + " ");
        directions.add(node.data);
        List<Vertex> edges=node.getEdge();
        node.visited=true;
        for (int i = 0; i < edges.size(); i++) {
            Vertex n=edges.get(i);
            if(n!=null && !n.visited) {
                dfs(n);
                return;
            }
        }
        Log.e("QQ", "END");
        if (onFinish != null){
            onFinish.isFinish(isEnd(endPos));
        }
    }

    private boolean isEnd(int endPos){
        for (int pos : directions){
            if (pos == endPos){
                return true;
            }
        }
        return false;
    }

    // Iterative DFS using stack
    public  void dfsUsingStack(Vertex node)
    {
        Stack<Vertex> stack=new  Stack<Vertex>();
        stack.add(node);
        while (!stack.isEmpty())
        {
            Vertex element=stack.pop();
            if(!element.visited)
            {
                Log.e("QQ",  element.data + " ");
                element.visited=true;
            }

            List<Vertex> neighbours=element.getEdge();
            for (int i = 0; i < neighbours.size(); i++) {
                Vertex n=neighbours.get(i);
                if(n!=null && !n.visited)
                {
                    stack.add(n);
                }
            }
        }
    }

    public interface OnFinish{
        void isFinish(boolean isFinish);
    }
}