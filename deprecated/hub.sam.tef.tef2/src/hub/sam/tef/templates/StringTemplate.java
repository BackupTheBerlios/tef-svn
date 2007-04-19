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
import hub.sam.tef.syntax.ISyntaxProvider;

public class StringTemplate extends PrimitiveValueTemplate<String>{

	private final String fPattern;
	
	public StringTemplate(Template template) {
		super(template, template.getModelProvider().getModel().getType(String.class));	
		fPattern = null;
	}
	
	public StringTemplate(Template template, final String pattern) {
		super(template, template.getModelProvider().getModel().getType(String.class));
		fPattern = pattern;
	}

	protected boolean verifyValue(String value) {
		if (fPattern == null) {
			return true;
		} else {
			return value.matches(fPattern);
		}
	}

	@Override
	public ICommand getCommandToCreateADefaultValue(IModelElement owner, String property, boolean composite) {		
		return getModel().getCommandFactory().set(owner, property, "name1");
	}	
	
	@Override
	protected Object getObjectValueFromStringValue(String value) {
		return value;
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
			return "`identifier`";
		}

		public String[][] getRules() {
			return new String[][]{};
		}					
	}
	
}
