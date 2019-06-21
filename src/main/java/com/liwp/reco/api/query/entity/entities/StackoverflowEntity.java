package com.liwp.reco.api.query.entity.entities;

import com.liwp.reco.api.query.entity.Entity;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import com.liwp.reco.api.query.mapper.utils.MapperUtils;

import java.util.Iterator;

/**
 * Created by liwp on 2017/5/3.
 */
@Getter
@Setter
public class StackoverflowEntity extends Entity {
    private String questionTitle = "";
    private String questionBody = "";
    private int answerScore = 0;
    private String answerBody = "";

    @Override
    public void build(Node node) {
        buildFromMethodNode(node);
    }

    @Override
    public String displayName() {
        return "Question: " + questionTitle;
    }

    private void buildFromMethodNode(Node methodNode) {
        Iterator<Relationship> itr = methodNode.getRelationships(RelationshipType.withName("codeMention")).iterator();
        while(itr.hasNext()) {
            Node soNode = itr.next().getStartNode();
            if(MapperUtils.checkNodeLabel(soNode, "StackOverflow")) {
                if (MapperUtils.checkNodeLabel(soNode, "StackOverflowQuestion")) {
                    this.setQuestionTitle(soNode.getProperty("title").toString());
                    this.setQuestionBody(soNode.getProperty("body").toString());
                    int score = -100;
                    Iterator<Relationship> it = soNode.getRelationships(RelationshipType.withName("haveSoAnswer")).iterator();
                    while (it.hasNext()) {
                        Node answerNode = it.next().getEndNode();
                        int oneScore = Integer.parseInt(answerNode.getProperty("score").toString());
                        if (oneScore > score) {
                            score = oneScore;
                            this.setAnswerScore(score);
                            this.setAnswerBody(answerNode.getProperty("body").toString());
                        }
                    }
                } else {
                    this.setAnswerScore(Integer.parseInt(soNode.getProperty("score").toString()));
                    this.setAnswerBody(soNode.getProperty("body").toString());
                    Node questionNode = soNode.getRelationships(RelationshipType.withName("haveSoAnswer")).iterator().next().getStartNode();
                    this.setQuestionTitle(questionNode.getProperty("title").toString());
                    this.setQuestionBody(questionNode.getProperty("body").toString());
                }
                break;
            }
        }
    }

}
