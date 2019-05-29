pragma solidity^0.4.19;

contract TransactSC {

    struct Record {
        int transactId;
        int productId;
        string hash;
    }

    mapping(int => Record) private byTransactId;
    mapping(int => int[]) private byProductId;

    function insert(int transactId, int productId, string hash) public {
        byTransactId[transactId] = Record(transactId, productId, hash);
        int[] transactions = byProductId[productId];
        transactions.push(transactId);
        byProductId[productId] = transactions;
    }

    function getByProduct(int productId) constant returns(int[]) {
        return byProductId[productId];
    }

    function getHash(int transactId) constant returns(string) {
        return byTransactId[transactId].hash;
    }
}