package com.github.bingoohuang.utils.lang;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class Listt {
    public static <T> List<T> unique(List<T> original) {
        return newArrayList(newHashSet(original));
    }
}
