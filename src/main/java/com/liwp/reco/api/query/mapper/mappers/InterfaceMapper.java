package com.liwp.reco.api.query.mapper.mappers;

import com.liwp.reco.api.query.mapper.Mapper;
import com.liwp.reco.api.query.mapper.utils.MapperUtils;
import lombok.Getter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import com.liwp.reco.api.query.entity.entities.InterfaceEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwp on 2017/5/6.
 */
public class InterfaceMapper implements Mapper {
    private final GraphDatabaseService graphDb;
    @Getter
    private Map<InterfaceEntity, Integer> interfaceMap = new HashMap<>();

    public InterfaceMapper(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public void build() {
        buildClassMap();
    }

    private void buildClassMap() {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.getAllNodes().stream().forEach(node -> {
                if(MapperUtils.checkNodeLabel(node, "Interface")) {
                    InterfaceEntity interfaceEntity = MapperUtils.buildInterface(node);
                    if(interfaceMap.containsKey(interfaceEntity)) {
                        interfaceMap.put(interfaceEntity, interfaceMap.get(interfaceEntity) + 1);
                    } else {
                        interfaceMap.put(interfaceEntity, 1);
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
