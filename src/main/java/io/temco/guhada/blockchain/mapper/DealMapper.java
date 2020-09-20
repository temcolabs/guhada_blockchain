package io.temco.guhada.blockchain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import io.temco.guhada.blockchain.model.request.TrackRecord;

@Repository
@Mapper
public interface DealMapper {
	
	@Select("select    \n" + 
			"    dl.id AS dealId,\n" + 
			"    pd.model_number AS serialId,\n" + 
			"    pd.name AS productName,\n" + 
			"    br.name_en AS brandName,\n" + 
			"    dl.sell_price AS price,\n" + 
			"    bu.company_name AS owner\n" + 
			"	from deal dl\n" + 
			"	left join track_record tr on dl.id = tr.deal_id\n" + 
			"    left join product pd on dl.product_id = pd.id\n" + 
			"    left join brand br on pd.brand_id = br.id\n" + 
			"    left join business_user bu on dl.seller_id = bu.id\n" + 
			"    where dl.id not in (select deal_id from track_record) and dl.status = 'SALE' and dl.total_stock > 0 and dl.is_displaying = 1 and now() between dl.display_start_at and dl.display_end_at\n" + 
			"    limit 10;")
	List<TrackRecord> getUploadDeal();

}
