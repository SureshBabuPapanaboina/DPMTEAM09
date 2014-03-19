package test;

class Odo {
	Str str = test.c1 ;
	
	public Odo(Conf conf) {
		str = conf.str;
	}
	Str getStr(){
		return str;
	}
	void setStr(Str s){
		str = s;
	}
}
