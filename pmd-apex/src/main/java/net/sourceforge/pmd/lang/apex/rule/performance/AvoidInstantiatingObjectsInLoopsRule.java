/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.apex.rule.performance;

import net.sourceforge.pmd.lang.apex.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTDoLoopStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTForEachStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTForLoopStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTNewListInitExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTNewMapInitExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTNewObjectExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTNewSetInitExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTThrowStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTWhileLoopStatement;
import net.sourceforge.pmd.lang.apex.ast.AbstractApexNode;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;
import net.sourceforge.pmd.lang.ast.Node;

public class AvoidInstantiatingObjectsInLoopsRule extends AbstractApexRule {
    @Override
    public Object visit(ASTNewObjectExpression node, Object data) {
        if (insideLoop(node) && fourthParentNotThrow(node) && fourthParentNotReturn(node)) {
            addViolation(data, node);
        }
        return data;
    }

    @Override
    public Object visit(ASTNewSetInitExpression node, Object data) {
        if (insideLoop(node) && fourthParentNotThrow(node) && fourthParentNotReturn(node)) {
            addViolation(data, node);
        }
        return data;
    }

    @Override
    public Object visit(ASTNewListInitExpression node, Object data) {
        if (insideLoop(node) && fourthParentNotThrow(node) && fourthParentNotReturn(node)) {
            addViolation(data, node);
        }
        return data;
    }

    @Override
    public Object visit(ASTNewMapInitExpression node, Object data) {
        if (insideLoop(node) && fourthParentNotThrow(node) && fourthParentNotReturn(node)) {
            addViolation(data, node);
        }
        return data;
    }

    private boolean fourthParentNotThrow(AbstractApexNode node) {
        return !(node.jjtGetParent() instanceof ASTThrowStatement);
    }

    private boolean fourthParentNotReturn(AbstractApexNode node) {
        return !(node.jjtGetParent() instanceof ASTReturnStatement);
    }

    private boolean insideLoop(AbstractApexNode node) {
        Node n = node.jjtGetParent();
        boolean insideBlock = false;
        while (n != null) {
            if (n instanceof ASTBlockStatement) {
                insideBlock = true;
            }
            if (insideBlock && (n instanceof ASTDoLoopStatement
                    || n instanceof ASTWhileLoopStatement
                    || n instanceof ASTForLoopStatement
                    || n instanceof ASTForEachStatement)) {
                return true;
            }
            n = n.jjtGetParent();
        }
        return false;
    }
}
