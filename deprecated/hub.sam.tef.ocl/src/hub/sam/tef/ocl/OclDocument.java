package hub.sam.tef.ocl;

import hub.sam.tef.annotations.IAnnotationModelProvider;
import hub.sam.tef.annotations.IChecker;
import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.completion.ICompletionComputer;
import hub.sam.tef.documents.DocumentModel;
import hub.sam.tef.documents.TEFDocument;
import hub.sam.tef.layout.AbstractLayoutManager;
import hub.sam.tef.layout.ExpressionLayout;
import hub.sam.tef.ocl.annotations.OclChecker;
import hub.sam.tef.ocl.annotations.OclIdentifierResolver;
import hub.sam.tef.ocl.completion.OclCollectionOperationCompletion;
import hub.sam.tef.ocl.completion.OclOperationCallExpCompletion;
import hub.sam.tef.ocl.completion.OclPropertyCallExpCompletion;
import hub.sam.tef.ocl.completion.OclSingleIdentifierSchemeCompletion;
import hub.sam.tef.ocl.templates.ConstraintTemplate;
import hub.sam.tef.templates.Template;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.emf.ecore.EObject;


public class OclDocument extends TEFDocument {
	
	//private final OclChecker fChecker = new OclChecker();
	private final OclIdentifierResolver fIdentifierResolver = new OclIdentifierResolver();
	private final OclSingleIdentifierSchemeCompletion fSingleIdentifierSchemeCompletion = new OclSingleIdentifierSchemeCompletion();
	
	@Override
	public Template createTopLevelTemplate(IAnnotationModelProvider annotationModelProvider) {
		return new ConstraintTemplate(getModelProvider(), getModelProvider().getModel().getMetaElement("Constraint"));
	}

	public IIdentifierResolver getIdentityResolver() {
		return fIdentifierResolver;
	}
		
	public IChecker getChecker() {		
		//return fChecker;
		return null;
	}	

	@Override
	protected AbstractLayoutManager createLayout() {
		return new ExpressionLayout();
	}

	public Collection<ICompletionComputer> getCompletions() {
		Collection<ICompletionComputer> computers = new Vector<ICompletionComputer>();
		computers.add(new OclPropertyCallExpCompletion());
		computers.add(new OclOperationCallExpCompletion());
		computers.add(new OclCollectionOperationCompletion());
		computers.add(fSingleIdentifierSchemeCompletion);
		return computers;
	}
	
	public void setContext(EObject context) {		
		fIdentifierResolver.setContext(context);
		fSingleIdentifierSchemeCompletion.setContext(context);
		if (getModelProvider() != null) {
			((DocumentModel)getModelProvider()).reconcile();
		}
	}
}
