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

import hub.sam.tef.controllers.AbstractRequestHandler;
import hub.sam.tef.controllers.IProposalHandler;
import hub.sam.tef.controllers.Proposal;
import hub.sam.tef.models.ICollection;
import hub.sam.tef.models.ICommand;
import hub.sam.tef.models.IModelElement;
import hub.sam.tef.models.ISequence;
import hub.sam.tef.views.Text;

import java.util.List;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public abstract class SequenceTemplate<ElementModelType> extends CollectionTemplate<ElementModelType> {
	
	class SeedTextEventListener extends AbstractRequestHandler<ElementModelType> 
			implements IProposalHandler {	
		private final ISequence fModel;
		private final int fPosition;
		private final Text fCollectionText;		
		
		public SeedTextEventListener(final IModelElement owner, String property, 
				final ISequence model, int position, Text collectionText) {
			super(owner, property, null);
			fModel = model;
			fPosition = position;
			fCollectionText = collectionText;
		}

		public List<Proposal> getProposals(Text context, int offset) {
			return getValueTemplate().getProposals();
		}
		
		public boolean handleProposal(Text text, int offset, Proposal proposal) {
			if (getProposals(text, offset).contains(proposal)) {
				ICommand command = getValueTemplate().getCommandForProposal(
						proposal, getOwner(), getProperty(), fPosition);
				fCollectionText.setElement(CollectionCursorPositionMarker.class, new CollectionCursorPositionMarker(fPosition));
				command.execute();			
				return true;
			} else {
				return false;
			}
		}

		public ProposalKind getProposalKind() {
			return ProposalKind.insert;
		}		
	}
	
	public SequenceTemplate(ElementTemplate elementTemplate, String property, String separator, boolean separateLast) {
		super(elementTemplate, property, separator, separateLast);
	}
	
	public SequenceTemplate(ElementTemplate elementTemplate, String property, String separator, boolean separateLast, boolean isComposite) {
		super(elementTemplate, property, separator, separateLast, isComposite);
	}

	@Override
	protected SeedTextEventListener createSeedTextEventListenet(IModelElement owner, String property, 
			ICollection<ElementModelType> list, int position, Text collectionText) {
		return new SeedTextEventListener(owner, property, (ISequence<ElementModelType>) list, position, collectionText);
	}

	@Override
	public String getNonTerminal() {
		return super.getNonTerminal() + "_sequence";
	}

	@Override
	public String[][] getRules() {
		if (fSeparator != null) {
			if (fSeparateLast) {
				return new String[][] {
						new String[] { getNonTerminal(), "'" + fSeparator + "'" },
						new String[] { getNonTerminal(), getNonTerminal(), "'" + fSeparator + "'", getValueTemplate().getNonTerminal() } 
				};
			} else {
				return new String[][] {
						new String[] { getNonTerminal(), getValueTemplate().getNonTerminal() },
						new String[] { getNonTerminal(), getNonTerminal(), "'" + fSeparator + "'", getValueTemplate().getNonTerminal() } 
				};
			}
		} else {
			return new String[][] {
					new String[] { getNonTerminal(), getValueTemplate().getNonTerminal() },
					new String[] { getNonTerminal(), getNonTerminal(),  getValueTemplate().getNonTerminal() } 
			};
		}
	}		
}
