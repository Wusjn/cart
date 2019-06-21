package com.liwp.reco.api.context;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by liwp on 2017/6/18.
 */
public class TestSourceParser {

    public static void parse() {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Collection<File> javaFiles = FileUtils.listFiles(new File("/Users/liwp/Desktop/lucene/src"), new String[]{"java"}, true);

        String inputPath = "/Users/liwp/Desktop/lucene/src/lucene-6.4.1/core/src/java/org/apache/lucene/index/BufferedUpdates.java";
        String inputFolderPath = "/Users/liwp/Desktop/lucene/src/lucene-6.4.1/core/src/java/org/apache/lucene/index";

        File file = new File(inputPath);
        String all = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            all = "";
            String line = reader.readLine();
            while(line != null) {
                all += line + "\n";
                line = reader.readLine();
            }
            //System.out.println(all);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(new File("/Users/liwp/Desktop/testSource.java"));
            writer.write(all);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] srcPaths = new String[1];
        srcPaths[0] = "/Users/liwp/Desktop/testSource.java";
        String[] srcFolderPaths = new String[1];
        srcFolderPaths[0] = "/Users/liwp/Desktop";

        parser.setEnvironment(null, srcFolderPaths, null, true);
        parser.setResolveBindings(true);
        Map<String, String> options = new Hashtable<>();
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(options);
        parser.setBindingsRecovery(true);


        parser.createASTs(srcPaths, null, new String[]{}, new FileASTRequestor() {
            @Override
            public void acceptAST(String sourceFilePath, CompilationUnit javaUnit) {

                javaUnit.accept(new RecoASTVisitor(RecoASTVisitor.Type.test));

            }
        }, null);
    }
}
