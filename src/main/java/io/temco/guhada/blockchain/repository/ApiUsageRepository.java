package io.temco.guhada.blockchain.repository;

import io.temco.guhada.blockchain.model.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApiUsageRepository extends JpaRepository<ApiUsage,Long> {

    ApiUsage findByApiLimitIdAndMonthAndDay(int apiLimitId, int month, int day);


    @Query("select sum(USAGE_COUNT) from API_USAGE where API_LIMIT_ID = :apiLimitId AND MONTH = :month")
    int sumMonthUsage(@Param("apiLimitId") int apiLimitId, @Param("month") int month );
}
