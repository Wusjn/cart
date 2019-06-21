package com.liwp.reco.api.query.mapper.utils;

import com.liwp.reco.api.query.entity.Entity;
import com.liwp.reco.api.query.entity.entities.ClassEntity;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import com.liwp.reco.api.query.entity.entities.InterfaceEntity;
import com.liwp.reco.api.query.entity.entities.MethodEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by liwp on 2017/5/3.
 */
public class MapperUtils {

    public static Collection<String> getBodyWords(Node node) {
        String body = node.getProperty("body").toString().toLowerCase();
        String[] words = shave(body);
        return new ArrayList(Arrays.asList(words));
    }

    private static String[] shave(String body) {
        body = body.trim()
                .replaceAll("\n", " ")
                .replaceAll("\\(", " ")
                .replaceAll("\\)", " ")
                .replaceAll(",", " ")
                .replaceAll(":", " ")
                .replaceAll("\\.", " ")
                .replaceAll("\\?", " ")
                .replaceAll("<code>[^<>]*</code>", "")
                .replaceAll("<[^<>]*>"," ");
        String[] words = body.split(" ");
        return words;
    }

    public static String getMethodName(Node node) {
        StringBuilder sbuilder = new StringBuilder();
        String methodName = sbuilder
                .append(node.getProperty("belongTo"))
                .append(".")
                .append(node.getProperty("name"))
                .toString();
        return methodName;
    }

    public static Collection<Entity> getRefs(Node node) {
        Collection<Entity> refEntities = new ArrayList<>();
        refEntities.addAll(getRefMethods(node));
        refEntities.addAll(getRefClasses(node));
        refEntities.addAll(getRefInterfaces(node));
        return refEntities;
    }

    public static Collection<MethodEntity> getRefMethods(Node node) {
        Collection<MethodEntity> methodEntities = new ArrayList<>();
        node.getRelationships(RelationshipType.withName("codeMention")).forEach(relationship -> {
            if(checkNodeLabel(relationship.getStartNode(), "StackOverflowQuestion") || checkNodeLabel(relationship.getStartNode(), "StackOverflowAnswer")) {
                Node srcNode = relationship.getEndNode();
                if (checkNodeLabel(srcNode, "Method")) {
                    MethodEntity methodEntity = buildMethod(srcNode);
                    methodEntities.add(methodEntity);
                }
            }
        });
        return methodEntities;
    }

    public static Collection<ClassEntity> getRefClasses(Node node) {
        Collection<ClassEntity> classEntities = new ArrayList<>();
        node.getRelationships(RelationshipType.withName("codeMention")).forEach(relationship -> {
            if(checkNodeLabel(relationship.getStartNode(), "StackOverflowQuestion") || checkNodeLabel(relationship.getStartNode(), "StackOverflowAnswer")) {
                Node srcNode = relationship.getEndNode();
                if (checkNodeLabel(srcNode, "Class")) {
                    ClassEntity classEntity = buildClass(srcNode);
                    classEntities.add(classEntity);
                }
            }
        });
        return classEntities;
    }

    public static Collection<InterfaceEntity> getRefInterfaces(Node node) {
        Collection<InterfaceEntity> interfaceEntities = new ArrayList<>();
        node.getRelationships(RelationshipType.withName("codeMention")).forEach(relationship -> {
            if(checkNodeLabel(relationship.getStartNode(), "StackOverflowQuestion") || checkNodeLabel(relationship.getStartNode(), "StackOverflowAnswer")) {
                Node srcNode = relationship.getEndNode();
                if (checkNodeLabel(srcNode, "Interface")) {
                    InterfaceEntity interfaceEntity = buildInterface(srcNode);
                    interfaceEntities.add(interfaceEntity);
                }
            }
        });
        return interfaceEntities;
    }

    public static MethodEntity buildMethod(Node node) {
        MethodEntity oneMethodEntity = new MethodEntity();
        oneMethodEntity.build(node);
        return oneMethodEntity;
    }

    public static ClassEntity buildClass(Node node) {
        ClassEntity oneClassEntity = new ClassEntity();
        oneClassEntity.build(node);
        return oneClassEntity;
    }

    public static InterfaceEntity buildInterface(Node node) {
        InterfaceEntity oneInterfaceEntity = new InterfaceEntity();
        oneInterfaceEntity.build(node);
        return oneInterfaceEntity;
    }

    public static boolean checkNodeLabel(Node node, String target) {
        for(Label label : node.getLabels()) {
            if(label.toString().contains(target)) {
                return true;
            }
        }
        return false;
    }
}
