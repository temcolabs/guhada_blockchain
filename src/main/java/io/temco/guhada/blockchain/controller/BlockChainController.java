package io.temco.guhada.blockchain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.temco.guhada.blockchain.model.request.CompanyRequest;
import io.temco.guhada.blockchain.model.request.GenerateQrCodeRequest;
import io.temco.guhada.blockchain.model.request.TrackRecord;
import io.temco.guhada.blockchain.model.request.TransactRequest;
import io.temco.guhada.blockchain.service.TrackRecordService;
//import io.temco.guhada.blockchain.model.response.BlockChainInfo;
//import io.temco.guhada.blockchain.model.response.CompanyResponse;
//import io.temco.guhada.blockchain.model.response.GenerateQrCodeResponse;
//import io.temco.guhada.blockchain.model.response.UploadToBlockchainResponse;
//import io.temco.guhada.blockchain.service.SmartContractService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/blockchain")
@Api(basePath = "/api/blockchain", description = "상품과 물류정보를 블록체인에 올리고 확인하는 API들")
public class BlockChainController {

	@Autowired
	private TrackRecordService trackRecordService;

	@PostMapping("/testUpload")	
	@ResponseBody
	public void testUpload() throws Exception {
		trackRecordService.testUpload();
	}
	
	
	/**
	 * get deal data from  blockchain.  
	 * 
	 * @param dealId : deal id
	 */
	@GetMapping("/getProductTransactions")	
	@ResponseBody
	public List<TrackRecord> getProductTransactions(Long dealId) throws Exception {
		return trackRecordService.getProductTransactions(dealId);
	}
	
	

//    @Autowired
//    private SmartContractService smartContractService;
//
//    @PostMapping("/generateQrCode")
//    @ApiOperation(value = "uploadBlockchain", notes = "상품의 정보를 저장하고 상품을 스캔할 때 사용할 QRCode를 생성하기위한 값을 제공하는 API")
//    @ResponseBody
//    public GenerateQrCodeResponse generateQrCode(@RequestHeader(value="apiToken")String apiToken,
//                                                 @ApiParam(name = "GenerateQrCodeRequest", value = "등록할 제품의 정보", required = true)
//                               @RequestBody GenerateQrCodeRequest generateQrCodeRequest) throws Exception {
//        return smartContractService.generateQrCode(apiToken, generateQrCodeRequest);
//    }
//
//    @PostMapping("/uploadToBlockChain")
//    @ApiOperation(value = "uploadToBlockChain", notes = "등록된 상품의 물류정보를 블록체인에 upload 한 후에 그 Transaction Hash값을 제공해주는 API")
//    @ResponseBody
//    public UploadToBlockchainResponse uploadToBlockChain(@RequestHeader(value="apiToken")String apiToken,
//                                                         @ApiParam(name = "transact", required = true)
//                                     @RequestBody TransactRequest transactRequest) throws Exception {
//        return smartContractService.uploadToBlockchain(apiToken, transactRequest);
//    }
//
//    @GetMapping("/getBlockChainInfo/{hashId}")
//    @ApiOperation(value = "getBlockChainInfo", notes = "Transaction Hash 값을 이용하여 해당하는 물류정보와 상품정보를 제공해주는 API")
//    @ResponseBody
//    public BlockChainInfo getBlockChainInfo(@RequestHeader(value="apiToken")String apiToken,
//                                            @ApiParam(name = "hashId", required = true, type = "String")
//                                            @PathVariable(value="hashId") String hashId) throws Exception {
//        return smartContractService.getBlockChainInfo(apiToken,hashId);
//    }
//
//    @PostMapping("/registerCompany")
//    @ApiOperation(value = "registerCompany", notes = "Temco 의 blockchian API를 사용하기 위한 회사를 등록하는 API")
//    @ResponseBody
//    public CompanyResponse registerCompany(@ApiParam(name = "companyRequest", required = true)CompanyRequest companyRequest) throws Exception {
//        return smartContractService.registerCompany(companyRequest);
//    }

}
