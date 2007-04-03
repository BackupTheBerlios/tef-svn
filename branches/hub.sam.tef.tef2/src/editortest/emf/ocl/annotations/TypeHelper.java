package editortest.emf.ocl.annotations;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ocl.expressions.OCLExpression;
import org.eclipse.emf.ocl.expressions.PropertyCallExp;
import org.eclipse.emf.ocl.expressions.VariableExp;

public class TypeHelper {
	public static EClassifier getTypeFor(OCLExpression expression) {
		if (expression instanceof VariableExp) {
			return ((VariableExp)expression).getReferredVariable().getType();
		} else if (expression instanceof PropertyCallExp) {
			return ((PropertyCallExp)expression).getReferredProperty().getEType();
		} else {
			return null;
		}
	}
}
