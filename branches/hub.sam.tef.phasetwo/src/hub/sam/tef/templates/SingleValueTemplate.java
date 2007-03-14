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

import com.sun.corba.se.impl.io.FVDCodeBaseImpl;

import sun.rmi.runtime.GetThreadPoolAction;
import hub.sam.tef.controllers.AbstractRequestHandler;
import hub.sam.tef.controllers.Proposal;
import hub.sam.tef.controllers.RetifyCursorPositionModelEventListener;
import hub.sam.tef.liveparser.SymbolASTNode;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.parse.IASTBasedModelUpdater;
import hub.sam.tef.parse.ISyntaxProvider;
import hub.sam.tef.parse.ModelUpdateConfiguration;
import hub.sam.tef.parse.TextBasedAST;
import hub.sam.tef.treerepresentation.ITreeRepresentationProvider;
import hub.sam.tef.views.Text;

public abstract class SingleValueTemplate<ModelType> extends PropertyTemplate<ModelType> {
	
	public SingleValueTemplate(ElementTemplate elementTemplate, String property) {
		super(elementTemplate, property);
	}
	
	class ModelEventListener extends RetifyCursorPositionModelEventListener {
		private final IModelElement fModel;
		private final Text valueView;
		
		public ModelEventListener(final IModelElement model, final Text valueView) {
			super(model, valueView, getCursorPostionProvider());
			fModel = model;
			this.valueView = valueView;
		}

		@Override
		public void propertyChanged(Object element, String property) {
			if (property == getProperty()) {
				getValueTemplate().updateView(valueView, (ModelType)fModel.getValue(property));				
				setNewCursorPosition(valueView, 0);				
				valueView.update();				
			}			
		}	
	}
	
	class ValueChangeListener extends AbstractRequestHandler<ModelType> 
			implements IValueChangeListener<ModelType> {
		
		public ValueChangeListener(final IModelElement owner, String property) {
			super(owner, property, null);			
		}
		
		public void valueChanges(ModelType newValue) {
			getModelProvider().getModel().getCommandFactory().set(getOwner(), getProperty(), newValue).execute();
		}

		public void valueChanges(SymbolASTNode node) {
			node.createModelElements(getModelProvider().getModel(), getOwner(), getProperty());
		}

		public void newValue(Proposal proposal, ValueTemplate<ModelType> template) {
			template.getCommandForProposal(proposal, getOwner(), getProperty(), 0).execute();			
		}

		public void removeValue() {
			getModelProvider().getModel().getCommandFactory().delete(getOwner().getValue(getProperty())).execute();
		}						
	}
	
	@Override
	public Text createView(final IModelElement model) {
		ModelType value = (ModelType)model.getValue(getProperty());
		if (value == null) {
			getValueTemplate().getCommandToCreateADefaultValue(model, getProperty(), false).execute();
			value = (ModelType)model.getValue(getProperty());
		}
		final Text result = getValueTemplate().getView(value, new ValueChangeListener(model, getProperty()));
		new ModelEventListener(model, result); // activates itself once the view is shown
		return result;
	}
	
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IASTBasedModelUpdater.class == adapter || ISyntaxProvider.class == adapter) {
			return (T)new ModelUpdater();
		} else if (ITreeRepresentationProvider.class == adapter) {
			return (T)new TreeRepresentationProvider();
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	class TreeRepresentationProvider implements ITreeRepresentationProvider {
		public Object createTreeRepresentation(String property, Object model) {
			return getValueTemplate().getAdapter(ITreeRepresentationProvider.class).
					createTreeRepresentation(null, ((IModelElement)model).getValue(property));			
		}		
	}

	class ModelUpdater implements IASTBasedModelUpdater, ISyntaxProvider {	
		public void executeModelUpdate(ModelUpdateConfiguration configuration) {
			getValueTemplate().getAdapter(IASTBasedModelUpdater.class).
					executeModelUpdate(configuration);
		}

		public TextBasedAST createAST(TextBasedAST parent,  IModelElement model, Text text) {
			System.out.println("$$" + getProperty());
			getValueTemplate().getAdapter(ISyntaxProvider.class).createAST(parent, model, text);
			return null;
		}

		public String getNonTerminal() {
			return getValueTemplate().getAdapter(ISyntaxProvider.class).getNonTerminal();
		}

		public String[][] getRules() {
			return new String[][] {};
		}
		
	}
		
}
