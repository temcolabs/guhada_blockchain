package io.temco.guhada.blockchain.service;

import java.util.List;

import io.temco.guhada.blockchain.model.request.TrackRecord;

public interface TrackRecordService {
	
	/**
	 * upload new deal data to blockchain.  
	 * 
	 * @param trackRecordRequest
	 */
	void uploadProductInfo(TrackRecord trackRecordRequest);
	
	/**
	 * get deal data from  blockchain.  
	 * 
	 * @param dealId : deal id
	 */
	List<TrackRecord> getProductTransactions(Long dealId);
	
	void testUpload();
}
