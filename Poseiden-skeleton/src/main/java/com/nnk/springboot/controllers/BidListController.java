package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exception.DataNotFoundException;
import com.nnk.springboot.service.IBidListService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;


/**
 * Bid List Controller
 */
@Controller
public class BidListController {

    /**
     * SLF4J Logger instance.
     */
    private static final Logger logger = LogManager.getLogger("BidListController");


    /**
     * IBidListService instance.
     */
    private IBidListService bidListService;


    /**
     * @param bidListService
     */
    public BidListController(IBidListService bidListService) {
        this.bidListService = bidListService;
    }

    /**
     * get method to get all Bids
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/bidList/list")
    public String home(Model model, Principal principal) {
        logger.info("@RequestMapping(\"/bidList/list\")");
        model.addAttribute("bidList", bidListService.findAll());
        return "bidList/list";
    }

    /**
     * get method to show bidList form
     * @param bid
     * @return
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        logger.info("@GetMapping(\"/bidList/add\")");
        return "bidList/add";
    }

    /**
     * post method to add new bid
     * @param bid
     * @param result
     * @param model
     * @return add bid list in database
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        logger.info("@PostMapping(\"/bidList/validate\")");
        /**form data validation*/
        if (result.hasErrors()) {
            return "/bidList/add";
        }
        /**save in to dataBase:*/
        bidListService.save(bid);
        //redirection do not use the current Model
        return "redirect:/bidList/list";
    }

    /**
     * get method to show bid update form
     * @param id
     * @param model
     * @return bidList update form
     * @throws DataNotFoundException
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws DataNotFoundException {
        logger.info("@GetMapping(\"/bidList/update/{id}\")");
        Optional<BidList> bidList = bidListService.findById(id);

        model.addAttribute("bidList", bidList.get());
        return "bidList/update";
    }

    /**
     * post method to update bid by id
     * @param id
     * @param bidList
     * @param result
     * @param model
     * @return
     * @throws DataNotFoundException
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,@Valid BidList bidList,
                            BindingResult result, Model model) throws DataNotFoundException {
        /**form data validation*/
        if (result.hasErrors()) {
           return  "redirect:/bidList/update/{id}";
        }
        logger.info("@PostMapping(\"/bidList/update/{id}\")");
        bidList.setBidListId(id);
        bidListService.save(bidList);
        model.addAttribute("bidList", bidListService.findAll());
        return "redirect:/bidList/list";
    }

    /**
     * delete method to delete bid by id
     * @param id
     * @param model
     * @return
     * @throws DataNotFoundException
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) throws DataNotFoundException {
        logger.info("@GetMapping(\"/bidList/delete/{id}\"");
        bidListService.delete(id);
        model.addAttribute("bids", bidListService.findAll());
        return "redirect:/bidList/list";
    }


}
