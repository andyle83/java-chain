package com.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<>(); //list of all unspent transactions.

    // Mining difficulty level
    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;

    public static Wallet walletA;
    public static Wallet walletB;
    public static Wallet coinbase;

    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        initWallets();

        initTransaction();

        initBlocks();

        printBlockState();
    }

    private static void printBlockState() {
        String blockchainJson = Utils.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    private static void initBlocks() {
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);
    }

    // Genesis transaction
    private static void initTransaction() {
        // Create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);	 // Manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; // Manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); // It's important to store our first transaction in the UTXOs list.
    }

    public static void initWallets() {
        System.out.println("Init some wallets for testing");

        //Setup Bouncy castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        coinbase = new Wallet();

        // Test public and private keys
        System.out.println("Private and public keys in wallet A :");
        System.out.println(Utils.getStringFromKey(walletA.privateKey));
        System.out.println(Utils.getStringFromKey(walletA.publicKey));

        System.out.println("Private and public keys in wallet B :");
        System.out.println(Utils.getStringFromKey(walletB.privateKey));
        System.out.println(Utils.getStringFromKey(walletB.publicKey));

        System.out.println("Private and public keys in coinbase :");
        System.out.println(Utils.getStringFromKey(walletB.privateKey));
        System.out.println(Utils.getStringFromKey(walletB.publicKey));
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
