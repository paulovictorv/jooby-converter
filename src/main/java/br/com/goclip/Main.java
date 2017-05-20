package br.com.goclip;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.Optional;

/**
 * Created by paulo on 19/05/17.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream controller = new FileInputStream("");
        CombinedTypeSolver typeSolver = new CombinedTypeSolver(new JavaParserTypeSolver(new File("")));
        CompilationUnit parse = JavaParser.parse(controller);
        parse.getChildNodesByType(MethodDeclaration.class).parallelStream()
                .filter(methodDeclaration -> methodDeclaration.getName().toString().equals("listAllBatches"))
                .forEach(methodDeclaration -> {
                    EnumSet<Modifier> es = EnumSet.of(Modifier.PUBLIC);
                    methodDeclaration.getChildNodesByType(ReturnStmt.class).stream()
                            .map(returnStmt -> returnStmt.getExpression().filter(expression -> !expression.toString().contains("internalServerError")))
                            .filter(Optional::isPresent)
                            .forEach(returnStmt -> {
                                NodeList<Expression> arguments = ((MethodCallExpr) returnStmt.get().getChildNodes().get(1)).getArguments();
                                Expression expression = arguments.get(0);
                                System.out.println(JavaParserFacade.get(typeSolver).getType(expression));
                            });

//                    MethodDeclaration joobyMethod = new MethodDeclaration(es, null, methodDeclaration.getName());
                });
    }

}
