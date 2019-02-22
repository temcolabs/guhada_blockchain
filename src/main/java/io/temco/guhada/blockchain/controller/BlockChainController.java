package io.temco.guhada.blockchain.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.temco.guhada.blockchain.model.request.ProductRequest;
import io.temco.guhada.blockchain.model.request.TransactRequest;
import io.temco.guhada.blockchain.model.response.BlockChainInfo;
import io.temco.guhada.blockchain.service.SmartContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/blockchain")
@Api(basePath = "/api/blockchain", description = "상품과 물류정보를 블록체인에 올리고 확인하는 API들")
public class BlockChainController {


    @Autowired
    private SmartContractService smartContractService;

    @PostMapping("/generateQrCode")
    @ApiOperation(value = "generateQrCode", notes = "상품의 정보를 저장하고 상품을 스캔할 때 사용할 QRCode를 생성하기위한 값을 제공하는 API")
    @ResponseBody
    public long generateQrCode(@RequestHeader(value="apiToken")String apiToken,
                               @ApiParam(name = "ProductRequest", value = "등록할 사용자의 정보", required = true)
                               @RequestBody ProductRequest productRequest) throws Exception {
        return smartContractService.generateQrCode(apiToken,productRequest);
    }

    @PostMapping("/uploadToBlockChain")
    @ApiOperation(value = "uploadToBlockChain", notes = "등록된 상품의 물류정보를 블록체인에 upload 한 후에 그 Transaction Hash값을 제공해주는 API")
    @ResponseBody
    public String uploadToBlockChain(@RequestHeader(value="apiToken")String apiToken,
                                     @ApiParam(name = "transact", required = true)
                                     @RequestBody TransactRequest transactRequest) throws Exception {
        return smartContractService.uploadToBlockchain(apiToken, transactRequest);
    }

    @GetMapping("/getBlockChainInfo/{hashId}")
    @ApiOperation(value = "getBlockChainInfo", notes = "Transaction Hash 값을 이용하여 해당하는 물류정보와 상품정보를 제공해주는 API")
    @ResponseBody
    public BlockChainInfo getBlockChainInfo(@RequestHeader(value="apiToken")String apiToken,
                                            @ApiParam(name = "hashId", required = true, type = "String")
                                            @PathVariable(value="hashId") String hashId) throws Exception {
        return smartContractService.getBlockChainInfo(apiToken,hashId);
    }

}
