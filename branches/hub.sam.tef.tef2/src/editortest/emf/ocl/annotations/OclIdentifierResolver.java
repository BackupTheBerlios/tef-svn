package editortest.emf.ocl.annotations;

import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.emf.EMFIdentifierResolver;
import hub.sam.tef.emf.model.EMFMetaModelElement;
import hub.sam.tef.emf.model.EMFModel;
import hub.sam.tef.emf.model.EMFModelElement;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.treerepresentation.ASTElementNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.examples.extlibrary.EXTLibraryPackage;
import org.eclipse.emf.ocl.expressions.ExpressionsFactory;
import org.eclipse.emf.ocl.expressions.ExpressionsPackage;
import org.eclipse.emf.ocl.expressions.IteratorExp;
import org.eclipse.emf.ocl.expressions.LetExp;
import org.eclipse.emf.ocl.expressions.OCLExpression;
import org.eclipse.emf.ocl.expressions.Variable;
import org.eclipse.emf.ocl.internal.l10n.OCLMessages;
import org.eclipse.emf.ocl.parser.EcoreEnvironmentFactory;
import org.eclipse.emf.ocl.parser.Environment;
import org.eclipse.emf.ocl.parser.EnvironmentFactory;
import org.eclipse.emf.ocl.types.impl.TypeUtil;
import org.eclipse.emf.ocl.uml.TypedElement;


public class OclIdentifierResolver extends EMFIdentifierResolver {

	private final IIdentifierResolver variableResolver = new VariableResolver();
	private final IIdentifierResolver featureResolver = new FeatureResolver();	
	
	private EnvironmentFactory environmentFactory = new EcoreEnvironmentFactory(EPackage.Registry.INSTANCE);
	private Environment environment = null;
	private final ExpressionsFactory fExpressionsFactory = ExpressionsFactory.eINSTANCE;
	
	public OclIdentifierResolver() {
		EClassifier contextType = EXTLibraryPackage.eINSTANCE.getBook();
		environment = environmentFactory.createClassifierContext(contextType);
		if (environment == null) {
			throw new RuntimeException("assert");
		}
		Variable selfVar = ExpressionsFactory.eINSTANCE.createVariable();
		selfVar.setName("self");
		selfVar.setType(contextType);
		environment.addElement("self", selfVar, true);
		environment.setSelfVariable(selfVar);
		expressionVariableForEnvironmentVariable.put(selfVar, selfVar);
	}

	/*
	@Override	
	public IModelElement resolveIdentifier(IModel model, ASTElementNode node, IModelElement context, IModelElement topLevelElement, IMetaModelElement expectedType, String property) {		
		EClass metaType = ((EMFMetaModelElement)expectedType).getEMFObject();
		checkForVariablesWithLazyType(); // TODO performance?
		if (org.eclipse.emf.ocl.expressions.ExpressionsPackage.eINSTANCE.getVariable().isSuperTypeOf(metaType)) {
			return (IModelElement)EMFModel.getModelForEMFObject(
					expressionVariableForEnvironmentVariable.get(environment.lookup(node.getNode("name").getContent())));			
		} else if (org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEStructuralFeature().isSuperTypeOf(metaType)) {			
			return (IModelElement)EMFModel.getModelForEMFObject(
					environment.lookupProperty(((TypedElement)((EMFModelElement)context).getEMFObject()).getType(), 
					node.getNode("name").getContent()));			
		} else if (org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEOperation().isSuperTypeOf(metaType)) {
			return featureResolver.resolveIdentifier(model, node, context, topLevelElement, expectedType, property);
		} else {
			return super.resolveIdentifier(model, node, context, topLevelElement, expectedType, property);
		}
			
	}
	*/
	
	private final Map<Variable, OCLExpression> variablesWithLazyType = new HashMap<Variable, OCLExpression>();
	private final Map<Variable, Variable> expressionVariableForEnvironmentVariable = new HashMap<Variable, Variable>();
	
	private void checkForVariablesWithLazyType() {
		Collection<Variable> toRemove = new Vector<Variable>();
		for (Variable var: variablesWithLazyType.keySet()) {
			EClassifier type = TypeHelper.getTypeFor(variablesWithLazyType.get(var));
			if (type != null) {
				genVariableDeclaration(var, var.getName(), type, false);
				toRemove.add(var);
			}
		}
		for (Object remove: toRemove) {
			variablesWithLazyType.remove(remove);
		}
	}

	@Override
	public void addToEnvironment(IModelElement element) {
		if (element instanceof EMFModelElement) {
			if (((EMFModelElement)element).getEMFObject().eClass().equals(ExpressionsPackage.eINSTANCE.getIteratorExp())) {
				IteratorExp iterator = (IteratorExp)((EMFModelElement)element).getEMFObject();
				for (Object varObj: iterator.getIterator()) {
					Variable var = (Variable)varObj;		
					EClassifier type = TypeHelper.getTypeFor(iterator.getSource());
					if (type == null) {
						variablesWithLazyType.put(var, iterator.getSource());
					} else {
						genVariableDeclaration(var, var.getName(), type, false);
					}
				}			
			} else if (((EMFModelElement)element).getEMFObject().eClass().equals(ExpressionsPackage.eINSTANCE.getLetExp())) {
				LetExp let = (LetExp)((EMFModelElement)element).getEMFObject();
				Variable var = let.getVariable();
				genVariableDeclaration(var, var.getName(), var.getType(), false);
			}
		}
	}

	@Override
	public void removeFromEnvironment(IModelElement element) {	
		if (element instanceof EMFModelElement) {
			if (((EMFModelElement)element).getEMFObject().eClass().equals(ExpressionsPackage.eINSTANCE.getIteratorExp())) {
				IteratorExp iterator = (IteratorExp)((EMFModelElement)element).getEMFObject();
				for (Object varObj: iterator.getIterator()) {
					Variable var = (Variable)varObj;
					variablesWithLazyType.remove(var);
					environment.deleteElement(var.getName());
				}			
			} else if (((EMFModelElement)element).getEMFObject().eClass().equals(ExpressionsPackage.eINSTANCE.getLetExp())) {
				LetExp let = (LetExp)((EMFModelElement)element).getEMFObject();
				Variable var = let.getVariable();
				environment.deleteElement(var.getName());
			}
		}
	}
		
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
	
	
	private Variable genVariableDeclaration(Variable var, String name, EClassifier type, boolean explicitFlag) {
		Variable vdcl = fExpressionsFactory.createVariable();		
		vdcl.setName(name);
		vdcl.setType(TypeUtil.resolveType(environment, type));
				
		boolean result = environment.addElement(name, vdcl, explicitFlag);
		if (!result) {
			if (name != null) {
				String message = OCLMessages.bind(
						OCLMessages.VariableUsed_ERROR_,
						name);
				// TODO ERROR(rule, message);
			} else {
				// TODO ERROR(rule, OCLMessages.VariableDeclaration_ERROR_);
			}
		}
		expressionVariableForEnvironmentVariable.put(vdcl, var);
		return vdcl;
	}
}
