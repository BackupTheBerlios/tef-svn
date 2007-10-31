package hub.sam.tef;

import hub.sam.tef.documents.TEFDocumentProvider;
import hub.sam.tef.emf.EMFTextDocumentProvider;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.text.IDocument;

/**
 * An eclipse text editor that uses a background emf model representation as defined
 * through given TEF templates. It can be used with the regular org.eclipse.ui.editors::editor
 * extension point.
 */
public abstract class TEFEMFTextEditor extends TEFEMFEditor {
			
	@Override
	protected TEFDocumentProvider createDocumentProvider() {
		return new EMFTextDocumentProvider() {

			@Override
			protected Iterable<EFactory> getFactory() {
				return getModelFactories();
			}

			@Override
			protected Iterable<EPackage> getPackage() {
				return getMetaModelPackages();
			}
			
			@Override
			public IDocument createEmptyDocument()  {
				return TEFEMFTextEditor.this.createEmptyDocument();
			}
		};
	}
}
