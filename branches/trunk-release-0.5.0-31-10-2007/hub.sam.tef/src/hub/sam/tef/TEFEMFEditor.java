package hub.sam.tef;

import hub.sam.tef.annotations.IAnnotationModelProvider;
import hub.sam.tef.documents.TEFDocument;
import hub.sam.tef.emf.model.EMFModelElement;
import hub.sam.tef.layout.AbstractLayoutManager;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.tdl.TDLDocument;
import hub.sam.tef.tdl.TDLTextDocumentProvider;
import hub.sam.tef.tdl.delegators.TemplateFactory;
import hub.sam.tef.tdl.model.Syntax;
import hub.sam.tef.templates.Template;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.text.IDocument;
import org.osgi.framework.Bundle;

public abstract class TEFEMFEditor extends AbstractTEFEditor {
	
	private final TemplateFactory templateFactory = new TemplateFactory();
	
	/**
	 * The plug-in bundle. It is used to resolve template definition file (tdlt) and
	 * ecore meta-model within the custom editor plug-in.
	 */
	protected abstract Bundle getBundle();
	
	/**
	 * All factories that this editor must use to create the background model.
	 */
	protected abstract Iterable<EFactory> getModelFactories();
	
	/**
	 * All packages with meta-model element that this editor must use to create
	 * the background model from.
	 */
	protected abstract Iterable<EPackage> getMetaModelPackages();
	
	/**
	 * The layout used in this editor (this should be moved to the template
	 * definition file.
	 */
	protected abstract AbstractLayoutManager getLayoutManager();
	
	/**
	 * A path within the custom editor plugin to the template definition file (tdlt).
	 */
	protected abstract String getTemplateDefinitionBundleResourcePath();
	
	/**
	 * A path to the meta-model within the custom editor plugin. The result
	 * can be empty, but then you have to override {@link this#getMetaModelResource()}
	 */
	protected abstract String getMetaModelBundleResourcePath();
	
	/**
	 * The resource that contains the ecore meta-model.
	 */
	protected URI getMetaModelResource() {
		return URI.createURI(getBundle().getEntry(getMetaModelBundleResourcePath()).toString());
	}
	
	/**
	 * Allows to register custom template implementations in addition to the
	 * templates defined in the template definition file.
	 */
	protected void registerTemplateClasses(TemplateFactory factory) {
		// empty
	}

	protected final IDocument createEmptyDocument()  {
		return new TEFDocument() {
			@Override
			protected AbstractLayoutManager createLayout() {
				return getLayoutManager();
			}

			@Override
			protected Template createTopLevelTemplate(IAnnotationModelProvider annotationProvider) {
				registerTemplateClasses(templateFactory);								
				Syntax syntax;
				try {
					syntax = loadSyntax();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}		
				return templateFactory.createTemplate(getModelProvider(), syntax.getTopLevelTemplate());		
			}
			
			private Syntax loadSyntax() throws IOException {
				TDLTextDocumentProvider documentProvider = new TDLTextDocumentProvider();
				TDLDocument document = (TDLDocument)documentProvider.createEmptyDocument();				
								
				final InputStream tdl = getBundle().getEntry(getTemplateDefinitionBundleResourcePath()).openStream();
				URI resource = getMetaModelResource();				
				documentProvider.getEditingDomain().getResourceSet().getResource(resource, true);		
				
				try {
					documentProvider.setDocumentContentManually(document, tdl, null);
				} catch (CoreException e) {
					throw new RuntimeException("could not load tdl file for this editor " + e.getMessage());			
				}
							
				IModelElement topLevelElement = document.getModelProvider().getTopLevelElement();
				if (topLevelElement != null) {
					return (Syntax)((EMFModelElement)topLevelElement).getEMFObject();
				} else {
					throw new RuntimeException("could not load tdl file for this editor.");
				}
			}				
		};
	}	
}