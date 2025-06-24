const api = {
  fetchChain: () => fetch('/chain').then(r => r.json()),
  fetchBlock: index => fetch(`/block/${index}`).then(r => r.json()),
  fetchAllTransactions: () => fetch('/transactions').then(r => r.json()),
  fetchTransactionsForAddress: addr => fetch(`/transactions/${addr}`).then(r => r.json()),
  fetchBalance: addr => fetch(`/balance/${addr}`).then(r => r.json())
};
