package com.liwp.reco.api.query.factory;

import com.liwp.reco.api.query.entity.Entity;
import com.liwp.reco.api.query.entity.entities.ClassEntity;
import com.liwp.reco.api.query.entity.entities.InterfaceEntity;
import com.liwp.reco.api.query.entity.entities.MethodEntity;
import com.liwp.reco.api.query.mapper.mappers.ClassMapper;
import com.liwp.reco.api.query.mapper.mappers.InterfaceMapper;
import com.liwp.reco.api.query.mapper.mappers.MethodMapper;
import com.liwp.reco.api.query.mapper.mappers.WordToRefMapper;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MapFactory {

    @Autowired
    private GraphDbFactory graphDbFactory;

    public Map<String, Map<Entity, Integer>> buildAndGetWordToRefMap() {
        GraphDatabaseService graphDb = graphDbFactory.builder();
        WordToRefMapper wordToRefMapper = new WordToRefMapper(graphDb);
        wordToRefMapper.build();
        Map<String, Map<Entity, Integer>> wordToMethodMap = wordToRefMapper.getWordToMethodMap();
        wordToRefMapper.close();
        return wordToMethodMap;
    }

    public Map<MethodEntity, Integer> buildAndGetMethodMap() {
        GraphDatabaseService graphDb = graphDbFactory.builder();
        MethodMapper methodMapper = new MethodMapper(graphDb);
        methodMapper.build();
        Map<MethodEntity, Integer> methodMap = methodMapper.getMethodMap();
        methodMapper.close();
        return methodMap;
    }

    public Map<InterfaceEntity, Integer> buildAndGetInterfaceMap() {
        GraphDatabaseService graphDb = graphDbFactory.builder();
        InterfaceMapper interfaceMapper = new InterfaceMapper(graphDb);
        interfaceMapper.build();
        Map<InterfaceEntity, Integer> interfaceMap = interfaceMapper.getInterfaceMap();
        interfaceMapper.close();
        return interfaceMap;
    }

    public Map<ClassEntity, Integer> buildAndGetClassMap() {
        GraphDatabaseService graphDb = graphDbFactory.builder();
        ClassMapper classMapper = new ClassMapper(graphDb);
        classMapper.build();
        Map<ClassEntity, Integer> classMap = classMapper.getClassMap();
        classMapper.close();
        return classMap;
    }
}