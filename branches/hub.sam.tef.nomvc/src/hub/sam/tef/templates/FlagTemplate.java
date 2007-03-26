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
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import hub.sam.tef.templates.adaptors.IASTProvider;
import hub.sam.tef.templates.layout.AbstractLayoutManager;
import hub.sam.tef.treerepresentation.ASTElementNode;
import hub.sam.tef.treerepresentation.ModelASTElement;
import hub.sam.tef.treerepresentation.PrimitiveTreeRepresentation;
import hub.sam.tef.treerepresentation.SemanticsContext;
import hub.sam.tef.treerepresentation.ASTNode;


public class FlagTemplate extends PrimitiveValueTemplate<Boolean> {
	
	private final String fFlagKeyword;

	public FlagTemplate(Template template, String flagKeywork) {
		super(template, template.getModelProvider().getModel().getType(Boolean.class));
		fFlagKeyword = flagKeywork;
	}	
	
	class ActualValue {
		boolean value;
	}

	@Override
	public ICommand getCommandToCreateADefaultValue(IModelElement owner, String property, boolean composite) {	
		return null;
	}		
	
	@Override
	protected Object getObjectValueFromStringValue(String value) {
		return fFlagKeyword.equals(value);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter) {
			return (T)new SyntaxProvider();
		} else if (IASTProvider.class == adapter) {
			return (T)new TreeRepresentationProvider();
		} else if (ISemanticProvider.class == adapter) {
			return (T)new SemanticProvider();
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	class SyntaxProvider implements ISyntaxProvider {		
		public String getNonTerminal() {
			return "Flag_" + fFlagKeyword;
		}

		public String[][] getRules() {		
			return new String[][] {
					new String[] { getNonTerminal() , "'" + fFlagKeyword + "'" },
					new String[] { getNonTerminal() },
			};
		}		
	}
	
	class TreeRepresentationProvider implements IASTProvider {
		public ASTNode createTreeRepresentation(IModelElement owner, String property, Object model, boolean isComposite, AbstractLayoutManager layout) {
			ASTElementNode result = new ASTElementNode(new ModelASTElement(FlagTemplate.this, null));			
			if ((Boolean)model) {
				result.addNodeObject(fFlagKeyword);							
			} 
			return result;
		}

		public Object createCompositeModel(IModelElement owner, String property, ASTNode tree, boolean isComposite) {			
			if (((ASTElementNode)tree).getChildNodes().size() == 1) {
				return FlagTemplate.super.getAdapter(IASTProvider.class).
						createCompositeModel(owner, property, ((ASTElementNode)tree).getChildNodes().get(0), true);
			}
			return null;
		}

		public Object createReferenceModel(IModelElement owner, String property, ASTNode tree, boolean isComposite, SemanticsContext context) {		
			return null;
		}				
	}
	
	class SemanticProvider implements ISemanticProvider {

		public void check(ASTElementNode representation, SemanticsContext context) {
			// empty	
		}		
	}
}
