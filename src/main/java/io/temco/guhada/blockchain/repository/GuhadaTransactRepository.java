package io.temco.guhada.blockchain.repository;

import io.temco.guhada.blockchain.model.GuhadaTransact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-06-10
 */
public interface GuhadaTransactRepository extends JpaRepository<GuhadaTransact,Integer> {

    List<GuhadaTransact> findAllByProductId(long productId);
}
