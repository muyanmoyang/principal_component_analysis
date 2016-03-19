package muyanmoyang.loadData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 加载原始数据
 * @author hadoop
 *
 */
public class DataLoad {
	public static void main(String[] args) throws IOException {
//		loadLabelData("C:/Users/Administrator/Desktop/DataCastle/train_y.csv") ; 
//		loadTrainData("C:/Users/Administrator/Desktop/DataCastle/train_x - 副本.csv") ;
	}
	
	/*
	 *  构建训练集
	 */
	
	/*
	 * 加载类别标签数据
	 */
	private static Map<String,String> loadLabelData(String labelFile) throws IOException{
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(labelFile),"UTF-8")) ;
		Map<String,String> labelMap = new LinkedHashMap<String,String>() ;
		String line;
		int count = 0;
		while((line=BR.readLine())!=null){
			count ++ ;
			if(count > 1){
				String str[] = line.split(",") ;
				labelMap.put(str[0],str[1]) ;
			}
		}
//		System.out.println(labelMap.size()) ;
		Set<Entry<String,String>> set = labelMap.entrySet() ;
		Iterator<Entry<String,String>> it = set.iterator() ;
		while(it.hasNext()){
			Entry<String, String> entry = it.next() ;
			System.out.println(entry.getKey() + " : " + entry.getValue()) ;
		}
		return labelMap ;
	}
	
	/*
	 * 加载原始训练数据
	 */
	public static Map<String,List<Double>> loadTrainData(String trainFile) throws IOException{ 
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile),"UTF-8")) ;
		Map<String,List<Double>> trainMap = new LinkedHashMap<String,List<Double>>() ;
		String line;
		int count = 0;
		while((line=BR.readLine())!=null){
			List<Double> list = new ArrayList<Double>() ;
			count ++ ;
			System.out.println(count) ;
//			if(count > 0){
//				String str[] = line.split(" ") ;
//				for(int i=1; i<str.length; i++){
//					if(str[i].startsWith("\"")){
//						str[i] = str[i].split("\"")[1] ;
//					}
//					list.add(Double.parseDouble(str[i])) ;
//				}
				
//			}
			String str[] = line.split(" ") ;
			for(int i=0; i<str.length; i++){
				list.add(Double.parseDouble(str[i])) ;
			}
			trainMap.put(count+"",list) ;
			System.gc();
		}
//		System.out.println(trainMap.size()) ;
		Set<Entry<String,List<Double>>> set = trainMap.entrySet() ;
		Iterator<Entry<String,List<Double>>> it = set.iterator() ;
		while(it.hasNext()){
			Entry<String,List<Double>> entry = it.next() ;
//			System.out.println(entry.getKey() + " : " + entry.getValue().get(0)) ;
		}
		return trainMap ;
	}
	
}
