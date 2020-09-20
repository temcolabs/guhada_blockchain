package io.temco.guhada.blockchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.temco.guhada.blockchain.model.request.TrackRecord;

public interface TrackRecordRepository extends JpaRepository<TrackRecord,Integer> {

}
