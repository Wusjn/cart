package com.liwp.reco.api.context;

import com.liwp.reco.api.context.pool.TrainMethodPool;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.io.File;
import java.util.*;

/**
 * Created by liwp on 2017/6/18.
 */
public class TrainSourceParser {

    public static String dirPath = "E:/test/log4j";
    public static boolean dirs = false;

    public static void doParse() {
        if(dirs == false) {
            parse(dirPath);
            return;
        }
        File dir = new File(dirPath);
        for(File oneFile : dir.listFiles()) {
            System.out.println("Trying to deal with: " + oneFile.getAbsolutePath());
            parse(oneFile.getAbsolutePath());
            System.out.println(TrainMethodPool.methodBlocks.size());
        }
    }

    public static void parse(String path) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Collection<File> javaFiles = FileUtils.listFiles(new File(path), new String[]{"java"}, true);
        Set<String> srcPathSet = new HashSet<>();
        Set<String> srcFolderSet = new HashSet<>();
        for (File javaFile : javaFiles) {
            String srcPath = javaFile.getAbsolutePath();
            String srcFolderPath = javaFile.getParentFile().getAbsolutePath();
            srcPathSet.add(srcPath);
            srcFolderSet.add(srcFolderPath);
        }
        String[] srcPaths = new String[srcPathSet.size()];
        srcPathSet.toArray(srcPaths);
        String[] srcFolderPaths = new String[srcFolderSet.size()];
        srcFolderSet.toArray(srcFolderPaths);

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

                javaUnit.accept(new RecoASTVisitor(RecoASTVisitor.Type.train));

            }
        }, null);
    }
}
