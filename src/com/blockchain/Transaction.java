package com.blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionOutput> outputs;

    private static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return Utils.applySha256(
                Utils.getStringFromKey(sender) +
                        Utils.getStringFromKey(recipient) +
                        value + sequence
        );
    }

    // Signs all the data we don't wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(recipient) + value;
        signature = Utils.applyECDSASig(privateKey,data);
    }

    // Verifies the data we signed hasn't been tampered with
    public boolean verifySignature() {
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(recipient) + value;
        return Utils.verifyECDSASig(sender, data, signature);
    }
}
