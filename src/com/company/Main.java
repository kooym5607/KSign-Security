package com.company;

import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.util.Scanner;

import com.company.ARIA.ARIA;

public class Main {
    public static void main (String args[]) throws InvalidKeyException {
        Scanner input = new Scanner(System.in);
        PrintStream out=System.out;
        out.println("1: SEED / 2: ARIA");
        int num = input.nextInt();
        switch (num){
            case 1: break;
            case 2: ARIA.main(args);break;
        }

        input.close();
        out.close();
    }
}
