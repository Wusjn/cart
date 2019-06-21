package com.liwp.reco.api.query.reco;


import com.liwp.reco.api.query.entity.Entity;
import com.liwp.reco.api.query.entity.entities.ClassEntity;
import com.liwp.reco.api.query.entity.entities.InterfaceEntity;
import com.liwp.reco.api.query.entity.entities.MethodEntity;
import lombok.Getter;
import com.liwp.reco.api.query.factory.MapFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by liwp on 2017/5/3.
 */
@Component
@Getter
public class Recommendation {
    private final Map<String, Map<Entity, Integer>> wordToEntityMap;
    private final Map<MethodEntity, Integer> methodMap;
    private final Map<ClassEntity, Integer> classMap;
    private final Map<InterfaceEntity, Integer> interfaceMap;

    private Set<Entity> entityScores;

    @Autowired
    public Recommendation(MapFactory mapFactory) {
        wordToEntityMap = mapFactory.buildAndGetWordToRefMap();
        methodMap = mapFactory.buildAndGetMethodMap();
        classMap = mapFactory.buildAndGetClassMap();
        interfaceMap = mapFactory.buildAndGetInterfaceMap();
    }

    public Collection<Entity> recommend(String queryLine) {
        Collection<String> queryWords = QueryDealer.splitWords(queryLine);
        entityScores = Calculator.collectEntities(Calculator.calScoresFromQuerys(queryWords, wordToEntityMap));
        entityScores.forEach(entity -> {
            //System.out.println(entity.prefix() + " " + entity.name() + " "+ entity.suffix());
            //System.out.println(entity.urlPath());
        });
        return entityScores;
    }

//    public static void main(String args[]) {
//        Recommendation reco = new Recommendation();
//        String s = "index  ";
//        reco.recommend(s);
//        //System.out.println(reco.getMethodMap().size());
//        //System.out.println(reco.getClassMap().size());
//        //System.out.println(reco.getInterfaceMap().size());
//    }
}
