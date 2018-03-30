package mazes.generators.maze;

import datastructures.concrete.ChainedHashSet;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import java.util.Random;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        Random rand = new Random();
        
        ISet<Wall> walls = maze.getWalls();
        ISet<Room> rooms = maze.getRooms();
        
        ISet<Wall> kruskal = new ChainedHashSet<>();
        for (Wall edge : walls) {
            edge.setDistance(rand.nextDouble());
            kruskal.add(edge);
        }
        Graph<Room, Wall> g = new Graph<Room, Wall>(rooms, kruskal);
        ISet<Wall> path = g.findMinimumSpanningTree();
        for (Wall edge : walls) {
            edge.resetDistanceToOriginal();
        }
        return path;
    }
}
