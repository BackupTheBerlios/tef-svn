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

import hub.sam.tef.parse.ISemanticProvider;
import hub.sam.tef.templates.adaptors.ISyntaxProvider;
import hub.sam.tef.templates.adaptors.IASTProvider;

/**
 * This is a base class for all Properties that have a collection of value to
 * represent.
 */
public abstract class CollectionTemplate<ElementModelType> extends PropertyTemplate<ElementModelType> {

	protected final String fSeparator;
	protected final boolean fSeparateLast;
	private final boolean fIsComposite;
	
	public CollectionTemplate(ElementTemplate elementTemplate, String property, String separator, boolean separateLast) {
		super(elementTemplate, property);
		fSeparator = separator;
		fSeparateLast = separateLast;
		fIsComposite = false;
	}
	
	public CollectionTemplate(ElementTemplate elementTemplate, String property, String separator, boolean separateLast, boolean isComposite) {
		super(elementTemplate, property);
		fSeparator = separator;
		fSeparateLast = separateLast;
		fIsComposite = isComposite;		
	}	
		
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ISyntaxProvider.class == adapter) {
			return (T)new CollectionTemplateSemantics(this);
		} else if (IASTProvider.class == adapter) {
			return (T)new CollectionTemplateSemantics(this);
		} else if (ISemanticProvider.class == adapter) {
			return (T)new CollectionTemplateSemantics(this);
		} else {
			return super.getAdapter(adapter);			
		}
	}
}
