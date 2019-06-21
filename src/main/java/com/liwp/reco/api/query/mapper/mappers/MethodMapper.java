package com.liwp.reco.api.query.mapper.mappers;

import com.liwp.reco.api.query.entity.entities.MethodEntity;
import com.liwp.reco.api.query.mapper.Mapper;
import com.liwp.reco.api.query.mapper.utils.MapperUtils;
import lombok.Getter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwp on 2017/5/6.
 */
public class MethodMapper implements Mapper {
    private final GraphDatabaseService graphDb;
    @Getter
    private Map<MethodEntity, Integer> methodMap = new HashMap<>();


    public MethodMapper(final GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public void build() {
        buildMethodMap();
    }

    private void buildMethodMap() {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.getAllNodes().stream().forEach(node -> {
                if(MapperUtils.checkNodeLabel(node, "Method")) {
                    if(methodMap.containsKey(node)) {
                        methodMap.put(MapperUtils.buildMethod(node), methodMap.get(node) + 1);
                    } else {
                        methodMap.put(MapperUtils.buildMethod(node), 1);
                    }
                }
            });
            tx.success();
        }
    }

    @Override
    public void close() {
        graphDb.shutdown();
    }
}
