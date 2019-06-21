package com.liwp.reco.api.context;

import com.liwp.reco.api.context.info.MethodInfo;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liwp on 2017/5/21.
 */
public class RecoASTVisitorUtils {
    public static void parseMethodBody(MethodInfo methodInfo, Block methodBody) {
        if (methodBody == null)
            return;
        List<Statement> statementList = methodBody.statements();
        List<Statement> statements = new ArrayList<Statement>();
        for (int i = 0; i < statementList.size(); i++) {
            statements.add(statementList.get(i));
        }
        for (int i = 0; i < statements.size(); i++) {

            if (statements.get(i).getNodeType() == ASTNode.BLOCK) {
                List<Statement> blockStatements = ((Block) statements.get(i)).statements();
                for (int j = 0; j < blockStatements.size(); j++) {
                    statements.add(i + j + 1, blockStatements.get(j));
                }
                continue;
            }
            if (statements.get(i).getNodeType() == ASTNode.ASSERT_STATEMENT) {
                Expression expression = ((AssertStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                expression = ((AssertStatement) statements.get(i)).getMessage();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
            }

            if (statements.get(i).getNodeType() == ASTNode.DO_STATEMENT) {
                Expression expression = ((DoStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                Statement doBody = ((DoStatement) statements.get(i)).getBody();
                if (doBody != null) {
                    statements.add(i + 1, doBody);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
                Expression expression = ((EnhancedForStatement) statements.get(i)).getExpression();
                Type type = ((EnhancedForStatement) statements.get(i)).getParameter().getType();
                methodInfo.variableTypes.addAll(getTypes(type));
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                Statement forBody = ((EnhancedForStatement) statements.get(i)).getBody();
                if (forBody != null) {
                    statements.add(i + 1, forBody);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.EXPRESSION_STATEMENT) {
                Expression expression = ((ExpressionStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.FOR_STATEMENT) {
                List<Expression> list = ((ForStatement) statements.get(i)).initializers();
                for (int j = 0; j < list.size(); j++) {
                    parseExpression(methodInfo, list.get(j));
                }
                Expression expression = ((ForStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                Statement forBody = ((ForStatement) statements.get(i)).getBody();
                if (forBody != null) {
                    statements.add(i + 1, forBody);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.IF_STATEMENT) {
                Expression expression = ((IfStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                Statement thenStatement = ((IfStatement) statements.get(i)).getThenStatement();
                Statement elseStatement = ((IfStatement) statements.get(i)).getElseStatement();
                if (elseStatement != null) {
                    statements.add(i + 1, elseStatement);
                }
                if (thenStatement != null) {
                    statements.add(i + 1, thenStatement);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.RETURN_STATEMENT) {
                Expression expression = ((ReturnStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.SWITCH_STATEMENT) {
                Expression expression = ((SwitchStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                List<Statement> switchStatements = ((SwitchStatement) statements.get(i)).statements();
                for (int j = 0; j < switchStatements.size(); j++) {
                    statements.add(i + j + 1, switchStatements.get(j));
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.THROW_STATEMENT) {
                Expression expression = ((ThrowStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.TRY_STATEMENT) {
                Statement tryStatement = ((TryStatement) statements.get(i)).getBody();
                if (tryStatement != null) {
                    statements.add(i + 1, tryStatement);
                }
                continue;
            }
            if (statements.get(i).getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
                Type type = ((VariableDeclarationStatement) statements.get(i)).getType();
                List<VariableDeclaration> list = ((VariableDeclarationStatement) statements.get(i)).fragments();
                methodInfo.variableTypes.addAll(getTypes(type));
                for (VariableDeclaration decStat : list) {
                    parseExpression(methodInfo, decStat.getInitializer());
                }
            }
            if (statements.get(i).getNodeType() == ASTNode.WHILE_STATEMENT) {
                Expression expression = ((WhileStatement) statements.get(i)).getExpression();
                if (expression != null) {
                    parseExpression(methodInfo, expression);
                }
                Statement whileBody = ((WhileStatement) statements.get(i)).getBody();
                if (whileBody != null) {
                    statements.add(i + 1, whileBody);
                }
            }
        }

    }

    private static void parseExpression(MethodInfo methodInfo, Expression expression) {
        if (expression == null) {
            return;
        }
        //System.out.println(expression.toString()+" "+Annotation.nodeClassForType(expression.getNodeType()));
        if (expression.getNodeType() == ASTNode.ARRAY_INITIALIZER) {
            List<Expression> expressions = ((ArrayInitializer) expression).expressions();
            for (Expression expression2 : expressions) {
                parseExpression(methodInfo, expression2);
            }
        }
        if (expression.getNodeType() == ASTNode.CAST_EXPRESSION) {
            parseExpression(methodInfo, ((CastExpression) expression).getExpression());
        }
        if (expression.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION) {
            parseExpression(methodInfo, ((ConditionalExpression) expression).getExpression());
            parseExpression(methodInfo, ((ConditionalExpression) expression).getElseExpression());
            parseExpression(methodInfo, ((ConditionalExpression) expression).getThenExpression());
        }
        if (expression.getNodeType() == ASTNode.INFIX_EXPRESSION) {
            parseExpression(methodInfo, ((InfixExpression) expression).getLeftOperand());
            parseExpression(methodInfo, ((InfixExpression) expression).getRightOperand());
        }
        if (expression.getNodeType() == ASTNode.INSTANCEOF_EXPRESSION) {
            parseExpression(methodInfo, ((InstanceofExpression) expression).getLeftOperand());
        }
        if (expression.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION) {
            parseExpression(methodInfo, ((ParenthesizedExpression) expression).getExpression());
        }
        if (expression.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
            parseExpression(methodInfo, ((PostfixExpression) expression).getOperand());
        }
        if (expression.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
            parseExpression(methodInfo, ((PrefixExpression) expression).getOperand());
        }
        if (expression.getNodeType() == ASTNode.THIS_EXPRESSION) {
            parseExpression(methodInfo, ((ThisExpression) expression).getQualifier());
        }
        if (expression.getNodeType() == ASTNode.METHOD_INVOCATION) {
            List<Expression> arguments = ((MethodInvocation) expression).arguments();
            IMethodBinding methodBinding = ((MethodInvocation) expression).resolveMethodBinding();
            if (methodBinding != null)
                methodInfo.methodCalls.add(methodBinding);
            for (Expression exp : arguments)
                parseExpression(methodInfo, exp);
            parseExpression(methodInfo, ((MethodInvocation) expression).getExpression());
        }
        if(expression.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) {
            List<Expression> arguments = ((ClassInstanceCreation) expression).arguments();
            IMethodBinding methodBinding = ((ClassInstanceCreation) expression).resolveConstructorBinding();
            if (methodBinding != null)
                methodInfo.methodCalls.add(methodBinding);
            for (Expression exp : arguments)
                parseExpression(methodInfo, exp);
            parseExpression(methodInfo, ((ClassInstanceCreation) expression).getExpression());
        }
        if (expression.getNodeType() == ASTNode.ASSIGNMENT) {
            parseExpression(methodInfo, ((Assignment) expression).getLeftHandSide());
            parseExpression(methodInfo, ((Assignment) expression).getRightHandSide());
        }
        if (expression.getNodeType() == ASTNode.QUALIFIED_NAME) {
            if (((QualifiedName) expression).getQualifier().resolveTypeBinding() != null) {
                String name = ((QualifiedName) expression).getQualifier().resolveTypeBinding().getQualifiedName() + "." + ((QualifiedName) expression).getName().getIdentifier();
                methodInfo.fieldUsesSet.add(name);
            }
            parseExpression(methodInfo, ((QualifiedName) expression).getQualifier());
        }

    }

    private static Set<String> getTypes(Type oType) {
        Set<String> types = new HashSet<String>();
        if (oType == null)
            return types;
        ITypeBinding typeBinding = oType.resolveBinding();
        if (typeBinding == null)
            return types;
        String str = typeBinding.getQualifiedName();
        String[] eles = str.split("[^A-Za-z0-9_\\.]+");
        for (String e : eles) {
            if (e.equals("extends"))
                continue;
            types.add(e);
        }
        return types;
    }
}
