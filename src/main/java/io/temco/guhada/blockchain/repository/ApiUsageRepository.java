//package io.temco.guhada.blockchain.repository;
//
//import io.temco.guhada.blockchain.model.ApiUsage;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface ApiUsageRepository extends JpaRepository<ApiUsage,Long> {
//
//    ApiUsage findByCompanyIdAndMonthAndDay(int companyIdA, int month, int day);
//
//    @Query("select sum(usageCount) from ApiUsage where companyId = :companyId AND month = :month")
//    Integer sumMonthUsage(@Param("companyId") int companyId, @Param("month") int month );
//}
