
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Traverse traverse = new Traverse("http://www.jabong.com/");
		traverse.triggerTraverse();
		
		
		for(int i =0 ; i< 10 ; i++){
			//System.out.println("Thread : "+i);
			Thread t = new Thread(traverse);
			t.start();
		
		}
	}

}
