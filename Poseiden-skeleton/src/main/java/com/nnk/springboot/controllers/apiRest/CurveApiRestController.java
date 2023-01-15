package com.nnk.springboot.controllers.apiRest;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exception.DataNotFoundException;
import com.nnk.springboot.service.ICurvePointService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 *  RestController Curve
 */
@RestController
public class CurveApiRestController {


    /**
     * SLF4J Logger instance.
     */
    private static final Logger logger = LogManager.getLogger("CurveApiRestController");


    /**
     * Instance of ICurvePointService
     */
    private ICurvePointService curvePointService;

    /**
     * @param curvePointService
     */
    public CurveApiRestController(ICurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }


    /**
     * get method to swhow curvePoint
     * @return
     */
    @GetMapping("/curvePoint/api")
    public ResponseEntity<List<CurvePoint>> showRestCurvePoint() {
        logger.info("@RequestMapping(\"/curvePoint/api\")");
        return new ResponseEntity<>(curvePointService.findAll(), HttpStatus.OK);
    }

    /**
     * get method to show curvePoint by id
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    @GetMapping("/curvePoint/api/{id}")
    public ResponseEntity<Optional<CurvePoint>> showRestCurvePointById(@PathVariable int id) throws DataNotFoundException {
        logger.info("@RequestMapping(\"/curvePoint/api{id}\")");
        Optional<CurvePoint> curvePoint = curvePointService.findById(id);

        return new ResponseEntity<>(curvePointService.findById(id), HttpStatus.OK);
    }

    /**
     * post method to add curvePoint
     * @param curvePoint
     * @return
     */
    @PostMapping("/curvePoint/api")
    public CurvePoint addRestCurvePoint(@RequestBody CurvePoint curvePoint) {
        logger.info("@PostMapping(\"/curvePoint/api\")");
        curvePointService.save(curvePoint);

        return  curvePoint ;
    }

    /**
     * put method to upload curvePoint
     * @param curvePoint
     * @return
     */
    @PutMapping("/curvePoint/api")
    public CurvePoint uploadRestCurvePoint(@RequestBody CurvePoint curvePoint) {
        logger.info("@PutMapping(\"/curvePoint/api/{}\")  Id " + curvePoint.getId()+ " as modified", curvePoint.getId());
        curvePointService.update(curvePoint);
        return curvePoint;
    }

    /**
     * delete method to delete curvePoint
     * @param curvePointId
     * @return
     * @throws DataNotFoundException
     */
    @DeleteMapping("/curvePoint/api/{curvePointId}")
    public String deleteRestCurvePoint(@PathVariable int curvePointId) throws DataNotFoundException {
        logger.info("@DeleteMapping(\"/bidList/api/{bidListId}\")");

        curvePointService.delete(curvePointId);
        return "delete bid id: " + curvePointId + " success";
    }


}
