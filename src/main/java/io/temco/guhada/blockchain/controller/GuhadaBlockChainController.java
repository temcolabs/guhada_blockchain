package io.temco.guhada.blockchain.controller;

import io.swagger.annotations.ApiOperation;
import io.temco.guhada.blockchain.model.GuhadaTransact;
import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.response.GuhadaTransactResponse;
import io.temco.guhada.blockchain.service.GuhadaContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Shin Han
 * Since 2019-06-10
 */
@Slf4j
@RestController
@RequestMapping(value = "/guhada/blockchain")
public class GuhadaBlockChainController {


    @Autowired
    private GuhadaContractService guhadaContractService;

    @PostMapping("/upload-blockchain")
    @ApiOperation(value = "uploadBlockchain", notes = "상품의 정보를 저장하고 상품을 스캔할 때 사용할 QRCode를 생성하기위한 값을 제공하는 API")
    @ResponseBody
    public GuhadaTransact uploadBlockchain(@RequestBody GuhadaTransactRequest guhadaTransactRequest) throws Exception {
        return guhadaContractService.uploadToBlockchain(guhadaTransactRequest);
    }

    @GetMapping("/transact-Data/{productId}")
    @ApiOperation(value = "getTransactData", notes = "상품의 정보를 저장하고 상품을 스캔할 때 사용할 QRCode를 생성하기위한 값을 제공하는 API")
    @ResponseBody
    public List<GuhadaTransactResponse> getTransactData(@PathVariable(value = "productId") long productId){
        return guhadaContractService.getTransactData(productId);
    }
}
