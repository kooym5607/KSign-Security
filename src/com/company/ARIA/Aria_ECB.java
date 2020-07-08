package com.company.ARIA;

import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.util.Scanner;
import static com.company.ARIA.ARIAEngine.*;

public class Aria_ECB {
    protected static byte[][] ECB_enc(ARIAEngine aria, byte[][] plain_block) throws InvalidKeyException {
        byte[][] cipher_block = new byte[10][16];
        for(int i=0;i<plain_block.length;i++)
            aria.encrypt(plain_block[i], 0, cipher_block[i], 0);
        return cipher_block;
    }
    protected static byte[][] ECB_dec(ARIAEngine aria, byte[][] cipher_block) throws InvalidKeyException {
        byte[][] plain_block = new byte[10][16];
        for(int i=0;i<cipher_block.length;i++)
            aria.decrypt(cipher_block[i],0,plain_block[i],0);
        return plain_block;
    }
    public static void ECB(PrintStream out, Scanner input) throws InvalidKeyException {
        byte[][] plain_block = new byte[10][16];
        byte[][] cipher_block = new byte[10][16];
        out.println("             <<<<< ECB모드 >>>>>\n");

        out.println("사용할 key의 사이즈를 입력. (128 / 192 / 256)bit -> (16 / 24 / 32)byte");
        int keySize = input.nextInt();
        input.nextLine();

        out.println("사용할 암호키 입력.");
        byte[] key = ARIAEngine.hexStringToByteArray(input.nextLine().replaceAll(" ",""));

        out.println("bit 평문(1입력) / 스트링 평문(2입력)");
        int idx = input.nextInt();
        input.nextLine();
        plain_block = InputPlain(out,input,idx);

        ARIAEngine aria = new ARIAEngine(keySize);
        aria.setKey(key);
        aria.setupRoundKeys();

        out.print("\n  masterkey: "); printBlock(out, key); out.println();
        out.print("  plaintext : \n");
        printBlock(out,plain_block);

        // ECB 모드 암호화
        cipher_block = ECB_enc(aria,plain_block);

        out.print("  ciphertext: \n");
        printBlock(out,cipher_block);

        // ECB 모드 복호화
        plain_block = ECB_dec(aria,cipher_block);

        out.print("  복호화한 plaintext: \n");
        printBlock(out,plain_block);
    }
}
