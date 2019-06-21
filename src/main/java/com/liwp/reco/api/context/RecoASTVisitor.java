package com.liwp.reco.api.context;

import com.liwp.reco.api.context.info.MethodInfo;
import com.liwp.reco.api.context.pool.TestMethodPool;
import org.eclipse.jdt.core.dom.*;
import com.liwp.reco.api.context.pool.TrainMethodPool;
import com.liwp.reco.api.query.entity.entities.MethodEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liwp on 2017/5/21.
 */
public class RecoASTVisitor extends ASTVisitor{
    public enum Type{
        train, test
    }
    Type type;

    RecoASTVisitor(Type t) {
        type = t;
    }

    public boolean visit(MethodDeclaration methodDeclaration) {
        //System.out.println("::::");
        if(methodDeclaration.getBody() != null) {
            List statements = methodDeclaration.getBody().statements();
            List<MethodEntity> block = new ArrayList<>();

            //System.out.println(statement.getClass());
            MethodInfo info = new MethodInfo();
            RecoASTVisitorUtils.parseMethodBody(info, methodDeclaration.getBody());
            info.methodCalls.forEach(iMethodBinding -> {
                //System.out.println(iMethodBinding.getParameterTypes().length>0 ? iMethodBinding.getParameterTypes()[0]:0);
                //System.out.println(iMethodBinding.getReturnType());
                //System.out.println(iMethodBinding.getDeclaringClass().getBinaryName() + " " + iMethodBinding.getName());


                MethodEntity methodEntity = new MethodEntity();
                StringBuilder builder = new StringBuilder();
                Arrays.stream(iMethodBinding.getParameterTypes()).forEach(p -> builder.append(p.getName().toString()));
                methodEntity.setParams(builder.toString());
                methodEntity.setBelongTo(iMethodBinding.getDeclaringClass().getBinaryName().toString());
                methodEntity.setName(iMethodBinding.getName().toString());
                methodEntity.setRt(iMethodBinding.getReturnType().getName().toString());
                methodEntity.setAbsoluteName();

                block.add(methodEntity);
            });


            if(type == Type.train) {
                TrainMethodPool.methodBlocks.add(block);
            } else {
                TestMethodPool.methodBlocks.add(block);
            }
        }
        return true;
    }




}
