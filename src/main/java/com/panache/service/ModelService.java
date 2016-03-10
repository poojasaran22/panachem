package com.panache.service;


import com.panache.common.AppUtil;
import com.panache.dao.DaoService;
import com.panache.pojo.DropDownList;
import com.panache.pojo.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service
public class ModelService {

    @Autowired
    DaoService daoService;

    public List<Map<String,Object>> searchModel(Model model) throws Exception{

        String query = "select id, name, bust, country, experience, extra_tag,eyes, field, hair, height, shoes, type, waist, looksLike,modelid from pmodel where ";
        Map<String,Object> paramMap =  new LinkedHashMap<>();

        query += " type = '"+model.getType()+"'";

        if(model.getCountry()!=null && !"".equals(model.getCountry())) {
            query += " and  country = '"+model.getCountry()+"'";
        }

        if(model.getEyes()!=null && !"".equals(model.getEyes())) {
            query += " and  eyes = '"+model.getEyes() +"'";
        }
        if(model.getHair()!=null && !"".equals(model.getHair())) {
            query += " and  hair = '"+model.getHair()+ "'";
        }
        if(model.getExperience()!=null && !"".equals(model.getExperience())) {
            query += " and  experience = '"+model.getExperience()+ "'";
        }
        
        if(model.getLooksLike()!=null && !"".equals(model.getLooksLike())) {
            query += " and  looksLike = '"+model.getLooksLike()+ "'";
        }

        if(model.getBetweenHeight()!=null && !"".equals(model.getBetweenHeight())) {
            query += " and  height  between "+model.getBetweenHeight();
        }

        return daoService.findByQuery(query, null,
                "id", "name", "bust", "country", "experience", "extra_tag",
                "eyes", "field", "hair", "height", "shoes", "type", "waist","looksLike","modelid");
    }


    public List<Map<String,Object>> list() throws Exception{
        return daoService.findBy("pmodel", null, null, "id", "name", "bust", "country", "experience", "extra_tag",
                "eyes", "field", "hair", "height", "shoes", "type", "waist", "lookslike", "modelid");
    }

    public Map<String, Object> profile(Integer id)throws Exception {
        Map<String,Object> paramMap =  new LinkedHashMap<>();
        paramMap.put("id", id);

        Map<String,Object> profile = daoService.findOneBy("pmodel", paramMap, null, "id", "name", "bust", "country", "experience", "extra_tag",
                "eyes", "field", "hair", "height", "shoes", "type", "waist","lookslike","modelid");
        paramMap =  new LinkedHashMap<>();
        paramMap.put("modelid", id);
        List<Map<String,Object>> eduqc = daoService.findBy("pmodel_edu_q", paramMap, null, "id", "line1", "line2", "description", "modelid");
        profile.put("edu_qc", eduqc);
        return profile;
    }

    public List<Map<String,Object>> getDropDownList(String id) throws Exception{
    	return daoService.findBy("pmodel", null, null, id);
    }


    public List<Map<String,Object>> login(Map<String, Object> map) throws Exception {
        Map<String, Object> whereClauseMap = new LinkedHashMap<>();
        checkUser(map);
        whereClauseMap.put("username",AppUtil.lookup(map, "username", String.class));
        whereClauseMap.put("password",AppUtil.lookup(map, "password", String.class));

        List<Map<String, Object>> umap = daoService.findBy("public.user" ,whereClauseMap,null, "id","username","type");
        if (umap == null || umap.isEmpty()) {
            throw new RuntimeException(map.get("username") + " authentication failed");
        }
        Map<String, Object> m = umap.get(0);
        if(AppUtil.lookup(m,"type",String.class).equals("TALENT")){
            whereClauseMap = new LinkedHashMap<>();
            whereClauseMap.put("modelid", AppUtil.lookup(m, "id", Integer.class));
            Map<String, Object> m2 = daoService.findOneBy("pmodel", whereClauseMap, null, "id");
            m.put("id", AppUtil.lookup(m2,"id",Integer.class));
        }
        return umap;
    }

    public List<Map<String, Object>> checkUser(Map<String, Object> map) throws Exception {
        Map<String, Object> whereClauseMap = new LinkedHashMap<>();
        whereClauseMap.put("username", map.get("username"));
        List<Map<String, Object>> umap = daoService.findBy("public.user", whereClauseMap, null, "id", "username");
        if (umap == null || umap.isEmpty()) {
            throw new RuntimeException(map.get("username") + " does not exist");
        }
        return umap;
    }




