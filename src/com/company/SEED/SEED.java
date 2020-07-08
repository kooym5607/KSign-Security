package com.company.SEED;

import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.util.Scanner;

public class SEED {
    public static void main (String args[]) throws InvalidKeyException {
        Scanner input = new Scanner(System.in);
        PrintStream out=System.out;

        out.println("--- 모드를 설정해주세요 ---\n 1 - ECB / 2 - CBC / 3 - CTR");
        int mode = input.nextInt();
        switch(mode){
            case 1: KISA_SEED_ECB.main(args);
                break;
            case 2: KISA_SEED_CBC.main(args);
                break;
            case 3: KISA_SEED_CTR.main(args);
                break;
        }

        input.close();
        out.close();
    }
}
