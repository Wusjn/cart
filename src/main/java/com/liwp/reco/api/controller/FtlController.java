package com.liwp.reco.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwp on 2017/8/5.
 */
@Controller
public class FtlController {
    @RequestMapping("/api/qa_show")
    public String show(ModelMap model,
                       @RequestParam("sel") String sel,
                       @RequestParam("value") String value,
                       @RequestParam("apiName") String apiName,
                       @RequestParam("apiPrefix") String apiPrefix,
                       @RequestParam("apiSuffix") String apiSuffix) {
        System.out.println("sel : " + sel);
        System.out.println("value : " + value);
        System.out.println("apiName : " + apiName);
        System.out.println("apiPrefix : " + apiPrefix);
        System.out.println("apiSuffix : " + apiSuffix);

        model.addAttribute("value", value);
        model.addAttribute("sel", sel);

        Map<String,Boolean> relExist = new HashMap<>();
        StringBuilder linksSb = new StringBuilder();
        StringBuilder nodesSb = new StringBuilder();
        if (sel.equals("graph")){
            File file = new File("/Users/apple/Downloads/liwp/Graph-Lucene");
            GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
            System.out.println("Server is up and Running");
            try(Transaction tx = graphDB.beginTx()){
                //通过Cypher查询获得结果
                StringBuilder sb = new StringBuilder();
                sb.append("MATCH (targetApi:Method)<-[:haveMethod]-(apiClass:Class)-[:haveMethod]->(method:Method) ");
                sb.append("WHERE (targetApi.name = \"" + apiName.substring(0,apiName.indexOf('(')) + "\") AND " +
                        "(apiClass.name = \"" + apiPrefix.substring(apiPrefix.lastIndexOf('.')+1) +"\")");
                sb.append("RETURN apiClass,method");
                Result result = graphDB.execute(sb.toString());
                //遍历结果
                int recordNum = 0;
                while(result.hasNext()){
                    /*
                    if (recordNum > 21){
                        break;
                    }
                    */
                    //get("movie")和查询语句的return movie相匹配
                    Map<String,Object> record = result.next();
                    Node method = (Node) record.get("method");
                    Node apiClass = (Node) record.get("apiClass");
                    String ApiClassName = apiClass.getProperty("name").toString();
                    String ApiMethodName = method.getProperty("name").toString();
                    Map<String,Object> methodProperties = method.getAllProperties();
                    Map<String,Object> classProperties = apiClass.getAllProperties();
                    methodProperties.remove("name");
                    methodProperties.remove("content");
                    methodProperties.remove("comment");
                    methodProperties.remove("_text");
                    classProperties.remove("name");
                    classProperties.remove("content");
                    classProperties.remove("comment");
                    classProperties.remove("_text");

                    if(recordNum == 0){
                        nodesSb.append("{name:\"" + ApiClassName + "\", ");
                        nodesSb.append("type:\"Class\", ");
                        for(Map.Entry<String, Object> entry : classProperties.entrySet()){
                            String mapKey = entry.getKey();
                            String mapValue = entry.getValue().toString();
                            nodesSb.append(mapKey + ":\"" + mapValue + "\", ");
                        }
                        nodesSb.append("},\n");
                        recordNum = 1;
                    }

                    if (!ApiMethodName.equals("toString") && !ApiClassName.equals(ApiMethodName) && !relExist.containsKey(ApiClassName + " " + ApiMethodName)){
                        relExist.put(ApiClassName + " " + ApiMethodName, true);
                        nodesSb.append("{name:\"" + ApiMethodName + "\", ");
                        nodesSb.append("type:\"Method\", ");
                        for(Map.Entry<String, Object> entry : methodProperties.entrySet()){
                            String mapKey = entry.getKey();
                            String mapValue = entry.getValue().toString();
                            nodesSb.append(mapKey + ":\"" + mapValue + "\", ");
                        }
                        nodesSb.append("},\n");
                        linksSb.append("{source: 0, target: " + recordNum + ", relation: \"haveMethod\", value : 1},\n");
                        System.out.println(method.getId() + " : " + ApiMethodName);
                        recordNum += 1;
                    }
                }
                if (linksSb.length() != 0){
                    linksSb.deleteCharAt(linksSb.length()-1);
                }
                if (nodesSb.length() != 0){
                    nodesSb.deleteCharAt(nodesSb.length()-1);
                }

                tx.success();
                System.out.println("Done successfully");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                graphDB.shutdown();    //关闭数据库
            }

            model.addAttribute("links", linksSb.toString());
            model.addAttribute("nodes", nodesSb.toString());
            //model.addAttribute("link","{source: \"Hardlarge\", target: \"Amazon\", type: \"licensing\"},");
        }
        return "qa_show";
    }
}