    public int signup(Map<String, Object> map) throws Exception {
        String signupArray[] = new String []{"username","password","talentType"};
        for(String keyName : signupArray){
            AppUtil.lookup(map,keyName, String.class);
        }

        if(AppUtil.lookup(map, "talentType", String.class).equals("TALENT")) {
            String talentArray[] = new String[]{"name", "country", "name", "experience", "extra_tag", "eyes",
                    "field", "hair", "lookslike"};
            for (String keyName : talentArray) {
                AppUtil.lookup(map, keyName, String.class);
            }

            for(String keyName : new String[]{"bust", "shoes", "waist"}){
                AppUtil.lookup(map, keyName, Integer.class);
            }
            AppUtil.lookup(map, "height", Double.class);

            ArrayList<Map<String,Object>> edu_q_map_list = AppUtil.lookup(map, "edu_q_map_list", ArrayList.class);
            for(Map<String,Object> edu_q_map: edu_q_map_list) {
                AppUtil.lookup(edu_q_map, "line1", String.class);
                AppUtil.lookup(edu_q_map, "line2", String.class);
                AppUtil.lookup(edu_q_map, "description", String.class);
            }
        }


        Map<String, Object> whereClauseMap = new LinkedHashMap<>();

        whereClauseMap.put("username",AppUtil.lookup(map, "username", String.class));
        List<Map<String,Object>> umap =daoService.findBy("public.user", whereClauseMap, null, "id", "username");
        if(!umap.isEmpty()){
            throw new RuntimeException(map.get("username")+ " already exist");
        }

        Map<String, Object> insertMap = new LinkedHashMap<>();
        insertMap.put("username", AppUtil.lookup(map, "username", String.class));
        insertMap.put("password", AppUtil.lookup(map, "password", String.class));
        insertMap.put("type", AppUtil.lookup(map, "talentType", String.class));
        int modelid = daoService.executePrepareAndGetId("insert into public.user (username,password,type) values (?, ?, ?)", insertMap);
        insertMap.clear();

        if(AppUtil.lookup(map, "talentType", String.class).equals("TALENT")) {

            insertMap.put("modelid", modelid);
            insertMap.put("name", AppUtil.lookup(map, "name", String.class));
            insertMap.put("bust", AppUtil.lookup(map, "bust", Integer.class));
            insertMap.put("country", AppUtil.lookup(map, "country", String.class));
            insertMap.put("experience", AppUtil.lookup(map, "experience", String.class));
            insertMap.put("extra_tag", AppUtil.lookup(map, "extra_tag", String.class));
            insertMap.put("eyes", AppUtil.lookup(map, "eyes", String.class));
            insertMap.put("field", AppUtil.lookup(map, "field", String.class));
            insertMap.put("hair", AppUtil.lookup(map, "hair", String.class));
            insertMap.put("height", AppUtil.lookup(map, "height", Double.class));
            insertMap.put("shoes", AppUtil.lookup(map, "shoes", Integer.class));
            insertMap.put("type", AppUtil.lookup(map, "type", String.class));
            insertMap.put("waist", AppUtil.lookup(map, "waist", Integer.class));
            insertMap.put("lookslike", AppUtil.lookup(map, "lookslike", String.class));

            int mid = daoService.executePrepareAndGetId("insert into pmodel (modelid,name,bust,country,experience,extra_tag,eyes,field," +
                    "hair,height,shoes,type,waist,lookslike) values (?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?,?)", insertMap);

            insertMap.clear();

            ArrayList<Map<String,Object>> edu_q_map_list = AppUtil.lookup(map, "edu_q_map_list", ArrayList.class);

            for(Map<String,Object> edu_q_map: edu_q_map_list) {
                insertMap.put("line1", AppUtil.lookup(edu_q_map, "line1", String.class));
                insertMap.put("line2", AppUtil.lookup(edu_q_map, "line2", String.class));
                insertMap.put("description", AppUtil.lookup(edu_q_map, "description", String.class));
                insertMap.put("modelid", mid);

                daoService.save("pmodel_edu_q", insertMap);
            }
            return mid;
        }
        return modelid;
    }
}
