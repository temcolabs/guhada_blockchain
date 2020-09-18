pragma solidity^0.5.6;

contract TrackRecordSC{

    struct Record {
        uint orderId;
        uint dealId;
        string serialNumber;
        string productName;
        string brand;
        uint price;
        string color;
        string owner;
        string hash;
    }

    mapping(uint => Record[]) private dealList;
    
    function addTransaction(uint orderId, uint dealId, string memory serialNumber, string memory productName, string memory brand,
        uint price, string memory color, string memory owner, string memory hash) public {
        dealList[dealId].push(Record(orderId, dealId, serialNumber, productName, brand, price, color, owner, hash));
    }

    function getDealSize(uint dealId) public view returns(uint) {
        return dealList[dealId].length;
    }
    
    function getDeal(uint dealId, uint index) public view returns(uint, uint, string memory, string memory, string memory, uint, 
        string memory, string memory, string memory){
        Record memory dealRecord = dealList[dealId][index];
        return (dealRecord.orderId, dealRecord.dealId, dealRecord.serialNumber, dealRecord.productName, dealRecord.brand, 
            dealRecord.price, dealRecord.color, dealRecord.owner, dealRecord.hash);
    }

}