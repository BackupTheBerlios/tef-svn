/**
 * 
 */
package hub.sam.tef.tslsemantics;

import hub.sam.tef.modelcreating.IModelCreatingContext;
import hub.sam.tef.modelcreating.ModelCreatingException;
import hub.sam.tef.modelcreating.ParseTreeNode;
import hub.sam.tef.semantics.AbstractPropertySemantics;
import hub.sam.tef.semantics.Error;
import hub.sam.tef.semantics.IPropertyCreationSemantics;
import hub.sam.tef.semantics.IPropertyResolutionSemantics;
import hub.sam.tef.semantics.UnresolvableReferenceError;
import hub.sam.tef.tsl.CompositeBinding;
import hub.sam.tef.tsl.PropertyBinding;
import hub.sam.tef.tsl.ReferenceBinding;
import hub.sam.tef.tsl.Rule;
import hub.sam.tef.tsl.Syntax;
import hub.sam.tef.tslsemantics.TslModelCreatingContext.IEcoreModel;

import java.util.HashSet;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class TslBindingResolutionSemantics extends
		AbstractPropertySemantics implements IPropertyResolutionSemantics,
		IPropertyCreationSemantics {

	/**
	 * Is bound to the platform URI for the referenced meta-model.
	 */
	@Override
	public void addValue(ParseTreeNode parseTreeNode, Object actual,
			Object value, IModelCreatingContext context,
			CompositeBinding binding) throws ModelCreatingException {
		try {
			((IEcoreModel)context.getAdapter(IEcoreModel.class)).loadModel((String)value);
			((Syntax)actual).setMetaModelPlatformURI((String)value);
		} catch (Exception ex) {
			context.addError(new Error(parseTreeNode.getPosition(), "Cannot load ecore file: " + 
					ex.getLocalizedMessage()));
		}
	}
	
	/**
	 * Helper function that gets the rule that directly or indirectly is a
	 * container for the given object.
	 */
	private Rule getContainingRule(EObject object) {
		while (object != null) {
			if (object instanceof Rule) {
				return (Rule)object;
			} else {
				object = object.eContainer();
			}
		}
		return null;
	}

	@Override
	public UnresolvableReferenceError resolve(ParseTreeNode parseTreeNode,
			Object actual, Object value, IModelCreatingContext context,
			ReferenceBinding binding) throws ModelCreatingException {			
		String id = parseTreeNode.getNodeText();				
		EObject resolution = null;			
		if (actual instanceof PropertyBinding) {
			EClass correspondingMetaClass = SyntaxHelper.findCorrespondingElementBinding(					
					getContainingRule((EObject)actual), 
					(Syntax)context.getResource().getContents().get(0), new HashSet<Rule>());
			if (correspondingMetaClass == null) {
				return new UnresolvableReferenceError( 
						"Cannot resolve the meta-class for the given property",
						parseTreeNode);
			}
			for (EStructuralFeature feature: 
					correspondingMetaClass.getEAllStructuralFeatures()) {
				if (feature.getName().equals(id)) {
					resolution = feature;
				} 
			}
			if (resolution == null) {
				return new UnresolvableReferenceError(
						"Class " + correspondingMetaClass.getName() + 
						" does not contain a structural feature with the given name",
						parseTreeNode);
			}
		} else {							
			try {
				resolution = resolve("name", id, binding.getProperty()
						.getEType(), context.getAdapter(IEcoreModel.class).getAllContents());
			} catch (AmbiguousReferenceException ex) {
				context.addError(new Error(parseTreeNode.getPosition(), 
						"Reference is ambiguous"));				
			}				
		}
		if (resolution != null) {
			setValue((EObject) actual, resolution, binding
					.getProperty());
			return null;
		} else {
			return new UnresolvableReferenceError(
					"Could not resolve " + id + ".", parseTreeNode);
		}			
	}
}