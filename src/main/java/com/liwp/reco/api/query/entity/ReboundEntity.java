package com.liwp.reco.api.query.entity;

import com.liwp.reco.api.query.entity.entities.MethodEntity;
import com.liwp.reco.api.query.factory.StaticValueFactory;

import java.util.Arrays;

/**
 * Created by liwp on 2017/5/7.
 */
public abstract class ReboundEntity extends Entity {

    @Override
    public String urlPath() {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(StaticValueFactory.LUCENE_CORE_BASEURL);
        String packageSegments[] = prefix().split("\\.");
        Arrays.stream(packageSegments).forEach(packageName -> urlBuilder.append("/" + packageName));
        urlBuilder.append(".html");
        return urlBuilder.toString();
    }

    @Override
    public String name() {
        return "";
    }
    @Override
    public int compareTo(Entity e) {
        if(e instanceof MethodEntity) {
            return 1;
        } else {
            if(e.getTimes() != this.getTimes()) {
                return e.getTimes().compareTo(this.getTimes());
            } else {
                return e.displayName().compareTo(this.displayName());
            }
        }
    }
}
