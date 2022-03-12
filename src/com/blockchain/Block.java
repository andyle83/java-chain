package com.blockchain;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private final String data;
    private final long timeStamp;
    private int nonce;

    public Block(String data,String previousHash ) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        return Utils.applySha256(previousHash + timeStamp + nonce + data);
    }

    public void mineBlock(int difficulty) {
        String target = Utils.getDifficultyString(difficulty); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }
}
