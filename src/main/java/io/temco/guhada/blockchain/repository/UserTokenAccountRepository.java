package io.temco.guhada.blockchain.repository;

import io.temco.guhada.blockchain.model.UserTokenAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Shin Han
 * Since 2019-11-22
 */
public interface UserTokenAccountRepository extends JpaRepository<UserTokenAccount, Long> {
}
