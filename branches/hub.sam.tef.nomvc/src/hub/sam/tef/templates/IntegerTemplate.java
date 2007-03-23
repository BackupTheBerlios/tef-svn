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
import hub.sam.tef.treerepresentation.ISyntaxProvider;

public class IntegerTemplate extends PrimitiveValueTemplate<Integer>{
	
	private final Integer fDefaultValue;
	
	public IntegerTemplate(Template template, int defaultValue) {
		super(template, template.getModelProvider().getModel().getType(Integer.class));
		fDefaultValue = defaultValue;
	}
	
	protected boolean verifyValue(String value) {		
		return value.matches("-?[0-9]+");		
	}

	@Override
	public ICommand getCommandToCreateADefaultValue(IModelElement owner, String property, boolean composite) {
		return getModel().getCommandFactory().set(owner, property, -1);
	}			
	
	@Override
	protected Object getObjectValueFromStringValue(String value) {
		return new Integer(value);
	}


	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter) {
			return (T)new SyntaxProvider();
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	class SyntaxProvider implements ISyntaxProvider {	
		public String getNonTerminal() {
			return "`integer`";
		}

		public String[][] getRules() {
			return new String[][] {};
		}
	}
}
