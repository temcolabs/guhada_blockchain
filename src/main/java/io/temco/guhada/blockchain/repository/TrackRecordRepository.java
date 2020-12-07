package io.temco.guhada.blockchain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.temco.guhada.blockchain.model.request.TrackRecord;

public interface TrackRecordRepository extends JpaRepository<TrackRecord,Long> {

	Optional<TrackRecord> findTopByDealIdOrderByCreatedAtDesc(Long dealId);
}
