package io.temco.guhada.blockchain.dao;

import io.temco.guhada.blockchain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BrandDAO extends JpaRepository<Brand, Long> {

    @Query("select b from BRAND b where b.name = ?1")
    Optional<Brand> findByName(String name);

}
