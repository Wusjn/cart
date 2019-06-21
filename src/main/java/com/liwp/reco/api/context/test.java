package com.liwp.reco.api.context;

import com.liwp.reco.api.context.pool.TestMethodPool;
import com.liwp.reco.api.context.pool.TrainMethodPool;


/**
 * Created by liwp on 2017/5/7.
 */
public class test {
    public static void main(String args[]) {
//        try {
//            System.setOut(new PrintStream(new File("/Users/liwp/Desktop/out.txt")));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        TestSourceParser.parse();
//        System.out.println("!!!!!!!");
//        TestMethodPool.methodBlocks.forEach(block -> {
//            System.out.println("\n");
//            block.forEach(methodEntity -> {
//                System.out.println(methodEntity.displayName());
//            });
//        });
//
//        TestMethodPool.build();
//        System.out.println(TestMethodPool.methodDistributions.size());

        TrainMethodPool trainMethodPool = new TrainMethodPool();
        TestMethodPool testMethodPool = new TestMethodPool();

        TrainSourceParser.doParse();
        System.out.println(trainMethodPool.methodBlocks.size());
        System.out.println(testMethodPool.methodBlocks.size());
        //trainMethodPool.build();
        trainMethodPool.printPackages();
//        System.out.println("2");
//        TestSourceParser.parse();
//        System.out.println("3");
//        testMethodPool.build();
//        System.out.println("4");
//        testMethodPool.predict(trainMethodPool);


    }
}
