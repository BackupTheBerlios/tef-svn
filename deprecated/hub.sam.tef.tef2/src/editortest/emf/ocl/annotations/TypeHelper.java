package editortest.emf.ocl.annotations;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ocl.expressions.IteratorExp;
import org.eclipse.emf.ocl.expressions.OCLExpression;
import org.eclipse.emf.ocl.expressions.OperationCallExp;
import org.eclipse.emf.ocl.expressions.PropertyCallExp;
import org.eclipse.emf.ocl.expressions.Variable;
import org.eclipse.emf.ocl.expressions.VariableExp;
import org.eclipse.emf.ocl.parser.SemanticException;
import org.eclipse.emf.ocl.types.CollectionType;
import org.eclipse.emf.ocl.types.TypesFactory;
import org.eclipse.emf.ocl.types.TypesPackage;
import org.eclipse.emf.ocl.utilities.PredefinedType;

public class TypeHelper {
	public static EClassifier getTypeFor(OCLExpression expression) {
		if (expression instanceof VariableExp) {
			Variable var = ((VariableExp)expression).getReferredVariable();
			if (var != null) {
				EClassifier type = var.getType();
				if (type == null) {
					if (var.eContainer() instanceof IteratorExp) {
						IteratorExp iteratorExp = (IteratorExp)var.eContainer();
						return getTypeFor(iteratorExp.getSource());
					}
				}
				return type;	
			}		
			return null;
		} else if (expression instanceof PropertyCallExp) {
			EStructuralFeature property = ((PropertyCallExp)expression).getReferredProperty();
			if (property != null) {
				return property.getEType();
			}
			return null;
		} else if (expression instanceof OperationCallExp) {
			EOperation operation = ((OperationCallExp)expression).getReferredOperation();
			if (operation != null) {
				return operation.getEType();
			}
			return null;
		} else if (expression instanceof IteratorExp) {
			String iteratorName = ((IteratorExp)expression).getName();			
			try {
				PredefinedType predefinedSourceType = TypesFactory.eINSTANCE.createCollectionType();
				return predefinedSourceType.getResultTypeFor(null, 
						predefinedSourceType.getOperationCodeFor(iteratorName), null);
			} catch (SemanticException ex) {
				// TODO
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static PredefinedType getPredefinedType(EClassifier classifier) {
		EcorePackage ecore = EcorePackage.eINSTANCE;
		if (classifier instanceof PredefinedType) {
			return (PredefinedType)classifier;
		} else  if (classifier.equals(ecore.getEBoolean()) || classifier.equals(ecore.getEBooleanObject())) {
			return TypesFactory.eINSTANCE.createPrimitiveBoolean();
		} else {
			return null;
		}
	}
}
