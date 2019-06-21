package com.liwp.reco.api.context.info;

import com.liwp.reco.api.query.entity.entities.MethodEntity;

/**
 * Created by liwp on 2017/6/18.
 */
public class KeyPairInfo {

    public KeyPairInfo(MethodEntity key, MethodEntity value) {
        this.key = key;
        this.value = value;
        StringBuilder builder = new StringBuilder()
                .append(key.displayName())
                .append('#')
                .append(value.displayName());
        name = builder.toString();
    }

    public MethodEntity key;
    public MethodEntity value;
    private String name;

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
