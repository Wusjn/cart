package com.liwp.reco.api.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Created by liwp on 2017/8/5.
 */
@RestController
public class ShowController {
    @CrossOrigin
    @PostMapping("qa_show")
    public String show(@RequestParam("sel") String sel,
                       @RequestParam("value") String value) {
        System.out.println("sel : " + sel);
        System.out.println("value : " + value);
        return value;
    }
}
