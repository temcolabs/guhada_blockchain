package io.temco.guhada.blockchain.repository;

import io.temco.guhada.blockchain.model.ApiLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLimitRepository extends JpaRepository<ApiLimit,Long> {

    ApiLimit findByApiToken(String apiToken);

}
