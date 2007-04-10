package editortest.emf.ocl.annotations;

import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.ASTElementNode;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ocl.expressions.OperationCallExp;
import org.eclipse.emf.ocl.expressions.PropertyCallExp;
import org.eclipse.emf.ocl.types.TypesFactory;
import org.eclipse.emf.ocl.utilities.PredefinedType;

import cmof.Constraint;
import editortest.emf.model.EMFModel;
import editortest.emf.model.EMFModelElement;

public class FeatureResolver implements IIdentifierResolver {

	public IModelElement resolveIdentifier(IModel model, ASTElementNode node,
			IModelElement context, IModelElement topLevelElement,
			IMetaModelElement expectedType, String property) {
		String name = node.getNode("name").getContent();
		EObject eContext = (EObject)((EMFModelElement)context).getEMFObject();
		while (eContext != null) {
			if (eContext instanceof PropertyCallExp) {
				PropertyCallExp propertyCall = (PropertyCallExp)eContext;				
				EClassifier sourceType = TypeHelper.getTypeFor(propertyCall.getSource());
				if (sourceType == null) {
					System.out.println(getClass().getCanonicalName() + ": sourceType not set");
					return null;
				}
				if (sourceType instanceof EClass) {
					EClass sourceClass = (EClass)sourceType;
					for (Object featureObj: sourceClass.getEAllStructuralFeatures()) {
						EStructuralFeature feature = (EStructuralFeature)featureObj;
						if (name.equals(feature.getName())) {
							// TODO type check
							return (IModelElement)EMFModel.getModelForEMFObject(feature);
						}
					}
					return null;
				} 
				return null;
			} else if (eContext instanceof OperationCallExp) {
				OperationCallExp operationCall = (OperationCallExp)eContext;				
				EClassifier sourceType =  TypeHelper.getTypeFor(operationCall.getSource());
				
				if (sourceType == null) {
					System.out.println(getClass().getCanonicalName() + ": sourceType not set");
					return null;
				}
				
				PredefinedType predefinedSourceType = TypeHelper.getPredefinedType(sourceType);
				if (sourceType instanceof EClass) {
					EClass sourceClass = (EClass)sourceType;
					for (Object operationObj: sourceClass.getEAllOperations()) {
						EOperation operation = (EOperation)operationObj;
						if (name.equals(operation.getName())) {
							// TODO type check, parameter?
							return (IModelElement)EMFModel.getModelForEMFObject(operation);
						}
					}
					return null;
				} else if (predefinedSourceType != null) {
					for (Object operationObj: predefinedSourceType.getOperations()) {
						EOperation operation = (EOperation)operationObj;
						if (name.equals(operation.getName())) {
							// TODO type check, parameter?
							return (IModelElement)EMFModel.getModelForEMFObject(operation);
						}
					}
					return null;
				}
				return null;
			} else if (eContext instanceof Constraint) {
				return null;
			}
			eContext = eContext.eContainer();
		}
		return null;
	}

	

	public void addToEnvironment(IModelElement element) {
		// TODO Auto-generated method stub	
	}

	public void removeFromEnvironment(IModelElement element) {
		// TODO Auto-generated method stub	
	}
}
