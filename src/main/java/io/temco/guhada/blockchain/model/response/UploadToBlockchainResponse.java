//package io.temco.guhada.blockchain.model.response;
//
//import io.temco.guhada.blockchain.model.Transact;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class UploadToBlockchainResponse {
//    private long transactId;
//    private String contractAddress;
//    private String hash;
//
//    public static UploadToBlockchainResponse of(Transact transact){
//        UploadToBlockchainResponse uploadToBlockchainResponse = new UploadToBlockchainResponse();
//        uploadToBlockchainResponse.transactId = transact.getTransactId();
//        uploadToBlockchainResponse.contractAddress = transact.getContractAddress();
//        uploadToBlockchainResponse.hash = transact.getHash();
//        return uploadToBlockchainResponse;
//    }
//}
