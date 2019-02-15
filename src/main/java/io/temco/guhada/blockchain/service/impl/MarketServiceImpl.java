package io.temco.guhada.blockchain.service.impl;

import io.temco.guhada.blockchain.dao.GoodsDao;
import io.temco.guhada.blockchain.dao.TransactionDao;
import io.temco.guhada.blockchain.model.Goods;
import io.temco.guhada.blockchain.model.Seller;
import io.temco.guhada.blockchain.model.Transaction;
import io.temco.guhada.blockchain.model.client.GoodsItem;
import io.temco.guhada.blockchain.model.client.Upload;
import io.temco.guhada.blockchain.service.DictionaryCache;
import io.temco.guhada.blockchain.service.MarketService;
import io.temco.guhada.blockchain.service.SmartContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("marketService")
public class MarketServiceImpl implements MarketService {

    private static final Logger log = LoggerFactory.getLogger(MarketServiceImpl.class);
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private DictionaryCache dictionaryCache;
    @Autowired
    private SmartContractService smartContractService;

    public MarketServiceImpl(){
    }


    @Override
    public ResponseEntity<?> upload(Upload upload) {
        try {
            Optional<Seller> seller = dictionaryCache.getSeller(upload.getEmail());
            if(!seller.isPresent()) return null; // something wrong
            Goods goods = goodsDao.insert(Goods.from(upload, dictionaryCache));
            String hash = null;
            try {
                hash = smartContractService.insert(goods);
            } catch (Exception e) {
                log.error("Failed to insert data to smart contract", e);
            }
            if(hash!= null)  {
                transactionDao.save(new Transaction(seller.get(), hash));
            }
        } catch (IOException e) {
            log.error("Failed to insert data", e);
        }
        return null;
    }

    @Override
    public List<GoodsItem> getGoods() {
        return goodsDao.findAll(dictionaryCache);
    }
}
