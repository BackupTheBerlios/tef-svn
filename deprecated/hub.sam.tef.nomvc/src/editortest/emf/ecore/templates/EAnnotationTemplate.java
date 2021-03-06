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

import hub.sam.tef.models.IModelElement;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.SingleValueTemplate;
import hub.sam.tef.templates.StringTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;

public class EAnnotationTemplate extends EModelElementTemplate {

	public EAnnotationTemplate(Template template) {
		super(template, template.getModel().getMetaElement("EAnnotation"));
	}

	@Override
	public Template[] getNameTemplates(boolean withComma) {
		return new Template[] {			
			new TerminalTemplate(this, "source:"),
			new SingleValueTemplate<String>(this, "source") {
				@Override
				protected ValueTemplate<String> createValueTemplate() {
					return new StringTemplate(this);
				}					
			},						
		};
	}
	
	@Override
	Template[] getContentsTemplates() {	
		return null;
	}

	@Override
	String getElementKeyWord() {
		return "annotation";
	}

	@Override
	Template[] getReferenceTemplates() {
		return new Template[] {
			new TerminalTemplate(this, ","),	
			new TerminalTemplate(this, "references:["),
			new SequenceTemplate<IModelElement>(this, "references", ",", false) {
				@Override
				protected ValueTemplate<IModelElement> createValueTemplate() {
					return new ReferenceTemplate(this, getModel().getMetaElement("EModelElement")) {
						@Override
						protected ElementTemplate getElementTemplate() {
							return new EIdentifierTemplate(this);
						}							
					};
				}				
			},
			new TerminalTemplate(this, "]")
		};
	}

	@Override
	public boolean isTemplateFor(IModelElement model) {
		return model.getMetaElement().equals(getModel().getMetaElement("EAnnotation"));
	}

	@Override
	Template[] getFlags() {	
		return null;
	}
}
