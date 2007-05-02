package editortest.emf.ocl;

import hub.sam.tef.TEFEditor;
import hub.sam.tef.documents.TEFDocumentProvider;

import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class OclTextEditor extends TEFEditor {

	@Override
	protected TEFDocumentProvider createDocumentProvider() {
		return new OclTextDocumentProvider();
	}

	@Override
	protected SourceViewerConfiguration createSourceViewerConfiguration() {
		return new OclSourceViewerConfiguration();
	}

}
