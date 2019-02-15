package io.temco.guhada.blockchain.dao;

import io.temco.guhada.blockchain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDao  extends JpaRepository<Transaction, Long> {

}
