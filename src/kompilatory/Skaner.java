package kompilatory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Skaner {
	
	public static int Nmax=100;
	
	public static String[] lines=new String[Nmax];
	
	public static int nr_line=0;
	
	public static int nr_char=0;
	
	public static boolean test=true;
	
	public static String[] keywords={"abstract", "assert", "boolean",
    "break", "byte", "case", "catch", "char", "class", "const",
    "continue", "default", "do", "double", "else", "extends", "false",
    "final", "finally", "float", "for", "goto", "if", "implements",
    "import", "instanceof", "int", "interface", "long", "native",
    "new", "null", "package", "private", "protected", "public",
    "return", "short", "static", "strictfp", "super", "switch",
    "synchronized", "this", "throw", "throws", "transient", "true",
    "try", "void", "volatile", "while"};
	
	public static String[] mathoperators={"+", "*","/", "%", "&", "||"};
	
	public static String[] brackets={"{","}" ,"[", "]", "(", ")"};
	
	public static LinkedList<Token> tockens=new LinkedList<Token>();
	
	public static boolean iskeywords(String toc){
		for(int i=0;i<keywords.length;i++){
			if(keywords[i].equals(toc)){
				return true;
			}
		}
		return false;
	}
	
	
	public static char get(){
		
		if(nr_line>=Nmax){
			test=false;
			return ' ';
		}
		
		if(lines[nr_line].isEmpty()){
			nr_line++;
			nr_char=0;
			return get();
		}
		
		if(nr_line==Nmax-1 && lines[Nmax-1].isEmpty()){
			test=false;
			return ' ';
		}
		
		if(nr_char<lines[nr_line].length()){
			nr_char++;
			return lines[nr_line].charAt(nr_char-1);
		}else{
			nr_line++;
			nr_char=0;
			return get();
		}
			
	}
	
	public static String getwhile(char begsymbol, Pattern pat,int wers,String cond){
		
		int nr_line_pom=nr_line;
		int nr_char_pom=nr_char;
		
		String nextchar=""+get();
		String ntoken=""+begsymbol;
		int dot=0;
		
		while(test && dot<2 && (pat.matcher(nextchar).matches()||nextchar.equals(cond))){
			 nr_line_pom=nr_line;
			 nr_char_pom=nr_char;
			
			if(wers==1 && nextchar=="."){
				dot++;
			}
			ntoken=ntoken+nextchar;
			nextchar=""+get();
		}
		
		 nr_line=nr_line_pom;
		 nr_char=nr_char_pom;
		 
		 return ntoken;
	}
	
	public static String scan(char symbol){
		
		Pattern pat_alpha = Pattern.compile("[a-zA-Z]|_");
		Pattern pat_number= Pattern.compile("[0-9]");
		Pattern pat_mathlog=Pattern.compile("[+*-]");
		Pattern pat_bracket=Pattern.compile("[)(}{]");
		Pattern pat_word=Pattern.compile("[a-zA-Z]|[0-9]|_");
		Pattern pat_number_d=Pattern.compile("[0-9]");
		Pattern pat_nonquota=Pattern.compile("[^\"]");
		
	
		if(symbol==' '){
			tockens.add(new Token("space"," "));
			return " ";
		}
		
		if(symbol==';'){
			tockens.add(new Token("semicolon",";"));
			return "; <br>";
		}

		if(symbol=='"'){
			String ntoken=getwhile(symbol,pat_nonquota,0,"");
			get();
			tockens.add(new Token("String",ntoken+"\""));
		return "<font color=\"blue\">"+ntoken+"\""+"</font>";
		}
		
		if(symbol=='@'){
			String atcomment="@";
			for(int i=nr_char;i<lines[nr_line].length();i++){
				atcomment+=get();
			}
			tockens.add(new Token("at_comment",atcomment));
			return "<font color=\"gray\">"+atcomment+"</font> </br>";
		}
		
		if(pat_bracket.matcher(""+symbol).matches()){
			tockens.add(new Token("bracket",""+symbol));
			return "<font color=\"brown\">"+symbol+"</font>";
		}
		
		if(pat_alpha.matcher(""+symbol).matches()){
			
			String ntoken=getwhile(symbol,pat_word,0,"");
			
			if(iskeywords(ntoken)){
				tockens.add(new Token("keywords",ntoken));
				
			return "<font color=\"purple\">"+ntoken+"</font>";
			}else{
				tockens.add(new Token("variable",ntoken));
			 return "<font color=\"silver\">"+ntoken+"</font>";
			}
			
		}
		
	 if(pat_number.matcher(""+symbol).matches()){
		 String ntoken=getwhile(symbol,pat_number_d,1,".");
		 tockens.add(new Token("double",ntoken));
			return "<font color=\"blue\">"+ntoken+"</font>";
		}
		
	if(symbol=='/'){
		if(test){
			char next=get();
			if(next=='/'){
				String comment="//";
				for(int i=nr_char;i<lines[nr_line].length();i++){
					comment+=get();
				}
				tockens.add(new Token("comment",comment));
				return "<font color=\"green\">"+comment+"</font> </br>";
			}else{
				tockens.add(new Token("backslash","/"));
				return "/";
			}
		}
	}
	    tockens.add(new Token("error",""+symbol));
		return ""+symbol;
		
	}
	
	
	

	
	
	public static void reading(){
		   String mypath="resources/program.txt";
           FileReader fr = null;
		   String linia = "";
		   int n=0;

		   // OTWIERANIE PLIKU:
		   try {
		     fr = new FileReader(mypath);
		   } catch (FileNotFoundException e) {
		       System.out.println("B£¥D PRZY OTWIERANIU PLIKU!");
		       System.exit(1);
		   }

		   BufferedReader bfr = new BufferedReader(fr);
		   // ODCZYT KOLEJNYCH LINII Z PLIKU:
		   try {
		     while((linia = bfr.readLine()) != null){
		    	lines[n]=linia;
		    	n++;
		        //System.out.println(linia);
		     }
		    } catch (IOException e) {
		        System.out.println("B£¥D ODCZYTU Z PLIKU!");
		        System.exit(2);
		   }

		   // ZAMYKANIE PLIKU
		   try {
		     fr.close();
		    } catch (IOException e) {
		         System.out.println("B£¥D PRZY ZAMYKANIU PLIKU!");
		         System.exit(3);
		        }
		    
	}

    
	
public static void main(String[] a){
	
	for(int i=0;i<Nmax;i++){
		lines[i]="";
	}
	reading();	
	
	
	
	char symbol='0';
	String filePath = "color.html";
			FileWriter fileWriter = null;
			String tocken;
            
			try {
			    fileWriter = new FileWriter(filePath);
			        fileWriter.write("<html><head><title>Tytu³ strony</title></head><body>");
			    while(test){
					symbol=get();
					tocken=scan(symbol);
					fileWriter.write(tocken);
				}
			    fileWriter.write("</body></html>"); 
			   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			    if (fileWriter != null) {
			        try {
						fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
            
	
}
}

