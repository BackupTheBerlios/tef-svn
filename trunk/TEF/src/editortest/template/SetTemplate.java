package editortest.template;

import java.util.List;

import editortest.model.ICollection;
import editortest.text.CompoundText;
import editortest.text.Text;
import editortest.text.TextEvent;
import editortest.text.visitors.IProposalListener;
import editortest.text.visitors.ITextEventListener;
import editortest.text.visitors.Proposal;

public abstract class SetTemplate<ElementModelType> extends CollectionTemplate<ElementModelType> {
		
	class SeedTextEventListener implements IProposalListener {	
		private final ICollection fModel;
		
		public SeedTextEventListener(final ICollection model) {
			super();
			fModel = model;
		}

		public List<Proposal> getProposals(Text context, int offset) {
			return getElementTemplate().getProposals();
		}
		
		public boolean insertProposal(Text text, int offset, Proposal proposal) {
			if (getProposals(text, offset).contains(proposal)) {
				ElementModelType newElement = getElementTemplate().createModelFromProposal(proposal);
				fModel.add(newElement);
				return true;
			} else {
				return false;
			}
		}			
	}
	
	public SetTemplate(ElementTemplate elementTemplate, String property, String separator, boolean separateLast) {
		super(elementTemplate, property, separator, separateLast);
	}

	@Override
	protected IProposalListener createSeedTextEventListenet(ICollection<ElementModelType> list, int position) {
		return new SeedTextEventListener(list);
	}
}