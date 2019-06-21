package com.liwp.reco.api.query.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.graphdb.Node;

/**
 * Created by liwp on 2017/5/6.
 */
@Setter
@Getter
public abstract class Entity implements Comparable<Entity>{

    abstract public void build(Node node);
    abstract public String displayName();

    public String prefix() {return "";}
    public String suffix() {return "";}
    public String name() {return "";}
    public String refSoTitle() {return "";}
    public String refSoQuestionBody() {return "";}
    public String refSoAnswerBody() {return "";}

    public String urlPath() {return "";}

    private Integer times = 0;

    @Override
    public int hashCode() {
        return this.displayName().hashCode();
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof Entity) {
            Entity entity = (Entity) anObject;
            return this.displayName().equals(entity.displayName());
        }
        return false;
    }

    @Override
    public int compareTo(Entity e) {
        return e.getTimes().compareTo(this.getTimes());
    }

}
