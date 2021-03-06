package hub.sam.tef.parse;

import fri.patterns.interpreter.parsergenerator.Token.Address;
import fri.patterns.interpreter.parsergenerator.Token.Range;
import fri.patterns.interpreter.parsergenerator.syntax.Rule;
import hub.sam.tef.templates.ElementTemplate;
import hub.sam.tef.templates.PropertyTemplate;
import hub.sam.tef.templates.Template;
import hub.sam.tef.views.CompoundText;
import hub.sam.tef.views.DocumentText;
import hub.sam.tef.views.ITextVisitor;
import hub.sam.tef.views.Text;
import hub.sam.util.trees.AbstractChildTree;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import sun.rmi.runtime.GetThreadPoolAction;

public class TextBasedAST extends AST<TextBasedAST, Text> {
	
	/**
	 * Recursivly builds an TextBasedAST from a DocumentText and the templates in the texts.
	 */
	public static TextBasedAST createASTTree(DocumentText document) {
		CompoundText text = document;
		while (!textNodeIsASTNode(text) && text.getTexts().get(0) instanceof CompoundText) {
			text = (CompoundText)text.getTexts().get(0);
		}
		if (!textNodeIsASTNode(text)) {
			return null;
		} else {
			return createASTTreeFromText(text);
		}		
	}
	
	private static TextBasedAST createASTTreeFromText(Text text) {
		TextBasedAST result = new TextBasedAST(text);		
		if (text instanceof CompoundText) {
			FindChildren childrenFinder = new FindChildren(text);
			((CompoundText)text).process(childrenFinder, 0);			
			
			for (Text child: childrenFinder.children) {				
				TextBasedAST childNode = createASTTreeFromText(child);
				if (Rule.isTerminal(childNode.getSymbol())) {
					if (childNode.getTemplate() instanceof PropertyTemplate) {
						result.putStringValueForProperty(((PropertyTemplate)childNode.getTemplate()).getProperty(),
								child.getContent());
					} // otherwise it must be a terminal template
				} else {
					result.addChild(createASTTreeFromText(child));
				}
			}
		}
		return result;
	}
	
	private boolean reused = false;
	
	private TextBasedAST(Text text) {
		super(text, text.getElement(Template.class).getNonTerminal());
		Template template = text.getElement(Template.class);		
	}
	
	/**
	 * Collect texts starting from a comount text that would make suitable
	 * AST child nodes of the AST node for the compound text.
	 */
	static class FindChildren implements ITextVisitor {
		private final List<Text> children = new Vector<Text>();
		private final Text fText;			
		
		public FindChildren(final Text text) {
			super();
			fText = text;
		}

		public Iterable<Text> decentInto(CompoundText text) {
			if (textNodeIsASTNode(text) && text != fText) {
				return new Vector<Text>();
			} else {
				return text.getTexts();
			}
		}

		public void visitText(Text visitedText, int atOffset) {
			if (textNodeIsASTNode(visitedText) && visitedText != fText) {
				children.add(visitedText);
			}
 		}
		
		public void visitCompoundText(CompoundText visitedText, int atOffset) {
			visitText(visitedText, atOffset);
		}
	}
	
	public String getSymbol() {
		return getTemplate().getNonTerminal();
	}
	
	private static final boolean textNodeIsASTNode(Text text) {
		return text.getElement(Template.class) != null;
	}
	
	
	public final Template getTemplate() {
		return getElement().getElement(Template.class);
	}

	@Override
	public String toString() {		
		return "#(" + getSymbol() + super.toString() + ")";
	}
	
	public Range getRange() {
		return new Range(new Address(-1,-1, getElement().getAbsolutOffset(0)), 
				new Address(-1,-1, getElement().getAbsolutOffset(getElement().getLength())));
	}

	public List<String> getChildSymbols() {
		List<String> result = new Vector<String>();
		for (TextBasedAST child: getChildNodes()) {
			if (!Rule.isTerminal(child.getSymbol())) {
				result.add(child.getSymbol());
			}
		}
		return result;
	}	
	
	protected void setReused() {
		this.reused = true;
	}

	public boolean isReused() {
		return reused;
	}
}
