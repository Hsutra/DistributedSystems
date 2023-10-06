package com.company;
import mpi.*;

public class Lab1 {
    public static void main(String[] args) {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int message = myrank;
        int TAG = 0;
        if ((myrank % 2) == 0) {
            if ((myrank + 1) != size) {
                int dest = myrank + 1;
                MPI.COMM_WORLD.Send(new int[] { message }, 0, 1, MPI.INT, dest, TAG);
            }
        } else {
            if (myrank != 0) {
                int source = myrank - 1;
                int[] recvBuf = new int[1];
                MPI.COMM_WORLD.Recv(recvBuf, 0, 1, MPI.INT, source, TAG);
                message = recvBuf[0];
                System.out.println(message);
            }
        }
        MPI.Finalize();
    }
}

