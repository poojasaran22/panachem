package com.panache.controller;

import com.panache.pojo.Model;
import com.panache.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/model")
public class ModelController {

    @Autowired
    ModelService modelService;

    @RequestMapping("/list")
    public List<Map<String,Object>> list()throws Exception{
        return modelService.list();
    }

    @RequestMapping("/profile/{id}")
    public Map<String, Object> profile(@PathVariable("id") Integer id)throws Exception{
        return modelService.profile(id);
    }
    
    @RequestMapping("/dropdown/{id}")
    public List<Map<String,Object>> getDropDownList(@PathVariable("id") String id)throws Exception{
        return modelService.getDropDownList(id);
    }

    @RequestMapping(value = "/search" , method = RequestMethod.POST)
    public List<Map<String, Object>> search(@RequestBody Model model)throws Exception{
        return modelService.searchModel(model);
    }

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public List<Map<String, Object>> login(@RequestBody Map<String,Object> map)throws Exception{
        return modelService.login(map);
    }

    @RequestMapping(value = "/signup" , method = RequestMethod.POST)
    public int signup(@RequestBody Map<String,Object> map)throws Exception{
        return modelService.signup(map);
    }



}
