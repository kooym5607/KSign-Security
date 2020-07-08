package com.company.ARIA;

import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.util.Scanner;

import static com.company.ARIA.ARIAEngine.*;

public class Aria_CTR {
    //CTR 모드
    protected static byte[][] CTR_enc(ARIAEngine aria,byte[][] counter,byte[][] plain_block) throws InvalidKeyException {
        byte[] xor = new byte[16];
        byte[][] cipher_block = new byte[10][16];
        for (int i = 0; i < plain_block.length; i++) { // 1씩 증가하는 카운터를 입력으로 하여 xor연산
            aria.encrypt(counter[i], 0, xor, 0);
            for (int j = 0; j < 16; j++) {
                cipher_block[i][j] = (byte) (xor[j] ^ plain_block[i][j]);
            }
        }
        return cipher_block;
    }
    protected static byte[][] CTR_dec(ARIAEngine aria, byte[][] counter, byte[][] cipher_block) throws InvalidKeyException {
        byte[] xor = new byte[16];
        byte[][] plain_block = new byte[10][16];
        for(int i=0;i<cipher_block.length;i++){
            aria.encrypt(counter[i],0,xor,0);
            for(int j=0;j<16;j++)
                plain_block[i][j] = (byte)(xor[j]^cipher_block[i][j]);
        }
        return plain_block;
    }
    public static void CTR(PrintStream out, Scanner input) throws InvalidKeyException {
        byte[][] plain_block = new byte[10][16];
        byte[][] cipher_block;
        byte[][] counter = new byte[10][16];
        for(int i=0;i<counter.length;i++){
            counter[i][15] = (byte)i;
            counter[i][14] = (byte)(i>>8);
            counter[i][13] = (byte)(i>>16);
            counter[i][12] = (byte)(i>>24);
        }
        out.println("             <<<<< CTR모드 >>>>>\n");
        out.println("사용할 key의 사이즈를 입력. (128 / 192 / 256)bit -> (16 / 24 / 32)byte");
        int keySize = input.nextInt();
        input.nextLine();

        out.println("사용할 암호키 입력.");
        byte[] key = ARIAEngine.hexStringToByteArray(input.nextLine().replaceAll(" ", ""));

        out.println("bit 평문(1입력) / 스트링 평문(2입력)");
        int idx = input.nextInt();
        input.nextLine();

        plain_block = InputPlain(out,input,idx);

        ARIAEngine aria = new ARIAEngine(keySize);
        aria.setKey(key);
        aria.setupRoundKeys();

        out.print("\n  masterkey: ");
        printBlock(out, key);
        out.println();
        out.print("  plaintext : \n");
        printBlock(out,plain_block);

        //CTR모드 암호화
        cipher_block = CTR_enc(aria,counter,plain_block);

        out.print("  ciphertext: \n");
        printBlock(out,cipher_block);

        // CTR모드 복호화
        plain_block = CTR_dec(aria,counter,cipher_block);

        out.print("  복호화한 plaintext: \n");
        printBlock(out,plain_block);
    }
}
