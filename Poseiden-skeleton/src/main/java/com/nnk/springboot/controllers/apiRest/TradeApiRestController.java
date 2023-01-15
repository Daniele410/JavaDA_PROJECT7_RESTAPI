package com.nnk.springboot.controllers.apiRest;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exception.DataNotFoundException;
import com.nnk.springboot.service.ITradeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Trade Rest Controller
 */
@RestController
public class TradeApiRestController {

    /**
     * SLF4J Logger instance.
     */
    private static final Logger logger = LogManager.getLogger("TradeApiRestController");


   private ITradeService tradeService;

    public TradeApiRestController(ITradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * get method to show all trade
     * @return
     */
    @GetMapping("/trades/api")
    public ResponseEntity<List<Trade>> showRestTrade() {
        logger.info("@RequestMapping(\"/trades/api\")");
        return new ResponseEntity<>(tradeService.findAll(), HttpStatus.OK);
    }

    /**
     * get method to get trade by id
     * @param id
     * @return trade httpStatus.Ok
     * @throws DataNotFoundException
     */
    @GetMapping("/trade/api/{id}")
    public ResponseEntity<Optional<Trade>> showRestTradeById(@PathVariable int id) throws DataNotFoundException {
        logger.info("@RequestMapping(\"/trade/api/{id}\")");
        Optional<Trade> trade = tradeService.findById(id);

        return new ResponseEntity<>(tradeService.findById(id), HttpStatus.OK);
    }

    /**
     * post method to add new trade
     * @param trade
     * @return add bidList
     */
    @PostMapping("/trade/api")
    public Trade addRestTrade(@RequestBody Trade trade) {
        logger.info("@PostMapping(\"/trade/api\")");
        tradeService.save(trade);
        return trade;
    }


    /**
     * put method to upload trade
     * @param trade
     * @return update trade
     */
    @PutMapping("/trade/api")
    public Trade uploadRestTrade(@RequestBody Trade trade) {
        logger.info("@PutMapping(\"/trade/api/{}\")  Id " + trade + " as modified", trade.getTradeId());

        return tradeService.update(trade);
    }

    /**
     * delete method to delete trade by id
     * @param tradeId
     * @return
     * @throws DataNotFoundException
     */
    @DeleteMapping("/trade/api/{tradeId}")
    public String deleteRestTrade(@PathVariable int tradeId) throws DataNotFoundException {
        logger.info("@DeleteMapping(\"/trade/api/{tradeId}\")");

        tradeService.delete(tradeId);
        return "delete bid by id: " + tradeId + " success";
    }


}
