package com.liwp.reco.api.query.entity.entities;

import com.liwp.reco.api.query.entity.Entity;
import com.liwp.reco.api.query.factory.StaticValueFactory;
import com.liwp.reco.api.query.mapper.utils.MapperUtils;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import java.util.Arrays;

/**
 * Created by liwp on 2017/5/3.
 */
@Getter
@Setter
public class MethodEntity extends Entity {
    private String name;
    private String rt;
    private String belongTo;
    private String absoluteName;
    private String params;
    private String comment;
    private StackoverflowEntity refSoEntity = null;

    public void setAbsoluteName() {
        StringBuilder sbuilder = new StringBuilder();
        absoluteName = sbuilder
                .append(belongTo)
                .append(".")
                .append(name)
                .toString();
    }

    @Override
    public String prefix() {
        return belongTo;
    }

    @Override
    public String suffix() {
        return rt;
    }

    @Override
    public String name() {
        return name+"("+params +")";
    }

    @Override
    public String refSoTitle() {
        return refSoEntity == null ? "" : refSoEntity.getQuestionTitle();
    }

    @Override
    public String refSoQuestionBody() {
        return refSoEntity == null ? "" : refSoEntity.getQuestionBody();
    }

    @Override
    public String refSoAnswerBody() {
        return refSoEntity == null ? "" : refSoEntity.getAnswerBody();
    }

    @Override
    public String urlPath() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(StaticValueFactory.LUCENE_CORE_BASEURL);
        String packageSegments[] = prefix().split("\\.");
        String paramSegments[] = params.split(",");
        Arrays.stream(packageSegments).forEach(packageName -> urlBuilder.append("/" + packageName));
        urlBuilder
                .append(".html#")
                .append(name)
                .append("-");
        Arrays.stream(paramSegments).forEach(paramsName -> urlBuilder.append(paramsName.trim() + "-"));
        return urlBuilder.toString();
    }

    @Override
    public void build(Node node) {
        buildFromNode(node);
    }

    @Override
    public String displayName() {
        return "Method: " + absoluteName + "(" + params + ")";
    }

    private void buildFromNode(Node node) {
        try {
            this.setBelongTo(node.getSingleRelationship(RelationshipType.withName("haveMethod"), Direction.BOTH).getStartNode().getProperty("fullName").toString());
        } catch (Exception e) {
            //e.printStackTrace();
            this.setBelongTo("");
        }
        this.setName(node.getProperty("name").toString());
        this.setRt(node.getProperty("returnType").toString());
        this.setAbsoluteName(this.belongTo + "." + this.name);
        this.setParams(node.getProperty("paramType").toString());
        this.setComment(node.getProperty("comment").toString());
        this.refSoEntity = new StackoverflowEntity();
        this.refSoEntity.build(node);
    }

    @Override
    public int compareTo(Entity e) {
        if(e instanceof MethodEntity) {
            if(e.getTimes() != this.getTimes()) {
                return e.getTimes().compareTo(this.getTimes());
            } else {
                return e.displayName().compareTo(this.displayName());
            }
        } else {
            return -1;
        }
    }
}
