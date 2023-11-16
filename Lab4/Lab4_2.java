package com.company;
import mpi.*;

public class Lab4_2 {

    private static void initMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                matrix[i][j] = (int) (Math.random() * 10);
    }

    private static void initVector(int[] vector) {
        for (int i = 0; i < vector.length; i++)
            vector[i] = (int) (Math.random() * 10);
    }

    private static int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
        int n = matrix.length;
        int[] buf = new int[n * n];
        int[] localResult = new int[n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buf[index] = matrix[j][i] * vector[i];
                index += 1;
            }
        }
        for(int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < buf.length; j++)
                if(j % n == i)
                    sum += buf[j];
            localResult[i] = sum;
        }
        return localResult;
    }
    private static void printVector(int[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i] + " ");
        }
    }

    private static int[] matrixToArray(int[][] array) {
        int rows = array.length;
        int cols = array[0].length;
        int[] flattened = new int[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flattened[i * cols + j] = array[i][j];
            }
        }
        return flattened;
    }

    private static int[][] matrixFrom1DArray(int[] array, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = array[i * cols + j];
            }
        }
        return matrix;
    }

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int n = 1000;
        int[][] matrix = new int[n][n];
        int[] vector = new int[n];
        int[] localVector = new int[n];
        int[] localResult;

        if (rank == 0) {
            initMatrix(matrix);
            initVector(vector);
        }
        long startTime = System.currentTimeMillis();

        MPI.COMM_WORLD.Bcast(matrixToArray(matrix), 0, n * n, MPI.INT, 0);
        MPI.COMM_WORLD.Bcast(vector, 0, n, MPI.INT, 0);

        int blockSize = n / size;
        int remainder = n % size;
        MPI.COMM_WORLD.Scatter(vector, 0, blockSize + (rank == size - 1 ? remainder : 0), MPI.INT, localVector, 0, blockSize + (rank == size - 1 ? remainder : 0), MPI.INT, 0);
        localResult = multiplyMatrixVector(matrixFrom1DArray(matrixToArray(matrix), n, n), localVector);
        int[] globalResult = new int[n];
        MPI.COMM_WORLD.Reduce(localResult, 0, globalResult, 0, localResult.length, MPI.INT, MPI.SUM, 0);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        if (rank == 0) {
            System.out.println("Result vector:");
            printVector(globalResult);
            System.out.println("\nExecution Time: " + executionTime + " milliseconds");
        }
        MPI.Finalize();
    }
}
