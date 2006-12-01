package hub.sam.tef.controllers;

import hub.sam.tef.views.Text;

public class HandleEventVisitor extends AbstractTextEventBasedVisitor<ITextEventListener> {	
	
	public HandleEventVisitor(TextEvent event) {
		super(event);
	}

	@Override
	protected boolean dealWithEvent(TextEvent event, Text visitedText, ITextEventListener handler) {
		if (handler.verifyEvent(visitedText, event)) {
			return handler.handleEvent(visitedText, event);
		} else {
			return false;
		}
	}

	@Override
	protected Iterable<ITextEventListener> getHandler(Text visitedText) {
		return visitedText.getAllElements(ITextEventListener.class);
	}	
	
}
