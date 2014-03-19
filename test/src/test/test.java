package test;


public class test {
	


	static Str c1 ;
	static Conf conf;


	public static void main(String [] args){
		test t = new test();
		t.conf = new Conf();
		conf.setStr(new Str(1));
		Odo odo = new Odo(conf);
		
		System.out.println("config" + conf.getStr().getSt().toString());
		System.out.println("odo" + odo.getStr().getSt().toString());
		conf.setStr(new Str(2));
		System.out.println("config" + conf.getStr().getSt().toString());
		System.out.println("odo" + odo.getStr().getSt().toString());
		
		
		
	}
	
}
