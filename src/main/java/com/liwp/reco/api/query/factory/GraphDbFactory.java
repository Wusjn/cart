package com.liwp.reco.api.query.factory;

import com.liwp.reco.api.query.mapper.utils.MapperUtils;
import lombok.Data;
import lombok.Getter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by liwp on 2017/5/2.
 */
@Component
public class GraphDbFactory {

    @Value("${db.path}")
    private String dbPath;

    public GraphDatabaseService builder() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(new File(dbPath));
    }

//    public static int refNum = 0;
//    public static int methodNum = 0;
//    public static void main(String args[]) {
//        GraphDatabaseService db = builder();
//        try(Transaction tx = db.beginTx()) {
//            System.out.println(db.getAllNodes().stream().count());
//            db.getAllRelationshipTypes().stream().forEach(relationshipType -> {
//                System.out.println(relationshipType);
//            });
////            db.getAllNodes().stream().forEach(node -> {
////                if(MapperUtils.checkNodeLabel(node, "StackOverflowQuestion")) {
////                    node.getAllProperties().forEach((name, property) -> {
////                        System.out.println(name);
////                    });
////                }
////            });
//            db.getAllRelationships().stream().forEach(relationship -> {
//                if(relationship.getType().equals(RelationshipType.withName("codeMention"))) {
//                    refNum++;
////                    relationship.getStartNode().getLabels().forEach(label -> {
////                        System.out.println(label);
////                    });
//
//                    if(MapperUtils.checkNodeLabel(relationship.getEndNode(), "Method")) {
//                        //System.out.println(relationship.getEndNode().getProperty("params"));
//                        methodNum++;
//                    }
//
//                }
//            });
//            db.getAllLabels().stream().forEach(label -> {
//                System.out.println(label);
//            });
//
//            tx.success();
//        }
//        System.out.println(refNum);
//        System.out.println(methodNum);
//    }

}
