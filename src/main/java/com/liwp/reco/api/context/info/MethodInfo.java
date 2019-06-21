package com.liwp.reco.api.context.info;

import org.eclipse.jdt.core.dom.IMethodBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liwp on 2017/5/21.
 */
public class MethodInfo {

    public IMethodBinding methodBinding;

    public String name;
    public String returnString;
    public Set<String> returnTypes = new HashSet<String>();
    public String visibility;
    public boolean isConstruct;
    public boolean isAbstract;
    public boolean isFinal;
    public boolean isStatic;
    public boolean isSynchronized;
    public String content;
    public String comment = "";
    public String belongTo;
    public String paramString;
    public Set<String> paramTypes = new HashSet<String>();
    public Set<String> variableTypes = new HashSet<String>();
    public List<IMethodBinding> methodCalls = new ArrayList<>();
    public Set<String> fieldUsesSet = new HashSet<String>();
    public Set<String> throwSet = new HashSet<String>();

    public String hashName() {
        return belongTo + "." + name + "(" + paramString + ")";
    }

}
