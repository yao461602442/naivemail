import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.io.*;


public class readDoc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		buildSet(wordFilter);
		mergeSpamword(600);
		mergeHamWord(600);
	}
	public static Map<String,Integer> map= new HashMap<String,Integer>();
	public static Set<String> wordFilter = new HashSet<String>();
	public static Map<String,Integer> spamMap=new TreeMap<String,Integer>();
	public static Map<String,Integer> hamMap=new TreeMap<String,Integer>();
	
	public static void buildSet(Set<String> set){
		//---------------����������ڹ���һ����û�õĴ���Ƚ�����һ�����������wordFilter------------
		set.clear();
		String path = "E:/mailset/Chinese_stopwords.txt";
		try{
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(path)));
			String line = null;
			while ((line = br.readLine()) != null){
				if(!set.contains(line)) set.add(line);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	
	public static void mergeHamWord(int dimension){
		//---------------------�������ļ��Ĵʼ�д���ı�hamword----------------------------------
		map.clear();
		
		//---------------------��ȡ�����ļ����ļ�������ÿ���ִʶ����й��ˣ��ټ��뵽�����ı���-----------
		for(int i=0;i<215;i++){ //215
			for(int j=0;j<300;j++){ //299
				//open the file
				String fileName = "E:/mailset/hammail/";
				fileName += (getStr(i)+getStr(j));
				System.out.println(fileName);
				try{
					BufferedReader br =
							new BufferedReader(
									new InputStreamReader(
											new FileInputStream(fileName)));
					String line = null;
					while ((line = br.readLine()) != null){
						if(wordFilter.contains(line)) continue;
						if(!map.containsKey(line)) map.put(line,1);
						else map.put(line,map.get(line)+1);
					}
					br.close();
				}
				catch(Exception ex){
					continue;
				}
			}
		}
		
		// ------------------���д�Ƶ����ȡǰ600ά�������ʵ�����ά����ȷ��Ч����------------
		hamMap.clear();
		Set<String> key = map.keySet();
		for(String s : key){
			//System.out.print("in adding:");
			hamMap.put(s,map.get(s));
			//System.out.println(s + " " + ansMap.get(s));
		}
		
		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>( 
				hamMap.entrySet()); 
		//����ǰ 
		for (int i = 0; i < infoIds.size(); i++) { 
			;//String id = infoIds.get(i).toString(); //System.out.println(id); 
		}
		
		//���� 
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() { 
			public int compare(Map.Entry<String, Integer> o1, 
				Map.Entry<String, Integer> o2) { 
					return (o2.getValue() - o1.getValue()); 
				} 
			}); 
		
		//--------����� --------------����ʹ�ã�������ʾ����ͳ�ƴ�Ƶ��Ч��----------------
		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
			//System.out.println(id);
		}
		try{
			java.io.File file = new java.io.File("E:/mailset/hamword");
			java.io.PrintWriter output = new java.io.PrintWriter(file);
			for (int i=0;i<dimension;i++){
				String str = infoIds.get(i).getKey();
				int val = infoIds.get(i).getValue();
				output.print(str);
				output.print(' ');
				output.println(val);
			}
			output.close();
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		System.out.println("Words of ham mails have been recorded!Processed to End");
	}
	
	public static void mergeSpamword(int dimension){
		//--------------------�������ʼ��ķִ�д���ļ�spamword��-----------------------
		map.clear();
		for(int i=0;i<215;i++){ //215
			for(int j=0;j<300;j++){ //299
				//open the file
				String fileName = "E:/mailset/spammail/";
				fileName += (getStr(i)+getStr(j));
				System.out.println(fileName);
				try{
					BufferedReader br =
							new BufferedReader(
									new InputStreamReader(
											new FileInputStream(fileName)));
					String line = null;
					while ((line = br.readLine()) != null){
						if(wordFilter.contains(line)) continue;
						if(!map.containsKey(line)) map.put(line,1);
						else map.put(line,map.get(line)+1);
					}
					br.close();
				}
				catch(Exception ex){
					continue;
				}
			}
		}
		
		// ------------------���д�Ƶ����ȡǰ600ά�������ʵ�����ά����ȷ��Ч����------------
		spamMap.clear();
		Set<String> key = map.keySet();
		for(String s : key){
			//System.out.print("in adding:");
			spamMap.put(s,map.get(s));
			//System.out.println(s + " " + ansMap.get(s));
		}
		
		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>( 
				spamMap.entrySet()); 
		//����ǰ 
		for (int i = 0; i < infoIds.size(); i++) { 
			;//String id = infoIds.get(i).toString(); //System.out.println(id); 
		}
		
		//���� 
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() { 
			public int compare(Map.Entry<String, Integer> o1, 
				Map.Entry<String, Integer> o2) { 
					return (o2.getValue() - o1.getValue()); 
				} 
			}); 
		//--------����� --------------����ʹ�ã�������ʾ����ͳ�ƴ�Ƶ��Ч��----------------
		//---------2013.12.15,15:38����һ����ɷִʣ�����Ч������-----------------------
		for (int i = 0; i < infoIds.size(); i++) {
			String id = infoIds.get(i).toString();
			//System.out.println(id);
		}
		try{
			java.io.File file = new java.io.File("E:/mailset/spamword");
			java.io.PrintWriter output = new java.io.PrintWriter(file);
			for (int i=0;i<dimension;i++){
				String str = infoIds.get(i).getKey();
				int val = infoIds.get(i).getValue();
				output.print(str);
				output.print(' ');
				output.println(val);
			}
			output.close();
		}catch(Exception ex){
			System.out.println("in spam func!" + ex.toString());
		}
		System.out.println("Words in spam mails have been recorded!Processed to End");
	}
	
	public static String getStr(int t){
		String name = "";
		name += t / 100; name += t %100/10; name += t % 10;
		return name;
	}
}
