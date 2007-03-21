package hub.sam.tef;

import hub.sam.tef.parse.ParserBasedReconcilingStrategy;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.reconciler.AbstractReconciler;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.source.ISourceViewer;

public class TEFReconciler extends AbstractReconciler {

	private final IReconcilingStrategy fParserBasedReconcilingStrategy;	
	private final ISourceViewer fViewer;		
	
	public TEFReconciler(final ISourceViewer viewer) {
		super();
		fViewer = viewer;
		fParserBasedReconcilingStrategy = new ParserBasedReconcilingStrategy(viewer);
	}

	@Override
	protected void process(DirtyRegion dirtyRegion) {
		fParserBasedReconcilingStrategy.reconcile(dirtyRegion, null);		
	}

	@Override
	protected void reconcilerDocumentChanged(IDocument newDocument) {		
		fParserBasedReconcilingStrategy.setDocument(newDocument);
	}

	public IReconcilingStrategy getReconcilingStrategy(String contentType) {
		throw new RuntimeException("assert");
	}

}
