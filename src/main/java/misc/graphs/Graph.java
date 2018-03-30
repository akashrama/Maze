package misc.graphs;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;
import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import misc.Searcher;
/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    private IDictionary<V, ISet<E>> graphs;
    private int numVertex;
    private int numEdge;
    //private IDisjointSet<V> storage;
    private IList<E> newEdges;
    private ISet<E> add;
    
    private IList<V> vertices;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        this.numVertex = 0;
        this.numEdge = 0;
        //this.storage = new ArrayDisjointSet<V>();
        this.graphs = new ChainedHashDictionary<V, ISet<E>>();
        this.newEdges = Searcher.topKSort(edges.size(), edges);
        this.vertices = vertices;
        ArrayDisjointSet<V> set = new ArrayDisjointSet<V>();
        
        this.add = new ChainedHashSet<E>();
        
        for (V vertex : vertices) {
           numVertex++;
           set.makeSet(vertex);
           
           ISet<E> empty = new ChainedHashSet<>();
           graphs.put(vertex, empty);
           for (E edge: edges) {
               if (edge.getVertex1() == vertex) {
                   if (graphs.get(vertex) == null) {
                       ISet<E> first = new ChainedHashSet<>();
                       first.add(edge);
                       graphs.put(vertex, first);
                   } else {
                       ISet<E> temp = graphs.get(vertex);
                       temp.add(edge);
                       graphs.put(vertex, temp);
                   }
               }
               if (edge.getVertex2() == vertex) {
                   if (graphs.get(vertex)==null) {
                       ISet<E> first = new ChainedHashSet<>();
                       first.add(edge);
                       graphs.put(vertex, first);
                   } else {
                       ISet<E> temp = graphs.get(vertex);
                       temp.add(edge);
                       graphs.put(vertex, temp);
                   }                 
               }
           }
        }
        
        
        
        for (E edge: newEdges) {
            numEdge++;
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            if (!vertices.contains(vertex1) || !vertices.contains(vertex2)) {
                throw new IllegalArgumentException();
            }
            if (set.findSet(vertex1) != set.findSet(vertex2)) {
                set.union(vertex1, vertex2);
                add.add(edge);
                
            }
            
            
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        this(setToList(vertices), setToList(edges));
    }

    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVertex;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdge;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        return add;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IList<E> shortestPath = new DoubleLinkedList<E>();
        IPriorityQueue<E> heap = new ArrayHeap<E>();
        IDictionary<V, Double> weight = new ChainedHashDictionary<>();
        IDictionary<V, IList<E>> path = new ChainedHashDictionary<>();
        
        for (V vertex : vertices) {
            weight.put(vertex, Double.POSITIVE_INFINITY);
        }
        
        if (start.equals(end)) {
            return shortestPath;
        }
        ISet<V> visited = new ChainedHashSet<>();
        ISet<E> visitedEdge = new ChainedHashSet<>();
        visited.add(start);
        weight.put(start, 0.0);
        for (E edge : graphs.get(start)) {
            heap.insert(edge);
            visitedEdge.add(edge);
        }
        path.put(start, new DoubleLinkedList<>());
        while (!heap.isEmpty()) {
            E current = heap.removeMin();
           
            double currWeight = current.getWeight();
            V first;
            V second;
            if (!visited.contains(current.getVertex1())) {
                first = current.getVertex2();
                second = current.getVertex1();
            }
            else {
                first = current.getVertex1();
                second = current.getVertex2();
            }
           
            
            visited.add(second);
            double prevWeight = weight.get(first);
            currWeight = currWeight + prevWeight;
            if (weight.get(second) > currWeight) {
                weight.put(second, currWeight);
                IList<E> travel = new DoubleLinkedList<>();
                for (E edge : path.get(first)) {
                    travel.add(edge);
                }
                travel.add(current);
                path.put(second, travel);
            }
            
            if (second == end) {
                shortestPath = path.get(second);
                return shortestPath;
            }
            
            
            for (E edge : graphs.get(second)) {
                if (!visitedEdge.contains(edge)) {
                    heap.insert(edge);
                    visitedEdge.add(edge);
                }
            }
        }
        throw new NoPathExistsException();        
    }
}