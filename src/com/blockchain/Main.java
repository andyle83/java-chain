package com.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //list of all unspent transactions.

    // Mining difficulty level
    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;

    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
        initBlocks();

        initWallets();

        // Create a test transaction from WalletA to walletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);
        // Verify the signature works and verify it from the public key
        System.out.println("Is signature verified");
        System.out.println(transaction.verifySignature());
    }

    public static void initWallets() {
        System.out.println("Init some wallets for testing");

        //Setup Bouncy castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();

        // Test public and private keys
        System.out.println("Private and public keys in wallet A :");
        System.out.println(Utils.getStringFromKey(walletA.privateKey));
        System.out.println(Utils.getStringFromKey(walletA.publicKey));

        System.out.println("Private and public keys in wallet B :");
        System.out.println(Utils.getStringFromKey(walletB.privateKey));
        System.out.println(Utils.getStringFromKey(walletB.publicKey));
    }

    public static void initBlocks() {
        // Add our blocks to the blockchain ArrayList
        System.out.println("Trying to Mine block 1... ");
        addBlock(new Block("Hi im the first block", "0"));

        System.out.println("Trying to Mine block 2... ");
        addBlock(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));

        System.out.println("Trying to Mine block 3... ");
        addBlock(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = Utils.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    public static Boolean isChainValid() {
        Block currentBlock, previousBlock;

        for(int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.print("Previous Hashes not equal");
                return false;
            }
        }

        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
