package test;

import odometry.Odometer;

public class test {
	class Str {
		Integer st ; 
		public Str(Integer s){
			st = s;
		}
		Integer getSt(){
			return st;
		}
		void setSt(Integer s ){
			st = s;
		}
	}

	static Str c1 ;
	static Conf conf ;

	class Conf {
		Str str ;
		Str getStr(){
			return str;
		}
		void setStr(Str s){
			str = s;
		}
	}
	class Odo {
		Str str = conf.str ;
		
		Str getStr(){
			return conf.str;
		}
		void setStr(Str s){
			str = s;
		}
	}
	
	public void main(String [] args){
		
		conf = new Conf();
		conf.setStr(new Str(1));
		Odo odo = new Odo();
		
		System.out.println("config" + conf.getStr());
		System.out.println("odo" + odo.getStr());
		conf.setStr(new Str(2));
		System.out.println("config" + conf.getStr());
		System.out.println("odo" + odo.getStr());
		
		
		
	}
	
}
