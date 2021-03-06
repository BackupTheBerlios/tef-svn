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

import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.models.extensions.InternalModelElement;
import hub.sam.tef.syntax.ISemanticProvider;
import hub.sam.tef.templates.adaptors.IDocumentModelProvider;
import hub.sam.tef.templates.adaptors.IElementSyntaxProvider;
import hub.sam.tef.templates.adaptors.IPresentationOptionsProvider;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.templates.layout.WhitespaceTemplate;
import hub.sam.tef.treerepresentation.ASTElementNode;

/**
 * A special ValueTemplate used for elements, whereby elements are container for
 * Properties. An ElementTemplate provides the representations for whole model
 * elements using property and terminal templates as children. It defines a node in 
 * a hierarchy of templates.
 */
public abstract class ElementTemplate extends ValueTemplate<IModelElement> {
	
	private final IMetaModelElement fMetaModel;
	private Template[] fTemplates;
	
	public ElementTemplate(Template template, IMetaModelElement metaModel) {
		super(template, metaModel);		
		fMetaModel = metaModel;
	}	

	public ElementTemplate(IDocumentModelProvider modelProvider, IMetaModelElement metaModel) {
		super(modelProvider, metaModel);
		fMetaModel = metaModel;
	}
	
	/**
	 * Creates all child templates. This can be Terminal or ValueTemplates.
	 */
	public abstract Template[] createTemplates();
		
	@Override
	public Template[] getNestedTemplates() {
		return getTemplates();
	}

	private final Template[] getTemplates() {
		if (fTemplates == null) {
			fTemplates = createTemplates();
		}
		return fTemplates;
	}

	/**
	 * @return The meta element that describes the elements that this template
	 *         provides representations for.
	 */
	protected IMetaModelElement getMetaElement() {
		return this.fMetaModel;
	}
	
	/**
	 * Returns true for those meta model elements that this element template
	 * provides representations for.
	 */
	public boolean isTemplateFor(IModelElement model) {		
		return getMetaElement().isMetaModelFor(model);
	}
	
	/**
	 * When a view for this element is created those property subviews that
	 * represent properties that this method yields will be registered as
	 * occurences of the element represented by this template..
	 * 
	 * @param property
	 *            The property.
	 * @return True when the representation of this property should be marked as
	 *         a occurence of the element represented by the containing element
	 *         template.
	 */
	protected boolean isIdentifierProperty(String property) {
		return false;
	}

	@Override
	public ICommand getCommandToCreateADefaultValue(IModelElement owner, String property, boolean composite) {	
		return null;
	}
	
	public IModelElement createMockObject() {
		InternalModelElement mock = new InternalModelElement(getMetaElement());
		for (Template template: getTemplates()) {
			if (template instanceof SingleValueTemplate) {
				((ValueTemplate)template.getNestedTemplates()[0]).getCommandToCreateADefaultValue(
						mock, ((PropertyTemplate)template).getProperty(), false).execute();
			}
		}
		return mock;
	}
	
	protected String getAlternativeSymbol() {
		return null;
	}
	
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter || IElementSyntaxProvider.class == adapter) {
			return (T) new ElementTemplateSemantics(this);
		} else if (IASTProvider.class == adapter) {
			return (T) new ElementTemplateSemantics(this);
		} else if (ISemanticProvider.class == adapter) {
			return (T) new ElementTemplateSemantics(this);
		} else if (IPresentationOptionsProvider.class == adapter) {
			return (T) new IPresentationOptionsProvider() {
				public boolean markOccurences(ASTElementNode treeRepresentation, int localOffset) {
					return treeRepresentation.getNode("name") == treeRepresentation.getNode(localOffset);
				}				
			};
		} else {
			return null;
		}
	}

	@Override
	protected Object getId() {
		if (getAlternativeSymbol() != null) {			
			return getAlternativeSymbol();
		} else if (getAdapter(ISyntaxProvider.class) != null) {
			return getAdapter(ISyntaxProvider.class).getNonTerminal();
		} else {
			return getAlternativeSymbol();
		}
	}	
}
