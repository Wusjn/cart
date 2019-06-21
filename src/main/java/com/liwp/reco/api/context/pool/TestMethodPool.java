package com.liwp.reco.api.context.pool;

import com.liwp.reco.api.query.entity.entities.MethodEntity;

import java.util.*;


/**
 * Created by liwp on 2017/6/18.
 */
public class TestMethodPool{
    public static List<List<MethodEntity>> methodBlocks = new ArrayList<>();
    public Map<MethodEntity, Integer> methods = new HashMap<>();

    public void build() {
        methodBlocks.forEach(block -> {
            block.forEach(method -> {
                if(methods.containsKey(method)) {
                    methods.put(method, methods.get(method) + 1);
                } else {
                    methods.put(method, 1);
                }
            });
        });
    }

    public void predict(TrainMethodPool trainMethodPool) {
        Map<MethodEntity, Integer> predictMap = new HashMap<>();
        trainMethodPool.methods.forEach((m, num) -> predictMap.put(m, 1));
        this.methods.forEach((contextMethod, num) -> {
            trainMethodPool.methodDistributions.forEach((info, time) -> {
                if(info.key.equals(contextMethod)) {
                    predictMap.put(info.value, trainMethodPool.methodDistributions.get(info) + predictMap.get(info.key));
                }
            });
        });

        Map<MethodEntity, Integer> outMap = new TreeMap<>();
        predictMap.forEach((e, times) -> {
            e.setTimes(times);
            outMap.put(e, times);
        });

        outMap.forEach((e, times) -> {
            if(times > 1) System.out.println(e.displayName() + " " + times);
        });
    }
}
