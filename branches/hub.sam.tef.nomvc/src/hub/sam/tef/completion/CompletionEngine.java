package hub.sam.tef.completion;

import java.util.Collection;
import java.util.Vector;

import hub.sam.tef.parse.ParserInterface;
import hub.sam.tef.parse.UpdateTreeSemantic;
import hub.sam.tef.treerepresentation.ASTElementNode;

public class CompletionEngine {

	private final ParserInterface fParserInterface;
	
	public CompletionEngine(final ParserInterface parser) {
		super();
		fParserInterface = parser;
	}

	public Collection<CompletionContextInformation> collectCompletionsFromCompletionComputer(ICompletionComputer completion, CompletionContext context) {
		UpdateTreeSemantic semantic = new UpdateTreeSemantic(fParserInterface, context.getContent());		
		fParserInterface.parse(context.getContent(), semantic);
		CompletionParser parser = (CompletionParser)fParserInterface.getParser();
		
		String identifierPrefix = parser.removeLastIdentifier();
		context.setIdentifierPrefix(identifierPrefix);
		
		boolean completionOk = completion.reduceParseStack(parser);
		Collection<CompletionContextInformation> proposals = new Vector<CompletionContextInformation>();
		if (completionOk) {
			if (parser.hasValidStack()) {
				proposals.addAll(completion.createProposals((ASTElementNode)parser.getParseResult(0), context));
			}
		}

		return proposals;
	}
}
