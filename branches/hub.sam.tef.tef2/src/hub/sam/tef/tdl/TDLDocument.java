package hub.sam.tef.tdl;

import java.util.Collection;
import java.util.Vector;

import editortest.emf.ocl.completion.OclPropertyCallExpCompletion;
import hub.sam.tef.annotations.IAnnotationModelProvider;
import hub.sam.tef.annotations.IIdentifierResolver;
import hub.sam.tef.completion.ICompletionComputer;
import hub.sam.tef.documents.TEFDocument;
import hub.sam.tef.emf.EMFIdentifierResolver;
import hub.sam.tef.reconciliation.syntax.AbstractLayoutManager;
import hub.sam.tef.reconciliation.syntax.BlockLayout;
import hub.sam.tef.tdl.completions.TDLChoiceClassReferenceCompletion;
import hub.sam.tef.tdl.completions.TDLElementClassReferenceCompletion;
import hub.sam.tef.tdl.completions.TDLReferenceClassReferenceCompletion;
import hub.sam.tef.tdl.completions.TDLTemplateReferenceCompletion;
import hub.sam.tef.tdl.templates.SyntaxTemplate;
import hub.sam.tef.templates.Template;

public class TDLDocument extends TEFDocument {

	@Override
	protected AbstractLayoutManager createLayout() {
		return new BlockLayout();
	}	

	@Override
	public IIdentifierResolver getIdentityResolver() {
		return new EMFIdentifierResolver();
	}

	@Override
	protected Template createTopLevelTemplate(IAnnotationModelProvider annotationProvider) {
		return new SyntaxTemplate(getModelProvider());
	}	
	
	public Collection<ICompletionComputer> getCompletions() {
		Collection<ICompletionComputer> computers = new Vector<ICompletionComputer>();
		computers.add(new TDLTemplateReferenceCompletion());		
		computers.add(new TDLChoiceClassReferenceCompletion());
		computers.add(new TDLElementClassReferenceCompletion());
		computers.add(new TDLReferenceClassReferenceCompletion());
		return computers;
	}
}
