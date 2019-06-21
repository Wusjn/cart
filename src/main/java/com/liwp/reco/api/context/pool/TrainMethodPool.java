package com.liwp.reco.api.context.pool;

import com.liwp.reco.api.context.info.KeyPairInfo;
import com.liwp.reco.api.query.entity.entities.MethodEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by liwp on 2017/6/20.
 */
public class TrainMethodPool {
    public static List<List<MethodEntity>> methodBlocks = new ArrayList<>();
    public Map<MethodEntity, Integer> methods = new HashMap<>();
    public Map<KeyPairInfo, Integer> methodDistributions = new HashMap<>();
    public int num = 1;
    public int jdkNum = 0;
    public int gate = 100;
    public int realNum = 0;

    public String projectName = "log4j";

    public String dirPath = "C:/Users/dell/Desktop/cart/" + projectName;
    public String numsFilePath = "C:/Users/dell/Desktop/cart/" + projectName + "/nums.txt";
    public String methodsFilePath = "C:/Users/dell/Desktop/cart/" + projectName + "/methods.txt";
    public String realpath = "C:/Users/dell/Desktop/cart/" + projectName + "/realNums.txt";

    public void build() {
        methodBlocks.forEach(block -> {
            //System.out.println(num ++);
            //System.out.println(block.size() +" " + methods.size());
            block.forEach(method -> {
                //System.out.println(method.displayName());
                if (methods.containsKey(method)) {
                    methods.put(method, methods.get(method) + 1);
                } else {
                    methods.put(method, 1);
                }
            });

            Deque<MethodEntity> contextDeque = new LinkedList<>();
            block.forEach(value -> {
                contextDeque.forEach(key -> {
                    KeyPairInfo info = new KeyPairInfo(key, value);
                    if(methodDistributions.containsKey(info)) {
                        methodDistributions.put(info, methods.get(info.key) + 1);
                    } else {
                        methodDistributions.put(info, 1);
                    }
                });

                contextDeque.addLast(value);
                if(contextDeque.size() > 10) {
                    contextDeque.removeFirst();
                }
            });
        });
    }

    public int nums = 0;
    public void count() {

        methodBlocks.forEach(block -> {
            nums += block.size();
        });
        System.out.println("All api nums: " + nums);
    }

    public void printPackages() {
        count();
        try {
            File dir = new File(dirPath);
            if(!dir.exists()) dir.mkdir();
            HashMap<String, Integer> methodMap = new HashMap<>();
            HashMap<String, Integer> methodNumMap = new HashMap<>();
            TreeMap<String, Integer> JDKMap = new TreeMap<>();

            FileWriter writer = new FileWriter(numsFilePath);
            FileWriter writer2 = new FileWriter(methodsFilePath);
            writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
            writer2.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
            methodBlocks.forEach(block -> {
                block.forEach(method -> {
                    String one = method.displayName().substring(8);
                    one = one.substring(0, one.lastIndexOf('('));
                    //one = one.substring(0, one.lastIndexOf("."));
                    //one = one.substring(0, one.lastIndexOf("."));
                    methodNumMap.putIfAbsent(one, 0);
                    methodNumMap.put(one, methodNumMap.get(one) + 1);
                    if(!methodMap.containsKey(one)) {
                        methodMap.put(one, num++);
                        if(one.startsWith("java.")) {
                            jdkNum ++;
                        }
                    }
                    try {
                        if (one.startsWith("java.")) {
                            writer.write(String.valueOf(methodMap.get(one)) + '\n');
                            writer2.write("JDK: " + one + '\n');
                        } else {
//                            writer.write(String.valueOf(methodMap.get(one)) + '\n');
//                            writer2.write(one + '\n');
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                try {
                    writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer2.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer2.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer2.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer2.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                    writer2.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            writer.close();
            writer2.close();

            HashSet<Integer> realSet = new HashSet<>();
            methodNumMap.forEach((s, i) -> {
                if(s.startsWith("java.") && i >= 0) {
                    realSet.add(methodMap.get(s));
                    JDKMap.put(s, i);
                }
            });
            BufferedReader reader = new BufferedReader(new FileReader(numsFilePath));
            FileWriter writer3 = new FileWriter(realpath);
            String line = reader.readLine();
            while(line != null) {
                int one = Integer.valueOf(line);
                if(realSet.contains(one)) {
                    writer3.write(String.valueOf(one + 1000000) + '\n');
                } else {
                    writer3.write(String.valueOf(one) + '\n');
                }
                line = reader.readLine();
            }
            reader.close();
            writer3.close();

//            JDKMap.forEach((s, i) -> {
//                System.out.println(s + " " + i);
//            });
            System.out.println("realNum: " + realSet.size());
            System.out.println("methodMapNum: " + methodMap.size());
            System.out.println("RealJDKNum: " + JDKMap.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("APINum: " + num);
            System.out.println("JDKNum: " + jdkNum);
        }
    }

    public void printBlocks() {
        try {
            HashMap<String, Integer> methodMap = new HashMap<>();
            HashMap<String, Integer> methodNumMap = new HashMap<>();
            TreeMap<String, Integer> JDKMap = new TreeMap<>();

            FileWriter writer = new FileWriter(numsFilePath);
            FileWriter writer2 = new FileWriter(methodsFilePath);
            methodBlocks.forEach(block -> {
                block.forEach(method -> {
                    try {
                        String one = method.displayName().substring(8);
                        methodNumMap.putIfAbsent(one, 0);
                        methodNumMap.put(one, methodNumMap.get(one) + 1);
                        if (!methodMap.containsKey(one)) {
                            methodMap.put(one, num ++);
                            if (one.startsWith("java.")) {
                                jdkNum ++;
                            }
                        }
                        if(one.startsWith("java.")) {
                            writer.write(String.valueOf(methodMap.get(one)) + '\n');
                            writer2.write("JDK: " + one + '\n');
                        } else {
//                            writer.write(String.valueOf(methodMap.get(one)) + '\n');
//                            writer2.write(one + '\n');
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                try {
                    writer.write("0\n0\n0\n0\n0\n0\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            writer.close();
            writer2.close();

            HashSet<Integer> realSet = new HashSet<>();
            methodNumMap.forEach((s, i) -> {
                if(s.startsWith("java.") && i >= 10) {
                    realSet.add(methodMap.get(s));
                    JDKMap.put(s, i);
                }
            });
            BufferedReader reader = new BufferedReader(new FileReader(numsFilePath));
            FileWriter writer3 = new FileWriter(realpath);
            String line = reader.readLine();
            while(line != null) {
                int one = Integer.valueOf(line);
                if(realSet.contains(one)) {
                    writer3.write(String.valueOf(one + 500000) + '\n');
                } else {
                    writer3.write(String.valueOf(one) + '\n');
                }
                line = reader.readLine();
            }
            reader.close();
            writer3.close();

            JDKMap.forEach((s, i) -> {
                System.out.println(s + " " + i);
            });
            System.out.println("realNum: " + realSet.size());
            System.out.println("methodMapNum: " + methodMap.size());
            System.out.println("RealJDKNum: " + JDKMap.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("APINum: " + num);
            System.out.println("JDKNum: " + jdkNum);

        }



    }
}
