package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exception.DataNotFoundException;
import com.nnk.springboot.service.ICurvePointService;
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
 * Curve Controller
 */
@Controller
public class CurveController {

    /**
     * SLF4J Logger instance.
     */
    private static final Logger logger = LogManager.getLogger("CurveController");

    /**
     * ICurvePointService instance.
     */
    private ICurvePointService curvePointService;

    /**
     * @param curvePointService
     */
    public CurveController(ICurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }


    /**
     * @param model
     * @param principal
     * @return curvePoint/list
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model, Principal principal) {
        logger.info("@RequestMapping(\"/curvePoint/List\")");
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }

    /**
     * get method to show add curvePoint form
     * @param bid
     * @return
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        logger.info("@GetMapping(\"/curvePoint/add\")");
        return "curvePoint/add";
    }

    /**
     * post method to add curvePoint
     * @param curvePoint
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        logger.info("@PostMapping(\"/curvePoint/validate\")");
        /**save in to dataBase:*/
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * get mothod to show curvePoint update form
     * @param id
     * @param model
     * @return curvePoint update form
     * @throws DataNotFoundException
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws DataNotFoundException {
        logger.info("@GetMapping(\"/curvePoint/update/{id}\")");
        Optional<CurvePoint> curvePoint = curvePointService.findById(id);
        model.addAttribute("curvePoint", curvePoint.get());
        return "curvePoint/update";
    }

    /**
     * post method to update curvePoint by id
     * @param id
     * @param curvePoint
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                            BindingResult result, Model model) throws DataNotFoundException {
        logger.info("@PostMapping(\"/curvePoint/update/{id}\")");
        curvePoint.setId(id);
        curvePointService.save(curvePoint);
        model.addAttribute("curvePoint", curvePointService.findAll());
        return "redirect:/curvePoint/list";
    }

    /**
     * get method to delete curve point by id
     * @param id
     * @param model
     * @return
     * @throws DataNotFoundException
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) throws DataNotFoundException {
        logger.info("@GetMapping(\"/curvePoint/delete/{id}\"");
       curvePointService.delete(id);
       model.addAttribute("curvePoints", curvePointService.findAll());
        return "redirect:/curvePoint/list";
    }
}
