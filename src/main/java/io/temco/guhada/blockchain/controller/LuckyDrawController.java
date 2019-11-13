package io.temco.guhada.blockchain.controller;

import io.temco.guhada.blockchain.model.request.GuhadaTransactRequest;
import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.service.LuckyDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Shin Han
 * Since 2019-11-13
 */
@Slf4j
@RestController
@RequestMapping(value = "/guhada/lucky-draw")
public class LuckyDrawController {

    @Autowired
    private LuckyDrawService luckyDrawService;

    @PostMapping("/entry")
    @ResponseBody
    public void entry(@RequestBody LuckyDrawModel luckyDrawRequest) throws Exception {
        luckyDrawService.entry(luckyDrawRequest);
    }

    @PostMapping("/draw")
    @ResponseBody
    public void draw(@RequestParam long dealId) throws Exception {
        luckyDrawService.draw(dealId);
    }

    @GetMapping("/lucky-draw-entrys/{dealId}")
    @ResponseBody
    public LuckyDrawModel luckyDrawEntrys(@PathVariable(value="dealId") Long dealId){
        return luckyDrawService.getLuckyDrawEntrys(dealId);
    }

    @PostMapping("/lucky-draw-winner/{dealId}")
    @ResponseBody
    public LuckyDrawModel luckyDrawWinner(@PathVariable(value="dealId") Long dealId) throws Exception {
        return luckyDrawService.getLuckyDrawWinner(dealId);
    }
}
