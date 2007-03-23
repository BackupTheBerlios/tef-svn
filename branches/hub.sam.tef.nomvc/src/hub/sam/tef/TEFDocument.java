/*
 * Textual Editing Framework (TEF)
 * Copyright (C) 2006 Markus Scheidgen
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
 * MA 02111-1307 USA
 */
package hub.sam.tef;

import hub.sam.tef.controllers.IAnnotationModelProvider;
import hub.sam.tef.controllers.IDocumentModelProvider;
import hub.sam.tef.models.IModel;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.Template;
import hub.sam.tef.treerepresentation.ITreeContents;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.IndexTreeRepresentationSelector;
import hub.sam.tef.treerepresentation.ModelTreeContents;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.util.strings.Change;
import hub.sam.util.strings.Changes;
import hub.sam.util.trees.IChildSelector;
import hub.sam.util.trees.TreeIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * This class represent TEF documents as eclipse text documents
 * {@link IDocument}. In other words it wrapped a TEF text structures, starting
 * with a {@link DocumentText}, into an eclipse interface.
 * 
 * Instances of this class can run in two modi. One the TEF mode (the normal and
 * starting mode). All changes to the document caused by the eclipse editor
 * (user keystrokes mostly) are translated into TEF events. If these events can
 * be handled by TEF, nothing happens here. TEF will change the underlying
 * model, according listeners will change the TEF text structure, an according
 * updates will call {@link #doReplace(int, int, String)} in this class, which
 * forwards the changes to the eclipse editor. If an event can't be handled,
 * this document switched into the eclipse mode. In this mode the documents acts
 * like a normal eclipse document. It is now the responsibility of the installed
 * TEF reconciler to parse the current editor content at appropriate times and
 * finally change the model and therefore the TEX text structure. The TEF
 * reconciler is also responsibly to switch this document back into TEF mode. In
 * eclipse mode this document does not present the TEF text structure, but a
 * normal StringBuffer based string content.
 * 
 * This document is a document per editor. This is completly wrong. It should be separated from
 * a concrete editor instance.
 */
public abstract class TEFDocument extends Document implements IModelProvider, ITemplateProvider {	
	private Changes changes = new Changes();	
	private IModel model = null;
	private Object resource = null;	
	private Template topLevelTemplate = null;
	
	private DocumentModel documentModel = null;
	
	private boolean empty = true;	
	private boolean changed = false;
	
	@Override
	public final void replace(int pos, int length, String text) throws BadLocationException {
		eclipseReplace(pos, length, text);
	}
	
	/**
	 * This is a new method, not known to the eclipse platform. It is used to handle document changes that
	 * were not directly triggered by the user. These changes come from the according DocumentText.
	 * We say this are changes "from below", in contradiction to changes "from above" {@link #replace(int, int, String)}.
	 */
	public final void doReplace(int pos, int length, String text) throws BadLocationException {
		super.replace(pos, length, text);
	}
	
	private synchronized final void eclipseReplace(int pos, int length, String text) throws BadLocationException {
		changes.addChange(new Change(pos, length, text));
		changed = true;
		super.replace(pos, length, text);
	}
	
	public Changes getChanges() {
		return this.changes;
	}
	
	public IDocumentModelProvider getModelProvider() {
		final IModel model = this.model;
		return new IDocumentModelProvider() {
			public IModel getModel() {
				return model;
			}			
		};
	}

	public IModel getModel() {
		return model;
	}

	public IModelElement getTopLevelElement() {
		return (IModelElement)getModel().getOutermostCompositesOfEditedResource().iterator().next();
	}

	public void setInitialModelContent(IModel model, Object resource) {
		this.model = model;
		this.resource = resource;
		this.changed = false;
		TreeRepresentation treeRepresentation = (TreeRepresentation)getTopLevelTemplate().getAdapter(ITreeRepresentationProvider.class).createTreeRepresentation(null, 
				null, getTopLevelElement(), true);
		if (empty) {
			try {
				doReplace(0, get().length(), treeRepresentation.getContent());
			} catch (BadLocationException ex) {
				throw new RuntimeException(ex);
			}
			empty = false;
		}	
		this.documentModel = new DocumentModel(getTopLevelElement(), treeRepresentation);
	}
	
	synchronized public void setModelContent(DocumentModel documentModel) {
		this.documentModel.dispose();		
		model.replaceOutermostComposite(resource, getModelContent(), documentModel.getTopLevelModelElement());
		this.documentModel = documentModel;
	}
	
	public IModelElement getModelContent() {
		return getTopLevelElement();
	}
	
	public TreeRepresentation getModelRepresentation() {
		return documentModel.getTreeRepresentation();
	}
	
	public Object getModelResource() {
		return resource;
	}
	
	protected abstract Template createTopLevelTemplate(IAnnotationModelProvider annotationProvider);
	

	public final Template getTopLevelTemplate() {
		if (topLevelTemplate == null) {
			topLevelTemplate = createTopLevelTemplate(null);
		}
		return topLevelTemplate;
	}
	
	public boolean needsReconciling() {
		return changed;
	}
	
	synchronized public Map<Annotation, Position> createNewOccurenceAnnotations(ISourceViewer viewer) {										
		int cursorPosition = viewer.getTextWidget().getCaretOffset();			
		IChildSelector<TreeRepresentation> selector = new IndexTreeRepresentationSelector(cursorPosition, 0);		
		TreeRepresentation selectedTreeNode = TreeIterator.select(selector, getModelRepresentation());				
		
		IModelElement modelElement = null;
		ITreeContents selectedTreeContents = selectedTreeNode.getElement();
		if (selectedTreeContents instanceof ModelTreeContents) {
			modelElement = ((ModelTreeContents)selectedTreeContents).getModelElement();
		}
		
		Map<Annotation, Position> result = new HashMap<Annotation, Position>();
		if (modelElement == null) {			
			return result;
		}  else {			
			Collection<Position> positions =  documentModel.getOccurences(modelElement);					
			for(Position position: positions) {
				result.put(new Annotation("testeditor.occurencesmarker", false, "A OCCURENCE"), position);
			}
			return result;
		}
	}	
			
}
