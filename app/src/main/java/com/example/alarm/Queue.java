package com.example.alarm;

public class Queue {

    String[] Q;
    int head;
    int tail;
    int Qsize;
    int howManyDates = 0;

    public Queue(){
        Q = new String[10];
        head = 0;
        tail = -1;
        Qsize = 10;
    }

    public void enQ(String c){
        if(tail == Qsize){
            increaseSize();
        }

        tail++;
        Q[tail] = c;
        howManyDates++;
    }

    public String front(){
        return Q[head];
    }

    public String deQ(){
        String current = Q[head];
        Q[head] = null;
        head ++;
        howManyDates--;
        return current;
    }

    private void increaseSize(){
        String[] nQ = new String[Qsize*2];
        Qsize *= 2;

        for (int i = 0; i < Qsize; i++) {
            nQ[i] = Q[i];
        }
        Q = nQ;
    }





}
