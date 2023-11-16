package com.company;
import mpi.*;

public class Lab4_3 {
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

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printVector(int[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i] + " ");
        }
    }

    public static void main(String[] args) throws Exception {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int TAG = 0;
        int n = 1000;
        int[][] matrix = new int[n][n];
        int[] vector = new int[n];


        if (myrank == 0) {
            initMatrix(matrix);
            initVector(vector);

            long startTime = System.currentTimeMillis();

            for (int i = 1; i < size; i++) {
                int startIndex = i * n / size;
                int endIndex = (i + 1) * n / size;
                MPI.COMM_WORLD.Send(matrix, startIndex, endIndex - startIndex, MPI.OBJECT, i, TAG);
                MPI.COMM_WORLD.Send(vector, startIndex, endIndex - startIndex, MPI.INT, i, TAG);
            }

            int[] localResult = multiplyMatrixVector(matrix, vector);

            for (int i = 1; i < size; i++) {
                int startIndex = i * n / size;
                int endIndex = (i + 1) * n / size;
                int[] partialResult = new int[endIndex - startIndex];
                MPI.COMM_WORLD.Recv(partialResult, 0, endIndex - startIndex, MPI.INT, i, TAG);
                System.arraycopy(partialResult, 0, localResult, startIndex, endIndex - startIndex);
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("\nResult Vector:");
            printVector(localResult);
            System.out.println("\nTime (Process 0): " + executionTime + " milliseconds");

        } else {
            int startIndex = myrank * n / size;
            int endIndex = (myrank + 1) * n / size;

            int[][] localMatrix = new int[n / size][n];
            int[] localVector = new int[n];
            
            MPI.COMM_WORLD.Recv(localMatrix, 0, endIndex - startIndex, MPI.OBJECT, 0, TAG);
            MPI.COMM_WORLD.Recv(localVector, 0, endIndex - startIndex, MPI.INT, 0, TAG);

            int[] partialResult = multiplyMatrixVector(localMatrix, localVector);
            MPI.COMM_WORLD.Send(partialResult, 0, endIndex - startIndex, MPI.INT, 0, TAG);


        }
        MPI.Finalize();
    }
}
