//package io.temco.guhada.blockchain.repository;
//
//import io.temco.guhada.blockchain.model.Transact;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface TransactRepository extends JpaRepository<Transact,Long> {
//
//    List<Transact> findByContractAddressIsNull();
//
//    Transact findByContractAddress(String ContractAddress);
//}
