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
package hub.sam.tef.templates;

import hub.sam.tef.controllers.Proposal;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.treerepresentation.ISyntaxProvider;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.treerepresentation.ModelTreeContents;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.TreeRepresentation;
import hub.sam.tef.treerepresentation.TreeRepresentationLeaf;

import java.util.List;
import java.util.Vector;


/**
 * This is a ValueTemplate that represents different types of 
 * values.
 */
public abstract class ChoiceTemplate extends ValueTemplate<IModelElement> {
	
	private final ValueTemplate<IModelElement>[] fAlternativeTemplates;
	private final IMetaModelElement fMetaModelElement;
	
	public ChoiceTemplate(Template template, IMetaModelElement metaModelElement) {
		super(template, metaModelElement);
		this.fMetaModelElement = metaModelElement;
		this.fAlternativeTemplates = createAlternativeTemplates();
	}

	/**
	 * @return A set of value templates. These are the templates for all
	 *         possible values for this template.
	 */
	public abstract ValueTemplate<IModelElement>[] createAlternativeTemplates();
	
	@Override
	public Template[] getNestedTemplates() {
		return fAlternativeTemplates;
	}
	
	/**
	 * Returns true for those meta model elements that this element template
	 * provides representations for.
	 */
	@Override
	public boolean isTemplateFor(IModelElement model) {
		if (model instanceof IModelElement) {
			return fMetaModelElement.isMetaModelFor((IModelElement)model);
		} else {
			return super.isTemplateFor(model);
		}
	}	
				
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter) {
			return (T)new ModelUpdater(this);
		} else if (ITreeRepresentationProvider.class == adapter) {			
			return (T)new TreeRepresentationProvider();
		} else if (ISemanticProvider.class == adapter) {
			return (T)new SemanticProvider();
		} else {
			return super.getAdapter(adapter);
		}
	}

	class ModelUpdater extends ValueTemplateSemantics implements ISyntaxProvider {
				
		protected ModelUpdater(ValueTemplate template) {
			super(template);		
		}

		public String[][] getRules() {
			String[][] result = new String[fAlternativeTemplates.length][];
			int i = 0;
			for(Template choice: fAlternativeTemplates) {
				result[i++] = new String[] { getNonTerminal(), choice.getAdapter(ISyntaxProvider.class).getNonTerminal() };
			}
			return result;					
		}			
	}
	
	class TreeRepresentationProvider implements ITreeRepresentationProvider {
		public TreeRepresentationLeaf createTreeRepresentation(IModelElement owner, String notused, Object model, boolean isComposite) {			
			ModelTreeContents contents = new ModelTreeContents(ChoiceTemplate.this, (IModelElement)model);
			TreeRepresentation treeRepresentation = new TreeRepresentation(contents);

			for (ValueTemplate alternative: fAlternativeTemplates) {
				if (alternative.isTemplateFor(model)) {
					treeRepresentation.addContent(alternative.getAdapter(ITreeRepresentationProvider.class).
							createTreeRepresentation(owner, notused, model, true));
																			
					return treeRepresentation;
				}
			}
			throw new RuntimeException("assert");
		}

		public Object createCompositeModel(IModelElement owner, String property, TreeRepresentationLeaf tree, boolean isComposite) {			
			TreeRepresentationLeaf childTree = tree.getChildNodes().get(0);			
			IModelElement result = (IModelElement) childTree.getElement().getTemplate().getAdapter(ITreeRepresentationProvider.class).
					createCompositeModel(owner, property, childTree, isComposite);
			tree.setElement(new ModelTreeContents(tree.getElement().getTemplate(), result));
			return result;
		}

		public Object createReferenceModel(IModelElement owner, String property, TreeRepresentationLeaf tree, boolean isComposite, SemanticsContext context) {
			TreeRepresentationLeaf childTree = tree.getChildNodes().get(0);			
			IModelElement result = (IModelElement) childTree.getElement().getTemplate().getAdapter(ITreeRepresentationProvider.class).
					createReferenceModel(owner, property, childTree, isComposite, context);			
			return result;
		}				
	}
		
	class SemanticProvider implements ISemanticProvider {
		
		public void check(TreeRepresentation representation, SemanticsContext context) {		
			TreeRepresentation nextNode = ((TreeRepresentation)representation).getChildNodes().get(0);
			nextNode.getElement().getTemplate().getAdapter(ISemanticProvider.class).
					check(nextNode, context);
		}		
	}
}
