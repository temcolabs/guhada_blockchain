pragma solidity^0.5.6;

contract TrackRecordSC{

    struct Record {
        int transactId;
        int productId;
        string productName;
        string brand;
        int price;
        string color;
        string owner;
        string hash;
    }

    mapping(int => Record) private byTransactId;
    mapping(int => int[]) private byProductId;

    function insert(int transactId, int productId,  string memory productName, string memory brand,
        int price, string memory color, string memory owner, string memory hash) public {
        byTransactId[transactId] = Record(transactId, productId, productName, brand, price, color, owner, hash);
        int[] storage transactions = byProductId[productId];
        transactions.push(transactId);
        byProductId[productId] = transactions;
    }

    function getByProduct(int productId) public view returns(int[] memory) {
        return byProductId[productId];
    }

    function getHash(int transactId) public view returns(string memory) {
        return byTransactId[transactId].hash;
    }
}