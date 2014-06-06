import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.3 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */


public
class SimpleNode implements Node {

	protected Node parent;
	protected Node[] children;
	protected int id;
	protected Object value;
	protected Ebnf parser;

	// added
	public String valor;
	public static ArrayList<String> identifiers = new ArrayList<String>();
	public static HashMap<String,String> mapTokensJavaCC = new HashMap<String,String>();

	//for dotty
	private static HashMap<String, String> labels = new HashMap<String,String>();
	private static int labelsCounter=0;

	private static String getNextLabel() {
		return "n"+labelsCounter++;
	}
	
	private static void resetLabelsMap() {
		labels.clear();
	}
	private static String addLabel(String value) {
		String node = getNextLabel();
		labels.put(node, value);
		return node;
	}

	public static void addIdentifier(String i) {
		identifiers.add(i.trim().replace(" ", "_"));
	}

	public static void addToken(String i){

		Pattern p = Pattern.compile("[^a-zA-Z0-9\\s]*"); 
		Matcher m = p.matcher(i);
		if(m.matches())
		{ //Nome é simbolo
			i = i.trim().replace(" ", "_");
			RandomString str = new RandomString(4);
			String string = str.nextString();
			i = i.trim().replace(" ", "_");
			mapTokensJavaCC.put(i,string);
		}
		else
		{
			//Nome é palavra
			String numberFilter;
			if(Character.isDigit(i.charAt(0)))
			{
				numberFilter = "i"+i;
				i = i.trim().replace(" ", "_");
				String filterSymbols = i.replaceAll("[^a-zA-Z0-9\\s]","");
				mapTokensJavaCC.put(filterSymbols ,numberFilter);
			}
			else
			{
				i = i.trim().replace(" ", "_");
				String filterSymbols = i.replaceAll("[^a-zA-Z0-9\\s]","");
				mapTokensJavaCC.put(i,filterSymbols);
			}      
		}
	}

	public static void dumpIds() {
		System.out.print(identifiers);
		//for(int i=0; i<identifiers.size(); i++) {
		// System.out.print(identifiers.get(i)+" - ");    
	}
	public static void dumpTokens() {
		System.out.print(mapTokensJavaCC);
		//for(int i=0; i<identifiers.size(); i++) {
		// System.out.print(identifiers.get(i)+" - ");    
	}

	public boolean verifyIdentifiers() {
		if(this.id == EbnfTreeConstants.JJTIDENTIFIER) {
			if(!identifiers.contains(this.value)) {
				System.out.println("\nIdentifier "+this.value+" not declared!");
				return false;
			}
		}
		else
		{
			if(children != null) {
				for(int i=0; i<jjtGetNumChildren();i++)
					if(!((SimpleNode)jjtGetChild(i)).verifyIdentifiers()) return false;
			}
		}
		return true;
	}

	public SimpleNode(int i) {
		id = i;
	}

	public SimpleNode(Ebnf p, int i) {
		this(i);
		parser = p;
	}

	public void jjtOpen() {
	}

	public void jjtClose() {
	}

	public void jjtSetParent(Node n) { parent = n; }
	public Node jjtGetParent() { return parent; }

