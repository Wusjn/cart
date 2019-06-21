package com.liwp.reco.api.query.reco;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by liwp on 2017/5/7.
 */
public class QueryDealer {
    public static Collection<String> splitWords(String words) {
        return Arrays.stream(words.toLowerCase().split(" ")).collect(Collectors.toList());
    }
}
