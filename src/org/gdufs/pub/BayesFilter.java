package org.gdufs.pub;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kevin.zhang.NLPIR;

/*
 * ������������ṩ��һ�������ӿ�testForSpam(String str),str���ʼ�����
 * ��������ʼ��Ƿ�Ϊ�����ʼ�
 */
public class BayesFilter {

	public static String txt = "";
	public static int testAns [] = new int[4];
	public static String getStr(int t){
		String name = "";
		name += t / 100; name += t %100/10; name += t % 10;
		return name;
	}
	public static void main(String[] args) {
		//----�����Ѿ����õ������ʼ��ִ��ı���Ƶ��������ʼ��ִ��ı���Ƶ������Ƶ�ʼ���-----
		//buildTable();//Ӧ�÷��ھ�̬����������
		//----����ĺ��������ڼ�����Ч��������������ѵ����--------------------------
		runForTest();
	}
	public static Map<String,Double> hamMap = new HashMap<String,Double>();
	public static Map<String,Double> spamMap = new HashMap<String,Double>();
	public static Map<String,Integer> hamMapWords = new HashMap<String,Integer>();
	public static Map<String,Integer> spamMapWords = new HashMap<String,Integer>();
	public static int vocabulary = 600;
	public static int totalHamWords = 0;
	public static int totalSpamWords = 0;
	public static void runForTest(){
		buildTable();

		testAns[0] = testAns[1] = testAns[2] = testAns[3];
		String fileName = "trec06c/data/";
		
		//----------------------------����У����--------------------------
		boolean vis[][] = new boolean[223][310];
		for(int i=0;i<216;i++)for(int j=0;j<300;j++)
				vis[i][j] = false;
		String testFile = "trec06c/full/index";
		try{
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(testFile)));
			String line = null;
			int cnt = 0;
			while((line = br.readLine()) != null) {
				if(line.charAt(0) == 'S' || line.charAt(0) == 's') vis[cnt/300][cnt%300] = true;
				if(line.charAt(0) == 'H' || line.charAt(0) == 'h') vis[cnt/300][cnt%300] = false;
				cnt++;
			    //System.out.println(line);
			}
			br.close();
		}
		catch(IOException ex){
			System.out.println(ex.toString());
		}
		//-----------vis[][] -> true : SPAM , false : HAM---------------
		
		for(int i=0;i<215;i++) for(int j=0;j<300;j++){
			String path = getStr(i) + "/" + getStr(j);
			txt = "";
			try{
				BufferedReader br =
						new BufferedReader(
								new InputStreamReader(
										new FileInputStream(fileName + path)));
				String line = null;
				while ((line = br.readLine()) != null){
					if(!checkChinese.isContainsChinese(line)){
						continue;
					}
					txt += line;
				}
				br.close();
			}
			catch(Exception ex){
				System.out.println(ex.toString());
				continue;
			}
			System.out.print("mail: " + getStr(i) + getStr(j) + ": ");
			
			if(testForSpam(txt)){
				System.out.println("NO,HAM");
				if(vis[i][j] == false) testAns[0]++;
				else testAns[3]++;
			}
			else{
				System.out.println("YES,SPAM");
				if(vis[i][j] == false) testAns[1]++;
				else testAns[2]++;
			}
		}
		System.out.println("     right   ,   wrong");
		System.out.println("HAM  " + testAns[0] +"   " + testAns[1]);
		System.out.println("SPAM  " + testAns[2] + "   " + testAns[3]);
		
	}
	
	//-----------����������������������������hamword��spamword������ά����ѡ-------------------
	public static void buildTable(){
		String hamPath = "hamword600";
		String spamPath = "spamword600";
		vocabulary = 600;
		hamMap.clear(); spamMap.clear();
		hamMapWords.clear(); spamMapWords.clear();
		int hamSum = 0 , spamSum = 0;
		
		//----------------��ȡ�����ʼ��ִʼ����Ƶ����--------------
		try{
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(hamPath)));
			String line = null;
			while ((line = br.readLine()) != null){
				String word[] = line.split(" ");
				int tmp = Integer.parseInt(word[1]);
				hamSum += tmp;
			}
			totalHamWords = hamSum;
			br.close();
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		try{
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(hamPath)));
			String line = null;
			while ((line = br.readLine()) != null){
				String word[] = line.split(" ");
				double tmp = Double.parseDouble(word[1]);
				double rate = tmp / hamSum * 10;
				int tmpWords = Integer.parseInt(word[1]);
				hamMap.put(word[0],rate);
				hamMapWords.put(word[0],tmpWords);
			}
			br.close();
			
			//Set<String> set = hamMap.keySet();
			//for(String s:set) System.out.println( s + " " + hamMapWords.get(s));
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		//----------------��ȡ�����ʼ��ִʼ���Ƶ����--------------------
		try{
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(spamPath)));
			String line = null;
			while ((line = br.readLine()) != null){
				String word[] = line.split(" ");
				int tmp = Integer.parseInt(word[1]);
				spamSum += tmp;
			}
			totalSpamWords = spamSum;
			br.close();
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		try{
			BufferedReader br =
					new BufferedReader(
							new InputStreamReader(
									new FileInputStream(spamPath)));
			String line = null;
			while ((line = br.readLine()) != null){
				String word[] = line.split(" ");
				double tmp = Double.parseDouble(word[1]);
				double rate = tmp / spamSum * 10;
				int tmpWords = Integer.parseInt(word[1]);
				spamMap.put(word[0],rate);
				spamMapWords.put(word[0],tmpWords);
			}
			br.close();
			
			//Set<String> set = spamMap.keySet();
			//for(String s:set) System.out.println( s + " " + spamMapWords.get(s));
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	static public double log10(double value) {
		return Math.log(value) / Math.log(10);
	}
	
	public static boolean testForSpam(String str){
		buildTable();
		//----------------����һ��������䣬���зִ�--------------------------
		try {
			NLPIR testNLPIR = new NLPIR();
			String argu = ".";
			if (testNLPIR.NLPIR_Init(argu.getBytes("UTF-8"),1) == false){
				System.out.println("Init Fail!");
				return false;
			}
			//------------�����û��ʵ�ǰ----------nativeBytes��һ�����Ĵʵļ��ϣ����ڼ�¼�ô���ĳ���-----
			byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(str.getBytes("UTF-8"), 0);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length-1, "UTF-8");
			//System.out.println(nativeStr);
			
			//---------------------------�����㷨�������Ƶ�����ر�Ҷ˹��----------------------------
			
			//--------��words��¼���ֵĴ���------------------------------------------------------
			String words[] = nativeStr.split(" ");
			
			//-------��counta��countb�����Ƶ�����û�г��ָõ��ʣ���1.0��һ�º��Ըò��� *1.0��������----------------
			double counta = 0.0 , countb = 0.0;
			
			//-------mka , mkb ���ڼ����ܼ����ı��ﵥ�������������Ƿ���֣����û�г��֣�Ĭ��Ϊ�����ʼ�--------------
			boolean mka = false , mkb = false;
			
			//-------�ֱ��������ʼ�������(Ƶ�ʱ�)�������ʼ�������(Ƶ�ʱ�)�����ı���Ĵ�����ֵ�Ƶ�ʣ���ˣ���Ҷ˹ԭ��-----
			for(String s : words){
				if(hamMap.containsKey(s)){
					counta += (int)-log10((hamMapWords.get(s) + 1)/(totalHamWords+vocabulary));
					mka = true;              //��ʾ�ı��Ĵ����������ʼ����������ֹ�
				}
				else{
					counta += (int)-log10(1.0/600);
				}
				if(spamMap.containsKey(s)){
					countb += (int)-log10((spamMapWords.get(s) + 1)/(totalSpamWords+vocabulary));
					mkb = true;				 //��ʾ�ı��Ĵ����������ʼ������������
				}
				else{
					countb += (int)-log10(1.0/600);
				}
				//System.out.println(counta + " " + countb);
			}
			//-----------------------����true��ʾ����ʼ���������-------------------------------
			//-����20411+1322+30745+12022���ʼ�����20411+1322����������,30745+12022���������ʼ�,�ɱ�Ҷ˹ԭ��������Ӧ�ı���-
			counta += (int)-log10(21733.0/64500);
			countb += (int)-log10(42767.0/64500);
			counta = -counta; countb = -countb;
			
			//-----------�������д������ڵ���ʹ��,������ʾ�ı������Ƿ���ֵ���ʾ��Ϣ--------------------
			//String markWord = "";
			//if(!mka && !mkb) markWord = "Not fitting appearance!";
			//else markWord = "Some words Appearance!";
			//System.out.println(mka + "  " + mkb);
			//System.out.println("�������ʣ� "+counta + " �������ʣ� "+countb + '\n' + markWord);
			
			//-----------����ı���ÿ��������������������û�г��֣���ô�����������ʼ�--------------------
			if(!mka && !mkb) return true;
			//-----------����������ı����ֵĸ��ʶ���С����ôҲ�����������ʼ�--------------------------
			//if(counta < Math.log(1.0E-130) && countb < Math.log(1.0E-130)) return true;
			
			//if(countb < Math.log(1.0E-240)) return false;
			//if(counta < Math.log(1.0E-240)) return true;
			
			/*-----------���ݱ�Ҷ˹����Ƶ���ʵĻ�С�ĳ��ֵĴ����ʹ�--------------------------------
			 * ----------���������ֵ�Ƶ�ʻ�С��˵�����������������ʼ����ֶ࣬--------------------------
			 * ----------���Է���true��ʾΪ�����ʼ��ĸ��ʴ�---------------------------------------
			 */
			if(mka && mkb && counta < countb ||
					!mka && mkb && counta > countb ||
					mka && !mkb && counta < countb) return true; //�����ʼ�
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
}

//------------����������ڼ��ÿһ��string���������Ƿ�������---------------
class checkChinese {
	static String regEx = "[\u4e00-\u9fa5]";
	static Pattern pat = Pattern.compile(regEx);
	//----------�����������---------------
	public static void testExample(String input) {
		input = "Hell world!";
		System.out.println(isContainsChinese(input));
		input = "hello world �Ҳ�֪��Ŷ������";
		System.out.println(isContainsChinese(input));
	}
	//----------����ʵ�ʼ��ĺ���-----------
	public static boolean isContainsChinese(String str){
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find())    {
			flg = true;
		}
		return flg;
	}
}