	public void jjtAddChild(Node n, int i) {
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	public Node jjtGetChild(int i) {
		return children[i];
	}

	public int jjtGetNumChildren() {
		return (children == null) ? 0 : children.length;
	}

	public void jjtSetValue(Object value) { this.value = value; }
	public Object jjtGetValue() { return value; }
	public int jjtGetID() {return id;}

	/* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

	public String toString() { return EbnfTreeConstants.jjtNodeName[id]; }
	public String toString(String prefix) { return prefix + toString(); }

	/* Override this method if you want to customize how the node dumps
  out its children. */

	public void dump(String prefix, Writer writer, String programName) throws IOException{
		System.out.println(toString(prefix));
		dump2(prefix +" ");

		//generateParser(writer,programName);
	}

	public void startPrints(Writer writer) throws IOException
	{
		for (int i = 0; i < children.length; i++) {
			SimpleNode n = (SimpleNode)children[i];
			if (n != null) {
				n.printNode(writer);
			}
		}
	}


	public void dump2(String prefix) {
		System.out.println(toString(prefix));

		if(children == null)
			System.out.println(prefix+" ["+this.value+"]");

		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				if (n != null) {
					n.dump2(prefix +" ");
				}
			}
		}
	}

	public void generateParser(Writer writer, String name) throws IOException {
		writer.write("PARSER_BEGIN("+name+")\n");
		writer.write("class "+name+" { }\n");
		writer.write("PARSER_END("+name+")\n");
		writer.write("SKIP : { \" \" | \"\\t\" | \"\\r\" | \"\\n\" }\n");
		writer.write("TOKEN : \n{\n "+getTokens()+"\n}\n");
		startPrints(writer);
	}

	public String getTokens()
	{
		String tokens = "";
		Iterator it = mapTokensJavaCC.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry pairs = (Map.Entry)it.next();
			Pattern p = Pattern.compile("[^a-zA-Z0-9\\s]*");
			Matcher m = p.matcher((String)pairs.getKey());    
			if(m.matches())
			{ 
				if(it.hasNext())
				{
					if(pairs.getKey().equals("\'") || pairs.getKey().equals("\""))
						tokens+="< "+pairs.getValue()+" : \"\\"+pairs.getKey()+"\" > | \n";
					else
						tokens+="< "+pairs.getValue()+" : \""+pairs.getKey()+"\" > | \n";
				}
				else
				{
					if(!pairs.getKey().equals("\'"))
						tokens+="< "+pairs.getValue()+" : \"\\"+pairs.getKey()+"\" >";
					else
						tokens+="< "+pairs.getValue()+" : \""+pairs.getKey()+"\" >";

				}
			}
			else
			{
				if(it.hasNext())
					tokens+="< "+pairs.getValue()+" : \""+pairs.getKey()+"\" > | \n";
				else
					tokens+="< "+pairs.getValue()+" : \""+pairs.getKey()+"\" >";
			}      
		}
		return tokens;
	}

	public static boolean containsToken(String i){
		return mapTokensJavaCC.containsValue(i);
	}


	public void printRule(Writer writer) throws IOException {
		if(children == null) {
			System.out.println("ERROR");
			System.exit(1);
		}
		SimpleNode n0 = (SimpleNode) children[0];
		writer.write("void "+n0.jjtGetValue()+"() : \n { ");
		SimpleNode n = (SimpleNode)children[1];
		n.printNode(writer);
		writer.write(" }");
	}

	public void printSequence(Writer writer) throws IOException {
		for (int i = 0; i < children.length; i++) {
			SimpleNode ni = (SimpleNode) children[i];
			ni.printNode(writer);
			if(this.value != null && i < children.length-1 )
			{
				if(this.value.equals("|"))
				{
					writer.write(" | ");
				}
				else
					if(this.value.equals(","))
						writer.write(" ");
			}

		}
	}

	public void printRepetition(Writer writer) throws IOException {
		writer.write("( ");

		for (int i = 0; i < children.length; i++) {
			SimpleNode ni = (SimpleNode) children[i];
			ni.printNode(writer);

		}
		writer.write(" )* ");

	}
	public void printGrouping(Writer writer) throws IOException {
		writer.write(" ( ");

		for (int i = 0; i < children.length; i++) {
			SimpleNode ni = (SimpleNode) children[i];
			ni.printNode(writer);

		}
		writer.write(" ) ");
	}
	public void printOptional(Writer writer) throws IOException {
		writer.write(" [ ");

		for (int i = 0; i < children.length; i++) {
			SimpleNode ni = (SimpleNode) children[i];
			ni.printNode(writer);

		}
		writer.write(" ] ");
	}

	public void printNode(Writer writer) throws IOException {

		switch(this.id)
		{
		case EbnfTreeConstants.JJTRULE :
			printRule(writer);
			writer.write("\n");
			break;
			/*  case EbnfTreeConstants.JJTUNION :
        printSequence(writer);
        break;*/
		case EbnfTreeConstants.JJTCONCAT :
			printSequence(writer);
			break;
		case EbnfTreeConstants.JJTEXCEPT :
			printSequence(writer);
			break;
		case EbnfTreeConstants.JJTREPETITION :
			printRepetition(writer);
			break;
		case EbnfTreeConstants.JJTIDENTIFIER :
			writer.write(""+this.jjtGetValue()+"()");
			break;
		case EbnfTreeConstants.JJTTERMINAL : 
			String terminal = ""+this.jjtGetValue();
			String filtered = terminal.substring(1,terminal.length()-1);
			if(mapTokensJavaCC.containsKey(filtered))   
			{
				writer.write("<"+mapTokensJavaCC.get(filtered)+">");
			}
			// terminal = terminal.replaceAll("[^a-zA-Z0-9\\s]","");

			break;
		case EbnfTreeConstants.JJTGROUPING :
			printGrouping(writer);
			break;
		case EbnfTreeConstants.JJTOPTION :
			printOptional(writer);
			break;
		case EbnfTreeConstants.JJTSPECIALSEQ :
			String str = ""+this.jjtGetValue();
			str = str.replace("?","\"");
			str = str.replace(" ","_");
			writer.write(str);
			break;
		default :   
			System.out.println("Nao processado: "+EbnfTreeConstants.jjtNodeName[this.id]);
			break;
		}

	}
	/*
	public ArrayList<String> generateDotty(Writer dwriter) throws IOException {

		if(this.id == EbnfTreeConstants.JJTRULE) {
			//resetLabelsMap();
			SimpleNode start = new SimpleNode(-1);
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				n.generateDotty(dwriter);
			}
		}
		if(this.id == EbnfTreeConstants.JJTRULENAME) {
			dwriter.write("digraph "+this.value+" {\n");
		}

		if(this.id == EbnfTreeConstants.JJTCONCAT) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				ArrayList<String> array = n.generateDotty(dwriter);
				if(array.size()==1) {

				}
			}
		}
		if(this.id == EbnfTreeConstants.JJTREPETITION) {

		}
		if(this.id == EbnfTreeConstants.JJTUNION) {

		}

		if(this.id == EbnfTreeConstants.JJTTERMINAL || this.id == EbnfTreeConstants.JJTIDENTIFIER) {
			ArrayList<String> res = new ArrayList<String>();
			res.add(""+this.jjtGetValue());
			return res;
		}

		if(this.id == EbnfTreeConstants.JJTOPTION) {

		}
		if(this.id == EbnfTreeConstants.JJTGROUPING) {

		}
		if(this.id == EbnfTreeConstants.JJTEXCEPT) {

		}



		if (children != null) {
			for (int i = 0; i < children.length; ++i) {

				SimpleNode n = (SimpleNode)children[i];
				if (n != null) {
					//	n.generateDotty(dwriter, anterior, seguinte)(" ");
				}
			}
		}
		return null;
	}

	public ArrayList<String> dotRepetition(Writer dwriter) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		ArrayList<String> anteriores = new ArrayList<String>();
		String actual = null;

		for (int i = 0; i < children.length; ++i) {
			SimpleNode n = (SimpleNode)children[i];
			int id = n.jjtGetID();

			if(n.jjtGetID()==Ebnf.JJTTERMINAL || n.jjtGetID()==Ebnf.JJTIDENTIFIER){
				actual = addLabel(""+n.jjtGetValue());
				if(i==0)
				{
					res.add(actual); //primeiro res
					anteriores.clear();
					anteriores.add(actual); //anterior
				}
				else if(i==children.length-1)
				{
					res.add(actual); //ultimo res
					for(String ant : anteriores)
						dwriter.write(ant + " -> " + actual+"\n");
					dwriter.write(actual + " -> " + res.get(0)+"\n");
				}
				else
				{
					for(String ant : anteriores)
						dwriter.write(ant + " -> " + actual+"\n");
					anteriores.clear();
					anteriores.add(actual); //anterior
				}
			}
			else if(id == Ebnf.JJTCONCAT || id == Ebnf.JJTREPETITION || id == Ebnf.JJTGROUPING) {
				ArrayList<String> ret;
				if(id == Ebnf.JJTCONCAT)
					ret = n.dotConcat(dwriter);
				else if(id == Ebnf.JJTREPETITION)
					ret = n.dotRepetition(dwriter);
				else
					ret = n.dotGrouping(dwriter);

				String primeiro = ret.get(0);
				String ultimo = ret.get(1);
				if(i==0) {
					anteriores.clear();
					anteriores.add(ultimo);
					res.add(primeiro);
				}
				else if (i==children.length-1){
					res.add(ultimo);
					for(String ant : anteriores)
						dwriter.write(ant + " -> " + primeiro+"\n");
					dwriter.write(ultimo + " -> " + res.get(0)+"\n");
				}
				else {
					for(String ant : anteriores)
						dwriter.write(ant + " -> " + primeiro+"\n");
					anteriores.clear();
					anteriores.add(ultimo);
				}
			}
			else if (id == Ebnf.JJTUNION){
				ArrayList<String>[] u;
				u = n.dotUnion(dwriter);

				ArrayList<String> uant = u[0];
				ArrayList<String> useg = u[1];

				if(i==0) {
					res.addAll(uant);
					anteriores = useg;
				}
				else if(i==children.length-1) {
					for(String a : anteriores) {
						for(String ua : uant)
							dwriter.write(a + " -> " + ua+"\n");
					}
					for(String a : anteriores) {
						for(String ua : uant)
							dwriter.write(a + " -> " + ua+"\n");
					}
					anteriores = useg;
				}
				else {
					for(String a : anteriores) {
						for(String ua : uant)
							dwriter.write(a + " -> " + ua+"\n");
					}
				}
			}
			else if (id == Ebnf.JJTEXCEPT) {
				String ret = n.dotExcept();
				for(String ant : anteriores)
					dwriter.write(ant + " -> " + ret+"\n");
				anteriores.clear();
				anteriores.add(ret);
			}
		}
		return res;
	}


	private ArrayList<String>[] dotUnion(Writer dwriter) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO verify diferente situations
	public String dotExcept() {
		return addLabel(children[0]+"-"+children[1]);
	}

	public ArrayList<String> dotGrouping(Writer dwriter) {
		ArrayList<String> res = new ArrayList<String>();
		ArrayList<String> anteriores = new ArrayList<String>();
		String actual = null;

		for(int i=0; i<children.length; i++) {

		}



		return null;
	}

	public ArrayList<String> dotConcat(Writer dwriter) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		String anterior = null;
		String actual = null;
		for (int i = 0; i < children.length; ++i) {
			SimpleNode n = (SimpleNode)children[i];
			int id = n.jjtGetID();

			if(id==Ebnf.JJTTERMINAL || id==Ebnf.JJTIDENTIFIER) {
				actual = ""+n.jjtGetValue();
				if(i==0)
				{
					res.add(actual);
					anterior = actual;
				}
				else if(i==children.length-1) {
					res.add(actual);
					dwriter.write(anterior + " -> " + actual);
				}
				else {
					dwriter.write(anterior + " -> " + actual);
					anterior = actual;
				}
			}

			if(id==Ebnf.JJTREPETITION || id==Ebnf.JJTGROUPING) {
				ArrayList<String> ret;
				if(id==Ebnf.JJTREPETITION)
					ret = n.dotRepetition(dwriter);
				else
					ret = n.dotGrouping(dwriter);

				String primeiro = ret.get(0);
				String ultimo = ret.get(1);

				if(i==0) {
					res.add(primeiro);
					anterior = primeiro;
				}
				else if (i==children.length-1){
					res.add(ultimo);
					dwriter.write(anterior + " -> " + primeiro+"\n");
				}
				else {
					dwriter.write(anterior + " -> " + primeiro+"\n");
					anterior = ultimo;
				}
			}

			if(id==Ebnf.JJTEXCEPT)
			{
				actual = n.dotExcept();
				if(i==0)
					res.add(actual);
				else if(i==children.length-1)
				{
					res.add(actual);
					dwriter.write(anterior + " -> " + actual);
				}
				else {
					dwriter.write(anterior + " -> " + actual);
					anterior = actual;
				}
			}			
		}
		return res;
	}
	 */
	
	private void printLabelsDotty(Writer dwriter) throws IOException {
		Iterator it = labels.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry pairs = (Map.Entry)it.next();
			dwriter.write(pairs.getKey()+" [label="+pairs.getValue()+"];\n");
		}
	}

	public void printRulesDotty(Writer dwriter) throws IOException {

		for(int i=0; i<children.length-1; i++)
		{
			resetLabelsMap();
			SimpleNode rule = (SimpleNode)children[i];
			
			String name = (String) ((SimpleNode)rule.jjtGetChild(0)).jjtGetValue();
			System.out.println("TESTE: "+name);
			dwriter.write("digraph " + name + " {\n");
			dwriter.write("label = \"" + name + "\";\n");

			((SimpleNode) rule.jjtGetChild(1)).printRuleDotty(dwriter);
			
			printLabelsDotty(dwriter);
			dwriter.write("}\n\n");
		}
	}


	public void printRuleDotty(Writer dwriter) throws IOException {

		ArrayList<String> inNodes = new ArrayList<String>();
		String initialNode = addLabel("Start");
		inNodes.add(initialNode);

		String endNode = addLabel("End");

		ArrayList<String> returns = new ArrayList<String>();

		returns = printNodeDotty(inNodes, dwriter);
		/*
		int ruleContentID = ruleContent.jjtGetID();

		if(ruleContentID == Ebnf.TERMINAL || ruleContentID == Ebnf.IDENTIFIER)
		{
			returns = printNode(inNodes, dwriter);
		}

		while(ruleContentID != Ebnf.TERMINAL && ruleContentID != Ebnf.IDENTIFIER)
		{
			for(int i=0; i<ruleContent.children.length-1; i++)
			{
				printRuleDotty((SimpleNode)ruleContent.children[i], dwriter);
			}
		}
		 */
		for(String ret : returns)
		{
			dwriter.write(ret + " -> " + endNode+";\n");
		}


		// buscar filho com a regra e chamar printNode nesse filho com inNodes initialNode

		// Para cada nó retornada no printNode, fazer print desse a ligar ao endNode;
	}


	public ArrayList<String> printNodeDotty(ArrayList<String> inNodes, Writer dwriter) throws IOException {

		switch(id) {
		case Ebnf.JJTTERMINAL:
		case Ebnf.JJTIDENTIFIER:
			return printTerminal(inNodes, dwriter);
		case Ebnf.JJTCONCAT:
			return printConcat(inNodes, dwriter);
		case Ebnf.JJTREPETITION:
			return printRepetition(inNodes, dwriter);
		case Ebnf.JJTGROUPING:
			return printGrouping(inNodes, dwriter);
		case Ebnf.JJTUNION:
			return printUnion(inNodes, dwriter);
		case Ebnf.JJTOPTION:
			return printOption(inNodes, dwriter);
		}

		return null;

	}

	private ArrayList<String> printUnion(ArrayList<String> inNodes, Writer dwriter) throws IOException {
		ArrayList<String> res = new ArrayList<String>();

		for(int i=0; i<children.length;i++) {
			res.addAll(((SimpleNode) children[i]).printNodeDotty(inNodes, dwriter));
		}
		//res.add(""+((SimpleNode)children[i]).jjtGetValue());
		/*
		for(String n : inNodes) {
			for(int i=0; i<children.length-1; i++)
			{
				dwriter.write(n + " -> " + ((SimpleNode)children[i]).jjtGetValue() + ";\n");
			}
		}
		 */
		return res;
	}

	private ArrayList<String> printGrouping(ArrayList<String> inNodes, Writer dwriter) throws IOException {
		ArrayList<String> res = new ArrayList<String>();

		res.addAll(((SimpleNode) children[0]).printNodeDotty(inNodes, dwriter));

		return res;
	}

	private ArrayList<String> printRepetition(ArrayList<String> inNodes, Writer dwriter) throws IOException {
		//		ArrayList<String> res = new ArrayList<String>();
		//		res.add(""+((SimpleNode)children[children.length-1]).jjtGetValue());

		ArrayList<String> res = new ArrayList<String>();
		res.addAll(inNodes);

		//for(int i=0; i<children.length;i++) {

		//assumindo que o node repetition tem sempre s� um filho
		res = ((SimpleNode) children[0]).printNodeDotty(inNodes, dwriter);
		for(String r : res) {
			for(String inNode : inNodes)
				dwriter.write(r + " -> " + inNode + ";\n");
		}
		//}

		/*
		for(String n : inNodes) {
			dwriter.write(n + " -> " + ((SimpleNode)children[0]).jjtGetValue() + ";\n");
		}

		if(children.length >= 2)
		{
			for(int i=0; i<children.length-1; i++)
			{
				dwriter.write(((SimpleNode)children[i]).jjtGetValue() + " -> " +((SimpleNode)children[i+1]).jjtGetValue());
			}
			dwriter.write(((SimpleNode)children[children.length-1]).jjtGetValue() + " -> " +((SimpleNode)children[0]).jjtGetValue());
		}
		 */
		return res;
	}

	private ArrayList<String> printConcat(ArrayList<String> inNodes, Writer dwriter) throws IOException {
		//		ArrayList<String> res = new ArrayList<String>();
		//		res.add(""+((SimpleNode)children[children.length-1]).jjtGetValue());

		ArrayList<String> in = new ArrayList<String>();
		in.addAll(inNodes);
		/*
		for(String n : inNodes) {
			dwriter.write(n + " -> " + ((SimpleNode)children[0]).jjtGetValue() + ";\n");
		}*/
		for(int i=0; i<children.length;i++) {
			in = ((SimpleNode) children[i]).printNodeDotty(in, dwriter);
		}

		/*
		if(children.length >= 2)
		{
			for(int i=0; i<children.length-1; i++)
			{
				dwriter.write(((SimpleNode)children[i]).jjtGetValue() + " -> " +((SimpleNode)children[i+1]).jjtGetValue());
			}
		}
		 */
		return in;
	}

	private ArrayList<String> printOption(ArrayList<String> inNodes, Writer dwriter) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		res.addAll(inNodes);

		//assumindo que o node option tem sempre s� um filho
		res.addAll(((SimpleNode) children[0]).printNodeDotty(inNodes, dwriter));

		return res;
	}


	private ArrayList<String> printTerminal(ArrayList<String> inNodes, Writer dwriter) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		String value = addLabel(""+jjtGetValue());
		res.add(value);

		for(String n : inNodes) {
			dwriter.write(n + " -> " + value + ";\n");
		}
		return res;
	}


}
/* JavaCC - OriginalChecksum=2edd50316e282230d4638a37fa816c17 (do not edit this line) */
