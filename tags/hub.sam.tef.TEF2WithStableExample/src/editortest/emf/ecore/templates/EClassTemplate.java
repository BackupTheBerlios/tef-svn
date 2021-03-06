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
import hub.sam.tef.syntax.BlockLayout;
import hub.sam.tef.templates.ChoiceTemplate;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.ReferenceTemplate;
import hub.sam.tef.templates.SequenceTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.templates.TerminalTemplate;
import hub.sam.tef.templates.ValueTemplate;
import hub.sam.tef.templates.WhitespaceTemplate;

public class EClassTemplate extends EModelElementTemplate {

	public EClassTemplate(Template template) {
		super(template, template.getModel().getMetaElement("EClass")); 
	}

	@Override
	public Template[] getContentsTemplates() {
		return new Template[] {
				new SequenceTemplate<IModelElement>(this, "eStructuralFeatures", ";", true) {
					@Override					
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return new ChoiceTemplate(this, getModel().getMetaElement("EStructuralFeature")) {
							@Override
							public ValueTemplate<IModelElement>[] createAlternativeTemplates() {
								return new ValueTemplate[] {
										new EReferenceTemplate(this),
										new EAttributeTemplate(this)
								};
							}							
						};						
					}
					@Override
					protected WhitespaceTemplate createSeparatorWhitespace() {
						return new WhitespaceTemplate(this, BlockLayout.STATEMENT);
					}					
				},
				new SequenceTemplate<IModelElement>(this, "eOperations", ";", true) {
					@Override					
					protected ValueTemplate<IModelElement> createValueTemplate() {
						return 	new EOperationTemplate(this);									
					}
				}
		};
	}

	@Override
	String getElementKeyWord() {
		return "class";
	}

	@Override
	Template[] getReferenceTemplates() {
		return new Template[] {
			new TerminalTemplate(this, "superclass", TerminalTemplate.KEY_WORD_HIGHLIGHT),
			new TerminalTemplate(this, ":["),
			new SequenceTemplate<IModelElement>(this, "eSuperTypes", ",", false) {
				@Override
				protected ValueTemplate<IModelElement>createValueTemplate() {
					return new ReferenceTemplate(this, getModel().getMetaElement("EClass")) {
						@Override
						protected ElementTemplate getElementTemplate() {
							return new EIdentifierTemplate(this);
						}							
					};
				}				
			},
			new TerminalTemplate(this, "]"),
		};
	}

	@Override
	Template[] getFlags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean showAnnotations() {
		return false;
	}			
}
