package com.liwp.reco.api.query.reco;

import com.liwp.reco.api.query.entity.Entity;

import java.util.*;

/**
 * Created by liwp on 2017/5/7.
 */
public class Calculator {
    public static Map<Entity, Integer> calScoresFromQuerys(
            Collection<String> querys,
            Map<String, Map<Entity, Integer>> wordToEntityMap) {
        Map<Entity, Integer> entityScores = new HashMap<>();
        for(String query : querys) {
            if(!wordToEntityMap.containsKey(query)) {
                continue;
            }
            wordToEntityMap.get(query).forEach((entity, num) -> {
                if(entityScores.containsKey(entity)) {
                    entityScores.put(entity, entityScores.get(entity) + num);
                } else {
                    entityScores.put(entity, num);
                }
            });
        }

        return entityScores;
    }

    public static Set<Entity> collectEntities(Map<Entity, Integer> entityMap) {
        Set<Entity> entitySet = new TreeSet<>();
        entityMap.forEach((entity, integer) -> {
            entity.setTimes(integer);
            entitySet.add(entity);
        });
        return entitySet;
    }

}
