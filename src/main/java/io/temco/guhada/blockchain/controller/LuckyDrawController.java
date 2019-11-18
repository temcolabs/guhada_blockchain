package io.temco.guhada.blockchain.controller;

import io.temco.guhada.blockchain.model.LuckyDrawModel;
import io.temco.guhada.blockchain.model.request.LuckyDrawRequest;
import io.temco.guhada.blockchain.service.LuckyDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void entry(@RequestBody List<LuckyDrawRequest> luckyDrawRequestList) throws Exception {
        luckyDrawService.entry(luckyDrawRequestList);
    }

    @PostMapping("/draw")
    @ResponseBody
    public void draw(@RequestParam long dealId) throws Exception {
        luckyDrawService.draw(dealId);
    }

    @GetMapping("/lucky-draw-entrys/")
    @ResponseBody
    public LuckyDrawModel luckyDrawEntrys(@RequestParam(value="dealId") Long dealId, @RequestParam(value="userId") Long userId) throws Exception {
        return luckyDrawService.getEntryUser(dealId,userId);
    }

    @PostMapping("/lucky-draw-winner/{dealId}")
    @ResponseBody
    public LuckyDrawModel luckyDrawWinner(@PathVariable(value="dealId") Long dealId) throws Exception {
        return luckyDrawService.getLuckyDrawWinner(dealId);
    }
}
