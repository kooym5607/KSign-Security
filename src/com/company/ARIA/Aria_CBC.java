package com.company.ARIA;

import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.util.Scanner;
import static com.company.ARIA.ARIAEngine.*;

public class Aria_CBC {
    protected static byte[][] CBC_enc(ARIAEngine aria, byte[] primeVec, byte[][] plain_block) throws InvalidKeyException {
        byte[] xor = new byte[16];
        byte[][] cipher_block = new byte[10][16];
        for (int i = 0; i < plain_block.length; i++) { // 초기 벡터와 이전 암호문을 이용한 xor.
            if(i==0) {
                for (int j = 0; j < 16; j++) {
                    xor[j] = (byte) (primeVec[j] ^ plain_block[i][j]);
                }
                aria.encrypt(xor, 0, cipher_block[i], 0);
            }
            else{
                for (int j = 0; j < 16; j++) {
                    xor[j] = (byte) (cipher_block[i-1][j] ^ plain_block[i][j]);
                }
                aria.encrypt(xor, 0, cipher_block[i], 0);
            }
        }
        return plain_block;
    }
    protected static byte[][] CBC_dec(ARIAEngine aria, byte[] primeVec, byte[][] cipher_block) throws InvalidKeyException {
        byte[] xor = new byte[16];
        byte[][] plain_block = new byte[10][16];
        for(int i=0;i<cipher_block.length;i++){
            aria.decrypt(cipher_block[i],0,xor,0);
            if (i == 0)
                for(int j=0;j<16;j++)
                    plain_block[i][j] = (byte) (primeVec[j] ^ cipher_block[i][j]);
            else
                for(int j=0;j<16;j++)
                    plain_block[i][j] = (byte) (cipher_block[i-1][j] ^ xor[j]);
        }
        return plain_block;
    }
    public static void CBC(PrintStream out, Scanner input) throws InvalidKeyException {
        byte[][] plain_block;
        byte[][] cipher_block;

        out.println("             <<<<< CBC모드 >>>>>\n");
        out.println("사용할 key의 사이즈를 입력. (128 / 192 / 256)bit -> (16 / 24 / 32)byte");
        int keySize = input.nextInt();
        input.nextLine();

        out.println("사용할 암호키 입력.");
        byte[] key = ARIAEngine.hexStringToByteArray(input.nextLine().replaceAll(" ", ""));

        out.println("초기 벡터(IV) 입력.");
        byte[] primeVec = ARIAEngine.hexStringToByteArray(input.nextLine().replaceAll(" ", ""));

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
        //CBC모드 암호화
        cipher_block = CBC_enc(aria,primeVec,plain_block);

        out.print("  ciphertext: \n");
        printBlock(out,cipher_block);

        //CBC모드 복호화
        plain_block = CBC_dec(aria,primeVec,cipher_block);

        out.print("  복호화한 plaintext: \n");
        printBlock(out,plain_block);
    }
}
