package io.temco.guhada.blockchain.controller;

import io.temco.guhada.blockchain.model.client.GoodsItem;
import io.temco.guhada.blockchain.model.client.Upload;
import io.temco.guhada.blockchain.service.MarketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarketController {

    private static final Logger log = LoggerFactory.getLogger(MarketController.class);


    @Autowired
    private MarketService marketService;

    @RequestMapping(value = "/listGoods", method = RequestMethod.GET)
    public List<GoodsItem> listGoods() {
        return marketService.getGoods();
    }

    @PostMapping(value = "/uploadGoods")
    public ResponseEntity<?> uploadGoods(@ModelAttribute Upload upload) {
        return marketService.upload(upload);
    }


}
