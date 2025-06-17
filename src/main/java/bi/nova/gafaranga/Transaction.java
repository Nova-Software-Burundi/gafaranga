package bi.nova.gafaranga;

import java.math.BigDecimal;

public class Transaction {
        public String sender;
        public String recipient;
        public BigDecimal amount;

        // Default constructor (required for Jackson)
        public Transaction() {}

        public Transaction(String from, String to, BigDecimal amount) {
            this.sender = from;
            this.recipient = to;
            this.amount = amount;
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
    }
