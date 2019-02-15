package io.temco.guhada.blockchain.dao;

import io.temco.guhada.blockchain.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ModelDAO extends JpaRepository<Model, Long>  {

    @Query("select m from MODEL m where m.name = ?1")
    Optional<Model> findByName(String name);

}
