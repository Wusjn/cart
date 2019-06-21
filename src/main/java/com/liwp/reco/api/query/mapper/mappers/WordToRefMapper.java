package com.liwp.reco.api.query.mapper.mappers;

import com.liwp.reco.api.query.entity.Entity;
import com.liwp.reco.api.query.mapper.Mapper;
import lombok.Getter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import com.liwp.reco.api.query.mapper.utils.MapperUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwp on 2017/5/3.
 */
public class WordToRefMapper implements Mapper {

    private final GraphDatabaseService graphDb;
    private final int upperLengthLimit = 15;
    private final int lowerLengthLimit = 3;

    private Map<String, Integer> wordMap = new HashMap<>();
    @Getter
    private Map<String, Map<Entity, Integer>> wordToMethodMap = new HashMap<>();

    public WordToRefMapper(final GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public void build() {
        buildWordToMethodMap();
    }

    private void buildWordToMethodMap() {
        buildWordMap();
        wordMap.keySet().stream().forEach(word -> {
            wordToMethodMap.put(word, new HashMap<>());
        });
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.getAllNodes().stream().forEach(node -> {
                if(MapperUtils.checkNodeLabel(node, "StackOverflow") && node.getProperties("body").size() > 0) {
                    Collection<Entity> refEntities = MapperUtils.getRefs(node);
                    Collection<String> words = MapperUtils.getBodyWords(node);
                    words.forEach(word -> {
                        Map<Entity, Integer> wordToMethods = wordToMethodMap.get(word);
                        if(wordToMethods != null) {
                            refEntities.forEach(entity -> {
                                if (wordToMethods.containsKey(entity)) {
                                    wordToMethods.put(entity, wordToMethods.get(entity) + 1);
                                } else {
                                    wordToMethods.put(entity, 1);
                                }
                            });
                        }
                    });
                }
            });
            tx.success();
        }
    }

    private void buildWordMap() {
        try (Transaction tx = graphDb.beginTx()) {
            graphDb.getAllNodes().stream().forEach(node -> {
                if(MapperUtils.checkNodeLabel(node, "StackOverflow") && (node.getProperties("body").size() > 0)) {
                    Collection<String> words = MapperUtils.getBodyWords(node);
                    words.forEach(word -> {
                        if(word.length() < upperLengthLimit && word.length() > lowerLengthLimit) {
                            if (wordMap.containsKey(word)) {
                                wordMap.put(word, wordMap.get(word) + 1);
                            } else {
                                wordMap.put(word, 1);
                            }
                        }
                    });
                }
            });
            tx.success();
        }
    }



    @Override
    public void close() {
        wordMap.clear();
        graphDb.shutdown();
    }

}
