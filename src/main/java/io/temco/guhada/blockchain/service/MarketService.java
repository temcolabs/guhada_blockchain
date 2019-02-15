package io.temco.guhada.blockchain.service;

import io.temco.guhada.blockchain.model.client.GoodsItem;
import io.temco.guhada.blockchain.model.client.Upload;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MarketService {


    ResponseEntity<?> upload(Upload upload);

    List<GoodsItem> getGoods();
}
