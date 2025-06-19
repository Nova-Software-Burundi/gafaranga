# Gafaranga Blockchain

Gafaranga is a Java-based educational blockchain prototype inspired by CELO's mobile-first, lightweight approach to financial infrastructure. This project aims to explore, demonstrate, and eventually evolve into a robust blockchain platform that integrates modern crypto-economic mechanisms with the reliability and clarity of the Java programming language.

---

## 🚀 Vision

Gafaranga sets out to be:

- A **developer-friendly blockchain** system written in Java, making blockchain logic accessible to millions of enterprise and academic Java programmers.
- A prototype that follows CELO's best features: mobile optimization, lightweight consensus, stable token flow, and economic incentives.
- A foundation for experimentation with novel tokenomics, governance models, and mobile-friendly architectures.

---

## 🧱 Core Features

- ✅ **Basic Blockchain Engine**: A fully functional blockchain with block creation, hash chaining, and transaction tracking.
- ✅ **Account-Based Ledger**: Wallets and balances are tracked across the chain, with real-time state updates.
- ✅ **Mining Reward System**: Each mined block rewards the miner with a fixed amount of native tokens.
- ✅ **RESTful API (Javalin)**: Simple HTTP endpoints to interact with the blockchain (view chain, balances, send transactions, mine).
- ✅ **In-Memory Ledger**: Uses LevelDB or in-memory maps for fast prototyping, and can evolve to persistent state handling.
- 🔜 **Smart Contract Engine (Planned)**: Future support for running programmable logic securely inside the chain.

---

## 💡 Why Java?

- ✨ **Clarity & Maturity**: Java is battle-tested, object-oriented, and ideal for building clean, extensible architectures.
- 🧩 **Ecosystem**: Rich libraries, tools like IntelliJ IDEA, and strong JVM-based persistence options (LevelDB, RocksDB).
- 🔧 **Educational Value**: Java is the go-to language in many universities and corporate environments — Gafaranga lowers the entry barrier for Java developers into Web3.

---

## 🔁 Inspired by CELO

CELO has shown how blockchain can be:

- 🌍 **Mobile-First**: Designed to work on devices with limited resources
- ⚡ **Fast & Lightweight**: Efficient consensus and stable currency infrastructure
- 🎁 **Economically Inclusive**: Tokenomics designed to support access and equity

Gafaranga borrows these principles as a technical base, while rethinking the developer experience in a Java-native ecosystem.

---

## 📦 Technologies Used

- **Java 17+**
- **Javalin** – lightweight web server for REST APIs
- **LevelDB** – fast, embeddable key-value store (planned)
- **GitHub + IntelliJ IDEA** – for modern Java project management

---

## 🛠️ Getting Started

To run the project:

```bash
git clone https://github.com/your-org/gafaranga.git
cd gafaranga
./gradlew run
