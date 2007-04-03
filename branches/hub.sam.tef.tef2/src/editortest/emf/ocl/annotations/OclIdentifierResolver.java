package editortest.emf.ocl.annotations;

import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.ASTElementNode;

import org.eclipse.emf.ecore.EClass;

import editortest.emf.EMFIdentifierResolver;
import editortest.emf.model.EMFMetaModelElement;

public class OclIdentifierResolver extends EMFIdentifierResolver implements IIdentifierResolver {

	private final IIdentifierResolver variableResolver = new VariableResolver();
	private final IIdentifierResolver featureResolver = new FeatureResolver();
	
	public IModelElement resolveIdentifier(IModel model, ASTElementNode node,
			IModelElement context, IModelElement topLevelElement,
			IMetaModelElement expectedType, String property) {
		EClass metaType = ((EMFMetaModelElement)expectedType).getEMFObject();
		if (org.eclipse.emf.ocl.expressions.ExpressionsPackage.eINSTANCE.getVariable().isSuperTypeOf(metaType)) {
			return variableResolver.resolveIdentifier(model, node, context, topLevelElement, expectedType, property);
		} else if (org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEStructuralFeature().isSuperTypeOf(metaType)) {
			return featureResolver.resolveIdentifier(model, node, context, topLevelElement, expectedType, property);
		} else if (org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEOperation().isSuperTypeOf(metaType)) {
			return featureResolver.resolveIdentifier(model, node, context, topLevelElement, expectedType, property);
		} else {
			return super.resolveIdentifier(model, node, context, topLevelElement, expectedType, property);
		}
	}

}
