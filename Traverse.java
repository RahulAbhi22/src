import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Traverse implements Runnable {
List<String> visitedUrls ;
Set<String>  toBeVisitedUrls;
String rootUrl ;

Object lock1 = new Object();
Object lock2 = new Object();

public Traverse(String url) {
	visitedUrls =   Collections.synchronizedList( new CopyOnWriteArrayList <String>());
	toBeVisitedUrls =  new CopyOnWriteArraySet<String>();
	//toBeVisitedUrls.add(url);
	rootUrl = url; 
}


//To fetch all the urls to be visisted
private synchronized void fetch(String url) throws IOException{
	List l1 = new ArrayList<String>();
	
	synchronized(lock1){
	visitedUrls.add(url);
	}
	Document doc = Jsoup.connect(url).get();
	Elements titles = doc.select(".entrytitle");

	//print all titles in main page
	for(Element e: titles){
		System.out.println("text: " +e.text());
		System.out.println("html: "+ e.html());
	}	

	//print all available links on page
	Elements links = doc.select("a[href]");
	for(Element l: links){
		System.out.println("link: " +l.attr("abs:href"));
		synchronized(lock2){
			toBeVisitedUrls.add(l.attr("abs:href"));
		}
			
	}

	
}


	


public synchronized void triggerTraverse() {
	
		visitedUrls.add(rootUrl);
		try {
			fetch(rootUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}

private Boolean isVisited(String url){
	return visitedUrls.contains(url);
}


@Override
public void run() {
	synchronized (lock2) {
		
	
	for(String url : toBeVisitedUrls ){
		System.out.println(url);
			if(!isVisited(url)){
				System.out.println(url);
				try {
					fetch(url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
}


