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
package editortest.emf.ecore.templates;

import hub.sam.tef.IDocumentModelProvider;
import hub.sam.tef.models.IMetaModelElement;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.reconciliation.syntax.BlockLayout;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.LayoutElementTemplate;
import hub.sam.tef.templates.LayoutManager;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.WhitespaceTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

public abstract class EModelElementTemplate extends ElementTemplate {

	public EModelElementTemplate(IDocumentModelProvider modelProvider, IMetaModelElement metaModel) {
		super(modelProvider, metaModel);
	}

	public EModelElementTemplate(Template template, IMetaModelElement metaModel) {
		super(template, metaModel);
	}

	protected Template[] getAnnotationTemplates() {
		return new Template[] {
				new WhitespaceTemplate(this, BlockLayout.INDENT),
			new TerminalTemplate(this, "annotations"),
			new WhitespaceTemplate(this, BlockLayout.BEGIN_BLOCK),
			new TerminalTemplate(this, ":[\n"),
			new SequenceTemplate<IModelElement>(this, "eAnnotations", null, true) {
				@Override
				protected ValueTemplate<IModelElement> createValueTemplate() {
					return new EAnnotationTemplate(this);
				}
			},
			new WhitespaceTemplate(this, BlockLayout.END_BLOCK),
			new WhitespaceTemplate(this, BlockLayout.INDENT),
			new TerminalTemplate(this, "]")
		};
	}
	
	public Template[] getNameTemplates(boolean withComma) {
		return (withComma) ?
			new Template[] {
				new SingleValueTemplate<String>(this, "name") {
					@Override
					protected ValueTemplate<String> createValueTemplate() {
						return new IdentifierValueTemplate(this);
					}					
				},			
				new TerminalTemplate(this, ","),
				new WhitespaceTemplate(this, BlockLayout.SPACE)
			} :
			new Template[] {
				new SingleValueTemplate<String>(this, "name") {
					@Override
					protected ValueTemplate<String> createValueTemplate() {
						return new IdentifierValueTemplate(this);
					}					
				},
				new WhitespaceTemplate(this, BlockLayout.SPACE)
			};
	}
	
	@Override
	public Template[] createTemplates() {
		Collection<Template> templates = new Vector<Template>();
		templates.add(new WhitespaceTemplate(this, BlockLayout.INDENT));
		Template[] flags = getFlags();
		if (flags != null) {
			templates.add(new WhitespaceTemplate(this, BlockLayout.SPACE));
			templates.addAll(Arrays.asList(flags));
		}
		templates.add(new TerminalTemplate(this, getElementKeyWord(), 
				TerminalTemplate.KEY_WORD_HIGHLIGHT));
		templates.add(new WhitespaceTemplate(this, BlockLayout.SPACE));
		Template[] references = getReferenceTemplates();
		templates.addAll(Arrays.asList(getNameTemplates(references != null)));		
		if (references != null) {
			templates.addAll((Arrays.asList(references)));		
			templates.add(new WhitespaceTemplate(this, BlockLayout.SPACE));
		}
			
		Template[] contents = getContentsTemplates();			
		if (showAnnotations()) {			
			templates.add(new WhitespaceTemplate(this, BlockLayout.BEGIN_BLOCK));
			templates.addAll(Arrays.asList(getAnnotationTemplates()));
			templates.add(new WhitespaceTemplate(this, BlockLayout.END_BLOCK));
		}
	    		
		if (contents != null) {			
			templates.add(new TerminalTemplate(this, "{"));
			templates.add(new WhitespaceTemplate(this, BlockLayout.BEGIN_BLOCK));
			templates.addAll(Arrays.asList(contents));		    
			templates.add(new WhitespaceTemplate(this, BlockLayout.END_BLOCK));
			templates.add(new WhitespaceTemplate(this, BlockLayout.INDENT));
		    templates.add(new TerminalTemplate(this, "}"));			
		} 			
		return templates.toArray(new Template[]{});
	}
	
	abstract Template[] getFlags();	
	abstract String getElementKeyWord();
	abstract Template[] getReferenceTemplates();
	abstract Template[] getContentsTemplates();
	
	protected boolean showAnnotations() {
		return false;
	}
	
	protected static Template[] concat(Template[][] ts) {		
		Collection<Template> result = new Vector<Template>();
		for(Template[] t: ts) {
			for(Template s: t) {
				result.add(s);
			}
		}
		return result.toArray(new Template[]{});
	}

	@Override
	protected boolean isIdentifierProperty(String property) {
		return property.equals("name");
	}
	
	
}
