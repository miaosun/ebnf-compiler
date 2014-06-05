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

  //Arraylist of nodes to store exception extra rules
  public static ArrayList<SimpleNode> exceptionNodes = new ArrayList<SimpleNode>();

  public static ArrayList<SimpleNode> ruleNodes = new ArrayList<SimpleNode>();


  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Ebnf parser;

  // added
  public String valor;
  public static ArrayList<String> identifiers = new ArrayList<String>();
  public static HashMap<String,String> mapTokensJavaCC = new HashMap<String,String>();
  static int cardinalToken = 0;

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

    getRulesAndExtraExceptions();

    generateParser(writer,programName);
  }

    public void getRulesAndExtraExceptions()
    {
        for (int i = 0; i < children.length; i++) 
        {
            SimpleNode n = (SimpleNode)children[i];
            if (n != null) 
            {
                n.checkIfAdd();  
            }
        }
    }

    public void checkIfAdd()
    {
      if(this.id == EbnfTreeConstants.JJTRULE)
        ruleNodes.add((SimpleNode) this.children[0]);
      else if(this.id == EbnfTreeConstants.JJTEXCEPT)
        exceptionNodes.add(this);

      if(children != null)
      {

        for (int i = 0; i < children.length; i++) 
          {
              SimpleNode n = (SimpleNode)children[i];
              if (n != null) 
              {
                  n.checkIfAdd();  
              }
          }
      }
          
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

  public void printExtraRules(Writer writer) throws IOException
  {
    SimpleNode child1;
    SimpleNode child2;

    for (int i = 0; i < exceptionNodes.size() ; i++) {
        SimpleNode n = exceptionNodes.get(i);
        if (n != null) {
          writer.write("void "+((SimpleNode) n.jjtGetChild(0)).jjtGetValue()+"_except_"+(i+1)+"() : {} \n { ");
          writer.write("     //TODO: redefine token on token list with "+((SimpleNode) n.jjtGetChild(0)).jjtGetValue()+" except "+((SimpleNode) n.jjtGetChild(1)).jjtGetValue()+"\n");
          writer.write("     // <insert_token_name_here> and after insert the token and its definition on the token list up!\n }");
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
    writer.write("class "+name+" { \n public static void main(String args[]) throws ParseException { \n ");
    writer.write(name+" parser = new "+name+"(System.in);\n");
    writer.write("SimpleNode root =parser.");
    writer.write(((SimpleNode) ruleNodes.get(0)).jjtGetValue()+"(); \n ");
    writer.write("root.dump(\"\"); \n } \n }\n");
    writer.write("PARSER_END("+name+")\n");
    writer.write("SKIP : { \" \" | \"\\t\" | \"\\r\" | \"\\n\" }\n");
    writer.write("TOKEN : \n{\n "+getTokens()+"\n}\n");
    startPrints(writer);
    printExtraRules(writer);
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
            if(pairs.getKey().equals("\'") || pairs.getKey().equals("\""))
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
    cardinalToken =1;
    SimpleNode n0 = (SimpleNode) children[0];
    SimpleNode n = (SimpleNode)children[1];
    String insertTokens=n.getNumberTokens();
    writer.write("void "+n0.jjtGetValue()+"() #"+n0.jjtGetValue()+" : {"+insertTokens+"} \n { ");
    
    n.printNode(writer);
    writer.write(" }");
  }

  public String getNumberTokens()
  {
      String tmp = "";

      int count = 1 ;
      if(children != null)
      {
        for(int i = 0 ; i < children.length ; i++)
        {
            SimpleNode ni = (SimpleNode) children[i];
            if(ni.id == EbnfTreeConstants.JJTTERMINAL)
            {
              if(count > 1)
                tmp+=", t"+count;
              else
                tmp+="Token t"+count;

              count++;
            }
        }
        tmp+=";";
        return tmp;
      }
      return "";     
  }

  public void printUnion(Writer writer) throws IOException {

    for (int i = 0; i < children.length; i++) {
      SimpleNode ni = (SimpleNode) children[i];
      ni.printNode(writer);
      if(this.value != null && i < children.length-1 )
           writer.write(" | ");
    }
  }

  public void printConcat(Writer writer) throws IOException {

    for (int i = 0; i < children.length; i++) {
      SimpleNode ni = (SimpleNode) children[i];
      ni.printNode(writer);
      if(this.value != null && i < children.length-1 )
           writer.write(" ");
    }
  }

  public void printException(Writer writer) throws IOException {
    SimpleNode child1 = (SimpleNode) children[0];

    writer.write(""+child1.jjtGetValue()+"_except_"+exceptionNodes.size()+"()");
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
      case EbnfTreeConstants.JJTUNION :
        printUnion(writer);
        break;
      case EbnfTreeConstants.JJTCONCAT :
        printConcat(writer);
        break;
      case EbnfTreeConstants.JJTEXCEPT :
        printException(writer);
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
          writer.write("t"+cardinalToken+"=<"+mapTokensJavaCC.get(filtered)+"> { jjtThis.value= \"\"+t"+cardinalToken+".image;}");
          cardinalToken++;
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
        System.out.println("Não processado: "+EbnfTreeConstants.jjtNodeName[this.id]);
        break;
    }

  }
}
/* JavaCC - OriginalChecksum=2edd50316e282230d4638a37fa816c17 (do not edit this line) */
