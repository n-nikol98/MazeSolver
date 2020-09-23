import com.mazesolver.MazeSolver;
import com.mazesolver.dijkstra.DijkstraMazeSolver;
import com.mazesolver.exception.*;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.stream.IntStream;

/**
 * A class to execute as many VALID Maze Tests as you want.
 * All that needs to be modified is the mazes file.
 *
 * */
@SuppressWarnings("Convert2Diamond")
@RunWith(Parameterized.class)
public class MultiMazeTest {

    /**
     * Integer[][] {Key} - Maze
     *
     * Integer[] {Value}:
     *      Integer[0] - X Coordinate
     *      Integer[1] - Y Coordinate
     *      Integer[2] - expectedResult
     * */
    private static final Map<Integer[][], Integer[]> mazes =
            new LinkedHashMap<Integer[][], Integer[]>() {{

                put(new Integer[][] {
                                {0, 11, 0, 0, 0, 2},
                                {0, 15, 0, 0, 0, 5},
                                {0, 1, 0, 0, 0, 5},
                                {0, 9, 1, 4, 1, 0},
                                {0, 2, 0, 9, 0, 0},
                                {0, 3, 0, 7, 0, 0}
                }, new Integer[] {2, 3, 15}
                );

                put(new Integer[][] {
                                {0, 1, 0, 0, 0, 1, 0, 4, 0},
                                {0, 2, 0, 0, 0, 5, 1, 1, 0},
                                {0, 1, 0, 0, 0, 5, 0, 0, 0},
                                {0, 1, 1, 1, 1, 1, 2, 6, 6},
                                {0, 2, 0, 0, 0, 0, 1, 0, 0},
                                {0, 1, 0, 7, 0, 0, 3, 0, 0},
                                {0, 2, 2, 6, 0, 0, 1, 0, 0},
                                {0, 1, 0, 1, 5, 1, 1, 0, 0},
                                {1, 0, 0, 0, 0, 0, 0, 0, 0}
                }, new Integer[]{6, 3, 11}
                );

                put(new Integer[][] {
                                {0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 1, 0, 2, 0, 0, 0, 1, 4, 1},
                                {0, 1, 5, 4, 0, 1, 0, 0, 0, 5, 0, 0},
                                {1, 6, 0, 1, 9, 1, 8, 1, 6, 1, 0, 0},
                                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                                {0, 0, 0, 14, 0, 0, 0, 1, 0, 0, 0, 0},
                                {0, 11, 1, 1, 0, 0, 0, 8, 0, 0, 0, 0},
                                {1, 1, 0, 0, 0, 0, 0, 1, 20, 1, 0, 0},
                                {0, 32, 0, 0, 1, 1, 0, 0, 0, 4, 0, 0},
                                {0, 1, 1, 1, 1, 0, 0, 0, 3, 1, 1, 0},
                                {0, 0, 9, 0, 0, 0, 0, 0, 0, 21, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}
                }, new Integer[]{3, 5, 29}
                );

                put(new Integer[][] {
                                {0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0},
                                {1, 1, 1, 1, 0, 0},
                                {1, 1, 1, 1, 0, 0},
                                {1, 1, 1, 1, 0, 0},
                                {0, 0, 0, 0, 0, 0}
                        }, new Integer[] {3, 3, 4}
                );
            }};

    @SuppressWarnings("unchecked")
    @Parameterized.Parameters(name = "maze:{0} | x:{1} | y:{2}, expectedResult:{3}")
    public static Collection<Object[]> data() {
        var result = new ArrayList();
        for (int i = 0; i < mazes.size(); i++) {

            final Integer[][] maze = (Integer[][]) mazes.keySet().toArray()[i];
            final Integer[] mazeAssociatedValues = mazes.get(maze);

            final int x = mazeAssociatedValues[0],
                    y = mazeAssociatedValues[1],
                    expectedResult = mazeAssociatedValues[2];

            final int[][] int_maze = new int[maze.length][];

            IntStream.range(0, maze.length).forEach(index ->
                    int_maze[index] = ArrayUtils.toPrimitive(maze[index]));

            result.add(new Object[] {int_maze, x, y, expectedResult});
        }

        return result;
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Parameterized.Parameter(0)
    public int[][] maze;
    @Parameterized.Parameter(1)
    public int x;
    @Parameterized.Parameter(2)
    public int y;
    @Parameterized.Parameter(3)
    public int expectedResult;

    @Test
    public void MultiTest() {
        MazeSolver solver = new DijkstraMazeSolver();

        int result = -1;

        try {
            result = solver.solve(maze, x, y);
        } catch (MalformedMazeException |
                InvalidMazeCoordinatesException |
                InvalidMazeStartPositionException |
                EmptyMazeException |
                InescapableMazeException |
                InvalidMazeValuesException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(expectedResult, result);
    }
}
