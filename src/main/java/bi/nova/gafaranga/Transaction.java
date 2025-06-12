package bi.nova.gafaranga;

public class Transaction {
        private String sender;
        private String recipient;
        private double amount;

        // Default constructor (required for Jackson)
        public Transaction() {}

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

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
