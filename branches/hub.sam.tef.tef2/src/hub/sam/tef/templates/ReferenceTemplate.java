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

import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.templates.layout.AbstractLayoutManager;
import hub.sam.tef.treerepresentation.ModelASTElement;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.ASTNode;


public abstract class ReferenceTemplate extends ValueTemplate<IModelElement> {		

	private final IMetaModelElement fTypeModel;
	private final ElementTemplate fIdentifierTemplate;
	private final String fSymbol;
	
	public ReferenceTemplate(Template template, IMetaModelElement typeModel, String symbol) {
		super(template, typeModel);
		this.fTypeModel = typeModel;		
		this.fIdentifierTemplate = getElementTemplate();
		this.fSymbol = symbol;
	}
	
	public ReferenceTemplate(Template template, IMetaModelElement typeModel) {
		super(template, typeModel);
		this.fTypeModel = typeModel;		
		this.fIdentifierTemplate = getElementTemplate();
		this.fSymbol = null;
	}	
	
	public IMetaModelElement getTypeModel() {
		return fTypeModel;
	}
	
	protected abstract ElementTemplate getElementTemplate();
	
	@Override
	public Template[] getNestedTemplates() {
		return new Template[] { fIdentifierTemplate };
	}
	
	@Override
	public ICommand getCommandToCreateADefaultValue(final IModelElement owner, String property, boolean composite) {
		IModelElement mock = fIdentifierTemplate.createMockObject(); 		
		return getModel().getCommandFactory().set(owner, property, mock);
	}
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter) {
			return (T)new SyntaxProvider();
		} else if (IASTProvider.class == adapter) {
			return (T)new TreeRepresentationProvider();
		} else if (ISemanticProvider.class == adapter) {
			return (T)new SemanticProvider();
		} else{
			return super.getAdapter(adapter);
		}
	}
	
	class SyntaxProvider extends ValueTemplateSemantics implements ISyntaxProvider {					
		
		public SyntaxProvider() {
			super(ReferenceTemplate.this);		
		}
		
		public String getNonTerminal() {
			if (fSymbol == null) {
				return super.getNonTerminal() + "_ref";
			} else {
				return fSymbol;
			}
		}

		public String[][] getRules() {
			return new String[][] {{ getNonTerminal(), fIdentifierTemplate.getAdapter(ISyntaxProvider.class).getNonTerminal() }};
		}			
	}
	
	class TreeRepresentationProvider implements IASTProvider {
		public ASTNode createTreeRepresentation(IModelElement owner, String notused, Object model, boolean isComposite, AbstractLayoutManager layout) {
			ModelASTElement contents = new ModelASTElement(ReferenceTemplate.this, (IModelElement)model);
			ASTElementNode treeRepresentation = new ASTElementNode(contents);
			
			treeRepresentation.addNodeObject(fIdentifierTemplate.getAdapter(IASTProvider.class).
					createTreeRepresentation(owner, notused, model, false, layout));						
			
			return treeRepresentation;
		}

		public Object createCompositeModel(IModelElement owner, String property, ASTNode tree, boolean isComposite) {				
			IModelElement result =  (IModelElement)fIdentifierTemplate.getAdapter(IASTProvider.class).
					createCompositeModel(owner, property, ((ASTElementNode)tree).getChildNodes().get(0), false);			
			tree.setElement(new ModelASTElement(ReferenceTemplate.this, result));
			return result;
		}

		public Object createReferenceModel(IModelElement owner, String property, ASTNode tree, boolean isComposite, SemanticsContext context) {
			IModelElement result = (IModelElement)fIdentifierTemplate.getAdapter(IASTProvider.class).
					createReferenceModel(owner, property, ((ASTElementNode)tree).getChildNodes().get(0), false, context);						
			return result;
		}			
		
	}
	
	class SemanticProvider implements ISemanticProvider {		
		public void check(ASTElementNode representation, SemanticsContext context) {			
			fIdentifierTemplate.getAdapter(ISemanticProvider.class).check(representation.getChildNodes().get(0), context);
		}		
	}
	
	@Override
	protected Object getId() {
		if (getAdapter(ISyntaxProvider.class) != null) {
			return getAdapter(ISyntaxProvider.class).getNonTerminal();
		} else {
			return super.getId();
		}
	}	
}
