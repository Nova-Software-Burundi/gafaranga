package bi.nova.gafaranga;

import java.math.BigDecimal;

public class Transaction {
        public String sender;
        public String recipient;
        public BigDecimal amount;
        private long timestamp = System.currentTimeMillis();
        private String txId;

        // Default constructor (required for Jackson)
        public Transaction() {}

        public Transaction(String sender, String recipient, BigDecimal amount) {
            this.sender = sender;
            this.recipient = recipient;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
            this.txId = generateTxId();
        }

        public String generateTxId() {
            String data = sender + recipient + amount + timestamp;
            return StringUtil.applySha256(data);
        }

    // Getters and setters
        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getTxId() {
            return txId;
        }

        public void setTxId(String txId) {
            this.txId = txId;
        }
}
