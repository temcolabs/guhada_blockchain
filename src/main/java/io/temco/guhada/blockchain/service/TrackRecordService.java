package io.temco.guhada.blockchain.service;

import java.util.List;

import io.temco.guhada.blockchain.model.request.TrackRecord;

public interface TrackRecordService {
	
	void uploadProductInfo(TrackRecord trackRecordRequest);
	
	List<TrackRecord> getProductTransactions(int dealId);
}
