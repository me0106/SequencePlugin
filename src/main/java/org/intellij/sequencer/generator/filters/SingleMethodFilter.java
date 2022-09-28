package org.intellij.sequencer.generator.filters;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.intellij.sequencer.openapi.Constants;
import org.intellij.sequencer.openapi.filters.MethodFilter;
import org.intellij.sequencer.util.MyPsiUtil;

import java.util.List;
import java.util.Objects;

/**
 * The method should be excluded.
 */
public class SingleMethodFilter implements MethodFilter {
    private final String _className;
    private final String _methodName;
    private final List<String> _argTypes;

    private final int _callOffset;

    public SingleMethodFilter(String className, String methodName, List<String> argTypes, int callOffset) {
        _className = className;
        _methodName = methodName;
        _argTypes = argTypes;
        _callOffset = callOffset;
    }

    public boolean allow(PsiElement psiElement, int callOffset) {
        if (psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            PsiClass containingClass = psiMethod.getContainingClass();
            boolean sameMethod = isSameClass(containingClass) && MyPsiUtil.isMethod(psiMethod, _methodName, _argTypes);
            if (!sameMethod) {
                return true;
            }
            System.out.println("------------------------------------------");
            System.out.println("SAME: ");
            System.out.println(callOffset);
            System.out.println(_callOffset);

            if (callOffset == _callOffset) {
                System.out.println("--------");
                System.out.println(callOffset);
                System.out.println(_callOffset);

                return false;
            }
        }
        return true;
    }

    private boolean isSameClass(PsiClass containingClass) {
        return _className.equals(Constants.ANONYMOUS_CLASS_NAME) && containingClass.getQualifiedName() == null ||
            _className.equals(containingClass.getQualifiedName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingleMethodFilter that = (SingleMethodFilter) o;
        return _className.equals(that._className) && _methodName.equals(that._methodName) && _argTypes.equals(that._argTypes) && _callOffset == that._callOffset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_className, _methodName, _argTypes);
    }
}
