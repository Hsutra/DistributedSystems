package com.company;
import mpi.*;

public class Lab4_1 {

    private static void initMatrix(int[][] matrix) {
        // Инициализация матрицы случайными значениями
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                matrix[i][j] = (int) (Math.random() * 10);
    }

    private static void initVector(int[] vector) {
        // Инициализация вектора случайными значениями
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

    static void printVector(int[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i] + " ");
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int n = 1000;
        int[][] matrix = new int[n][n];
        int[] vector = new int[n];
        int[] result;

        if (rank == 0) {
          initMatrix(matrix);
          initVector(vector);
        }
        int[][] localMatrix = new int[n][n];
        int[] localVector = new int[n];

        MPI.COMM_WORLD.Scatter(matrix, 0, n , MPI.OBJECT, localMatrix, 0, n, MPI.OBJECT, 0);
        MPI.COMM_WORLD.Scatter(vector, 0, n, MPI.INT, localVector, 0, n, MPI.INT, 0);

        result = multiplyMatrixVector(localMatrix, localVector);

        int[] finalResult = new int[n];
        MPI.COMM_WORLD.Gather(result, 0, n , MPI.INT, finalResult, 0, n , MPI.INT, 0);

        if (rank == 0) {
            System.out.print("Matrix-Vector Multiplication Result: ");
            for (int i = 0; i < n; i++) {
                System.out.print(finalResult[i] + " ");
            }
            System.out.println();
        }

        MPI.Finalize();
    }
}
