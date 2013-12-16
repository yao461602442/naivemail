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
 * 这个公共类里提供了一个函数接口testForSpam(String str),str是邮件内容
 * 返回这封邮件是否为垃圾邮件
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
		//----根据已经做好的垃圾邮件分词文本词频表和正常邮件分词文本词频表，进行频率计算-----
		
		//----下面的函数是用于检测分类效果，分类样本是训练集--------------------------
		//runForTest();
	}
	public static Map<String,Double> hamMap = new HashMap<String,Double>();
	public static Map<String,Double> spamMap = new HashMap<String,Double>();
	public static void runForTest(){
		buildTable();
		testAns[0] = testAns[1] = testAns[2] = testAns[3];
		String fileName = "e:/mailset/trec06c/data/";
		
		//----------------------------用于校验结果--------------------------
		boolean vis[][] = new boolean[223][310];
		for(int i=0;i<216;i++)for(int j=0;j<300;j++)
				vis[i][j] = false;
		String testFile = "E:/mailset/trec06c/full/index";
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
		
		for(int i=80;i<100;i++) for(int j=0;j<300;j++){
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
	
	//-----------根据向量器构造向量表，向量器有hamword和spamword两个，维数可选-------------------
	public static void buildTable(){
		String hamPath = "hamword600";
		String spamPath = "spamword600";
		hamMap.clear(); spamMap.clear();
		int hamSum = 0 , spamSum = 0;
		
		//----------------获取正常邮件分词集里的频数和--------------
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
				hamMap.put(word[0],rate);
			}
			br.close();
			
			//Set<String> set = hamMap.keySet();
			//for(String s:set) System.out.println( s + " " + hamMap.get(s));
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		//----------------获取垃圾邮件分词集的频数和--------------------
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
				spamMap.put(word[0],rate);
			}
			br.close();
			
			//Set<String> set = spamMap.keySet();
			//for(String s:set) System.out.println( s + " " + spamMap.get(s));
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}

	public static boolean testForSpam(String str){
		//----------------处理一行中文语句，进行分词--------------------------
		buildTable();
		try {
			NLPIR testNLPIR = new NLPIR();
			String argu = ".";
			if (testNLPIR.NLPIR_Init(argu.getBytes("UTF-8"),1) == false){
				System.out.println("Init Fail!");
				return false;
			}
			//------------导入用户词典前----------nativeBytes是一个中文词的集合，用于记录该词语的出现-----
			byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(str.getBytes("UTF-8"), 0);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length-1, "UTF-8");
			//System.out.println(nativeStr);
			
			//---------------------------核心算法，计算词频（朴素贝叶斯）----------------------------
			
			//--------用words记录出现的词组------------------------------------------------------
			String words[] = nativeStr.split(" ");
			
			//-------用counta，countb计算词频，如果没有出现该单词，乘1.0，一下忽略该步骤 *1.0操作步骤----------------
			double counta = 1.0 , countb = 1.0;
			
			//-------mka , mkb 用于检测接受检测的文本里单词在向量器里是否出现，如果没有出现，默认为正常邮件--------------
			boolean mka = false , mkb = false;
			
			//-------分别在正常邮件向量器(频率表)和垃圾邮件向量器(频率表)里检测文本里的词组出现的频率，相乘（贝叶斯原理）-----
			for(String s : words){
				if(hamMap.containsKey(s)){
					counta *= hamMap.get(s);
					mka = true;              //表示文本的词组在正常邮件向量器出现过
				}
				if(spamMap.containsKey(s)){
					countb *= spamMap.get(s);
					mkb = true;				 //表示文本的词组在垃圾邮件向量器出想过
				}
				
			}
			//-----------------------返回true表示这封邮件是正常的-------------------------------
			
			//-----------假设100份邮件中有90封是正常的,10封是垃圾邮件,由贝叶斯原理，乘上相应的比例--------
			counta *= 90; countb *= 10;
			
			//-----------下面三行代码属于调试使用,用于显示文本词组是否出现的提示信息--------------------
			String markWord = "";
			if(!mka && !mkb) markWord = "Not fitting appearance!";
			else markWord = "Some words Appearance!";
			System.out.println("正常比率： "+counta + " 垃圾比率： "+countb + '\n' + markWord);
			
			//-----------如果文本的每个词组在两个向量器都没有出现，那么当做是正常邮件--------------------
			if(!mka && !mkb) return true;
			
			//-----------如果在两个文本出现的概率都很小，那么也当做是正常邮件--------------------------
			if(counta < 1.0E-200 && countb < 1.0E-200) return true;
			
			/*-----------根据贝叶斯，词频概率的积小的出现的次数就大，--------------------------------
			 * ----------当正常出现的频率积小，说明次数多于在垃圾邮件出现多，--------------------------
			 * ----------所以返回true表示为正常邮件的概率大---------------------------------------
			 */
			if(counta < countb) return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}
}

//------------这个类是用于检测每一个string的内容中是否含有中文---------------
class checkChinese {
	static String regEx = "[\u4e00-\u9fa5]";
	static Pattern pat = Pattern.compile(regEx);
	//----------用于理解样例---------------
	public static void testExample(String input) {
		input = "Hell world!";
		System.out.println(isContainsChinese(input));
		input = "hello world 我不知道哦你在哪";
		System.out.println(isContainsChinese(input));
	}
	//----------用于实际检测的函数-----------
	public static boolean isContainsChinese(String str){
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find())    {
			flg = true;
		}
		return flg;
	}
}

