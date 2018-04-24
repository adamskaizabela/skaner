package kompilatory;

public class Token {
	String type;
	String value;
	Token(String _type, String _value){
		type=_type;
		value=_value;
	}
	
	String get_type(){
		return type;
	}
	
	String get_value(){
		return value;
	}

}
