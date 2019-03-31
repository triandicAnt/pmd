/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.apex.rule.codestyle;

import static net.sourceforge.pmd.properties.PropertyFactory.booleanProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTParameter;
import net.sourceforge.pmd.lang.apex.ast.ASTProperty;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;
import net.sourceforge.pmd.properties.PropertyDescriptor;

public class BooleanMethodNamingConventionsRule extends AbstractApexRule {
    /**
     * Methods that return boolean results should be named as predicate statements to denote this.
     * Allowed suffixes, ‘is’, ‘has’, ‘can’, ‘will’, ‘should’.
     * If skipMethodsWithParameters is True, skip for parametrized methods.
     */
    private static final String BOOLEAN_RETURN_TYPE = "boolean";
    private static final Set<String> PREFIX_SET = new HashSet<>();

    static {
        PREFIX_SET.add("is");
        PREFIX_SET.add("has");
        PREFIX_SET.add("will");
        PREFIX_SET.add("should");
        PREFIX_SET.add("can");
    }

    private static final PropertyDescriptor<Boolean> SKIP_METHODS_WITH_PARAMETERS
            = booleanProperty("skipMethodsWithParameters")
            .desc("Skip methods with parameters")
            .defaultValue(false)
            .build();

    public BooleanMethodNamingConventionsRule() {
        definePropertyDescriptor(SKIP_METHODS_WITH_PARAMETERS);

        addRuleChainVisit(ASTMethod.class);
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        if (isOverriddenMethod(node)
                || isPropertyAccessor(node)
                || isConstructor(node)) {
            return data;
        }
        List<ASTParameter> paramList = node.findDescendantsOfType(ASTParameter.class);
        if (!node.getReturnType().equalsIgnoreCase(BOOLEAN_RETURN_TYPE)
            || !paramList.isEmpty() && getProperty(SKIP_METHODS_WITH_PARAMETERS)) {
            return data;
        }
        String methodName = node.getImage();
        boolean found = false;
        for (String suffix : PREFIX_SET) {
            if (methodName.startsWith(suffix)) {
                found = true;
                break;
            }
        }
        if (!found) {
            addViolationWithMessage(
                    data, node,
                    "Method ''{0}'' name should start with ANY(‘is’, ‘has’, ‘can’, ‘will’, ‘should‘).",
                    new Object[] { methodName });
        }
        return data;
    }

    private boolean isOverriddenMethod(ASTMethod node) {
        return node.getModifiers().isOverride();
    }

    private boolean isPropertyAccessor(ASTMethod node) {
        return !node.getParentsOfType(ASTProperty.class).isEmpty();
    }

    private boolean isConstructor(ASTMethod node) {
        return node.isConstructor();
    }
}
