package io.temco.guhada.blockchain.dao;

import io.temco.guhada.blockchain.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SellerDAO extends JpaRepository<Seller, Long> {

    @Query("select s from SELLER s where s.email = ?1")
    Optional<Seller> findByEmail(String email);

}
