package com.blockchain;

public class TransactionInput {
    public String transactionOutputId; // Reference to TransactionOutputs -> transactionId
    public TransactionOutput UTXO; // Contains the Unspent transaction output (link to previous transactions)

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
