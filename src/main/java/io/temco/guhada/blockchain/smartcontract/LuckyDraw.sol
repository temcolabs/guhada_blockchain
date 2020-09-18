pragma solidity ^0.5.6;

contract LuckyDraw {

    struct EntryUser {
        uint dealId;
        uint userId;
        string userEmail;
    }
    
    mapping(string => EntryUser) public entryUsers; // 응모한 유저정보
    mapping(uint => string[]) public luckyDrawEntrys; // 게임(딜아이디안에 게임에 응모한 유저유니크 키값을 저장)
    mapping(uint => string) public luckyDrawWinner;  // 게임 당첨자정보(해당 게임의 딜 아이디로 당첨자 확인)
    
    function entry(string memory eventId, uint dealId, uint userId, string memory userEmail) public{
        entryUsers[eventId] = EntryUser(dealId, userId, userEmail);
        string[] storage transactions = luckyDrawEntrys[dealId];
        transactions.push(eventId);
    }
    
    function draw(uint dealId) public returns(string memory){
        uint limit = luckyDrawEntrys[dealId].length;
        uint random = randomNumber(limit);
        luckyDrawWinner[dealId] = luckyDrawEntrys[dealId][random];
    }
    
    function randomNumber(uint limit) internal view returns (uint) {
        return uint8(uint256(keccak256(abi.encodePacked(block.timestamp, block.difficulty)))%limit);
    
    }
    
    function destoryDrawItem(uint dealId) public{
        delete luckyDrawEntrys[dealId];
    }

}

