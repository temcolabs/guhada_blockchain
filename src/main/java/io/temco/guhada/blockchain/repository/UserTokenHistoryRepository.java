package io.temco.guhada.blockchain.repository;

import io.temco.guhada.blockchain.model.UserTokenHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Shin Han
 * Since 2019-11-22
 */
public interface UserTokenHistoryRepository extends JpaRepository<UserTokenHistory, Long> {

}
