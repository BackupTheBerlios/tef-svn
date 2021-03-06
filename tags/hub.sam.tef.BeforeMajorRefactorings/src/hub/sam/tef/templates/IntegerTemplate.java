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

import hub.sam.tef.controllers.CursorMovementStrategy;
import hub.sam.tef.controllers.ITextEventListener;
import hub.sam.tef.controllers.TextEvent;
import hub.sam.tef.views.ChangeText;
import hub.sam.tef.views.Text;

public class IntegerTemplate extends PrimitiveValueTemplate<Integer>{
	
	private final Integer fDefaultValue;
	
	public IntegerTemplate(Template template, int defaultValue) {
		super(template, template.getModel().getType(Integer.class));
		fDefaultValue = defaultValue;
	}
	

	@Override
	public Text createView(Integer model, final IValueChangeListener<Integer> changeListener) {
		ChangeText result = new ChangeText();
		if (model == null) {
			model = fDefaultValue;
		}
		result.setText(model.toString());
		result.addElement(ITextEventListener.class, new ITextEventListener() {
			public boolean handleEvent(Text text, TextEvent event) {
				if (!((ChangeText)text).isNull()) {
					StringBuffer value = new StringBuffer(text.getContent());
					value.replace(event.getBegin(), event.getEnd(), event.getText());					
					changeListener.valueChanges(new Integer(value.toString()));
				} else {
					changeListener.valueChanges(new Integer(event.getText()));
				}							
				return true;
			}
			public boolean verifyEvent(Text text, TextEvent event) {
				if (((ChangeText)text).isNull()) {
					return verifyValue(event.getText());
				} else {
					StringBuffer value = new StringBuffer(text.getContent());				
					value.replace(event.getBegin(), event.getEnd(), event.getText());
					return verifyValue(value.toString());
				}
			}			
		});	
		result.setElement(CursorMovementStrategy.class, new CursorMovementStrategy(true, true));
		return result;
	}
	
	protected boolean verifyValue(String value) {		
		return value.matches("-?[0-9]+");		
	}

	@Override
	public void updateView(Text view, Integer value) {
		((ChangeText)view).setText(value.toString());
	}


	@Override
	public String getNonTerminal() {
		return "`integer`";
	}
	
}
