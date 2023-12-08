package com.company;
import mpi.MPI;

import java.util.Arrays;


public class Lab5 {

    public static boolean isTorusGraph(int[][] adjacencyMatrix) {
        int numVertices = adjacencyMatrix.length;
        if (numVertices == 0) {
            return false; // Граф не может быть тором, если он пустой.
        }

        boolean isConnected = isConnected(adjacencyMatrix);
        boolean hasEulerianCircuit = hasEulerianCircuit(adjacencyMatrix);

        return isConnected && hasEulerianCircuit;
    }

    public static boolean isConnected(int[][] adjacencyMatrix) {
        int numVertices = adjacencyMatrix.length;
        boolean[] visited = new boolean[numVertices];

        // Пометим все вершины как непосещенные
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        // Выберем первую вершину в качестве начальной
        int startVertex = 0;

        // Выполним обход в глубину
        dfs(adjacencyMatrix, startVertex, visited);

        // Проверим, все ли вершины были посещены
        for (boolean v : visited) {
            if (!v) {
                return false; // Найдена непосещенная вершина, граф несвязный
            }
        }
        return true; // Все вершины были посещены, граф связный
    }

    private static void dfs(int[][] adjacencyMatrix, int vertex, boolean[] visited) {
        visited[vertex] = true;

        for (int neighbor = 0; neighbor < adjacencyMatrix.length; neighbor++) {
            if (adjacencyMatrix[vertex][neighbor] == 1 && !visited[neighbor]) {
                dfs(adjacencyMatrix, neighbor, visited);
            }
        }
    }

    // Метод для проверки наличия эйлерова цикла в графе на основе матрицы смежности
    private static boolean hasEulerianCircuit(int[][] adjacencyMatrix) {
        // Ваш код для проверки наличия эйлерова цикла в графе
        int numVertices = adjacencyMatrix.length;
        int oddDegreeCount = 0;

        for (int i = 0; i < numVertices; i++) {
            int degree = 0;
            for (int j = 0; j < numVertices; j++) {
                degree += adjacencyMatrix[i][j];
            }
            if (degree % 2 != 0) {
                oddDegreeCount++;
            }
        }
        return oddDegreeCount == 0 || oddDegreeCount == 2;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[][] adjacencyMatrix = {
                {0, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 0}
        };


        boolean isTorus = isTorusGraph(adjacencyMatrix);
        boolean[] localResults = new boolean[] { isTorus };
        boolean[] results = new boolean[size];

        MPI.COMM_WORLD.Gather(localResults, 0, 1, MPI.BOOLEAN, results, 0, 1, MPI.BOOLEAN, 0);

        if (rank == 0) {
            boolean finalResult = true;
            for (boolean result : results) {
                finalResult &= result;
            }
            System.out.println("Is the graph a torus?: " + (finalResult ? "Yes" : "No"));
        }
        MPI.Finalize();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime; // Получаем время выполнения в миллисекундах
        System.out.println("Execution time: " + executionTime + " milliseconds" + ", rank = " + rank);

    }
}

