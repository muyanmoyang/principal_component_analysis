package muyanmoyang.pca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class dataProcess {
	public static void main(String[] args) throws IOException {
//		FileWriter writer = new FileWriter(new File("C:/Users/Administrator/Desktop/DataCastle/燕尾花2.csv")) ;
//		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Users/Administrator/Desktop/DataCastle/燕尾花.csv"),"UTF-8")) ;
//		String line ;
//		int count = 0 ;
//		writer.write("user,x1,x2,x3,x4" + "\n");
//		while((line=BR.readLine())!=null){
//			count ++ ;
//			writer.write(count + "," +line + "\n");	
//			writer.flush();
//		}
//		writer.close();
		processNewMatrix("C:/Users/Administrator/Desktop/DataCastle/降维矩阵.csv",
					"C:/Users/Administrator/Desktop/DataCastle/new_降维矩阵.csv");
	}
	
	/*
	 *  处理降维后的新矩阵，只写入非空的数值
	 */
	public static void processNewMatrix(String fileDir, String resultDir) throws IOException{
		BufferedReader BR = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir),"UTF-8")) ;
		FileWriter writer = new FileWriter(new File(resultDir)) ;
		String line ;
		int count = 0 ;
		while((line=BR.readLine()) != null){
			count ++ ;
			System.out.println(count);
			String str[] = line.split(" ") ;
			if(count > 1){
				for(int i=0; i<str.length; i++){
					if(!str[i].equals("")){
						writer.write(str[i] + "\t");
					}
				}
				writer.write("\n") ;
			}
			writer.flush();			
		}
		writer.close();
	}
}
