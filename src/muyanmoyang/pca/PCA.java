package muyanmoyang.pca;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import Jama.Matrix;
import muyanmoyang.loadData.DataLoad;

/*
 *  主成分分析
 */
public class PCA {
	
	public static void main(String[] args) throws IOException { 
		DataLoad data = new DataLoad() ;
		PCA pca = new PCA() ;
		// 加载训练数据
		Map<String,List<Double>> trainMap = data.loadTrainData("C:/Users/Administrator/Desktop/DataCastle/内容TFIDF2.txt") ;
		// 计算训练集矩阵各列的平均值
		List<Double> averageList = pca.computeDataAdjust(trainMap,"C:/Users/Administrator/Desktop/DataCastle/各列数据的平均值.csv") ;
		// 经过特征中心化后的矩阵B
		Map<String,List<Double>> map_B = computeB(trainMap, averageList,"C:/Users/Administrator/Desktop/DataCastle/B_matrix.csv") ; // 得到特征中心化的B矩阵
		// 计算协方差矩阵
		Double[][] matrix_B = getMatrixFromB(map_B) ;
		double[][] covariance_matrix = computeCovariance(matrix_B) ;
		// 计算特征值
		double[][] eigenvalue_matrix = getEigenvalueMatrix(covariance_matrix) ;
		// 计算特征向量
		double[][] eigenvector_matrix = getEigenVectorMatrix(covariance_matrix) ;
		
		// 主成分矩阵
		Matrix principalMatrix = pca.getPrincipalComponent(eigenvalue_matrix, eigenvector_matrix);
		// 降维后的矩阵
		Matrix resultMatrix = pca.getResult(trainMap, principalMatrix);
		
		
	}
	private static final double threshold = 0.9999 ;// 特征值阈值
	/*
	 * 计算训练矩阵每列平均值
	 */
	static List<Double> computeDataAdjust(Map<String,List<Double>> trainMap,String averageWriter) throws IOException{
		Map<Integer,List<Double>> tmp = new LinkedHashMap<Integer,List<Double>>() ;
		FileWriter writer = new FileWriter(new File(averageWriter)) ;
		List<Double> averageList = new ArrayList<Double>() ;
		Double average ;
		Set<Entry<String,List<Double>>> set = trainMap.entrySet() ;
		Iterator<Entry<String,List<Double>>> it = set.iterator() ;
		int count = 0 ;
		while(it.hasNext()){
			count ++ ;
			System.out.println(count) ;
			Entry<String,List<Double>> entry = it.next() ;
			List<Double> list = entry.getValue() ;
			tmp.put(count,list) ;
			System.gc();
		}
		for(int i=0; i<1000; i++){
			System.out.println("计算各列平均值：" + (i+1));  
			List<Double> column = new ArrayList<Double>() ;
			Iterator<Entry<Integer,List<Double>>> it2 = tmp.entrySet().iterator() ;
//			System.out.println("it2:" + it2.next().getValue());
			while(it2.hasNext()){
				Entry<Integer, List<Double>> entry2 = it2.next() ;
				System.out.println("-----" + entry2.getValue().size()) ;
				column.add(entry2.getValue().get(i)) ;
			}
			average = computeAverage(column) ;
			averageList.add(average) ;
		}
		int columnNum = 0 ;
		for(Double num : averageList){
			columnNum ++ ;
			writer.write(columnNum + "\t" + num + "\n");
			writer.flush();
		}
		writer.close();
		return averageList ;
	}

	private static Double computeAverage(List<Double> column) { 
		// TODO Auto-generated method stub
		Double sum = 0.0 ; 
		for(int i=0; i<column.size(); i++){
			sum += column.get(i) ;
		}
		return sum / column.size() ;
	}
	
	/*
	 *  计算特征中心化后的矩阵B，各列减去该列均值
	 */
	static Map<String,List<Double>> computeB(Map<String,List<Double>> trainMap,List<Double> averageList,String B_matrixWriter) throws IOException{
		FileWriter BmatrixWriter = new FileWriter(new File(B_matrixWriter)) ;
		Set<Entry<String,List<Double>>> set = trainMap.entrySet() ;
		Iterator<Entry<String,List<Double>>> it = set.iterator() ;
		Map<String,List<Double>> new_trainMap = new LinkedHashMap<String,List<Double>>() ;		
		
		Double average ;
		int count = 0 ;
		while(it.hasNext()){
			count ++ ;
			System.out.println("特征中心化矩阵：" + count) ;
			List<Double> newList = new ArrayList<Double>() ;
			Entry<String,List<Double>> entry = it.next() ;
			List<Double> list = entry.getValue() ;
			String users = entry.getKey() ;
			for(int i=0; i<averageList.size(); i++){
				newList.add(list.get(i)-averageList.get(i)) ;
			}
			new_trainMap.put(users, newList) ;
			System.gc();
		}
		
		Iterator<Entry<String,List<Double>>> it2 = new_trainMap.entrySet().iterator() ;
		while(it2.hasNext()){
			Entry<String,List<Double>> entry = it2.next() ;
			List<Double> list = entry.getValue() ;
			BmatrixWriter.write(entry.getKey() + "\t");
			for(Double elem : list){
				BmatrixWriter.write(elem + "\t");
			}
			BmatrixWriter.write("\n");
			BmatrixWriter.flush();
		}
		BmatrixWriter.close();
		return new_trainMap ;
	}
	
	static Double[][] getMatrixFromB(Map<String,List<Double>> matrix_B){
		int row = matrix_B.entrySet().iterator().next().getValue().size() ; // 中心特征矩阵的列数
		int column = matrix_B.size() ; // 行数
		Double[][] B_Matrix = new Double[column][row]; 
		
		Iterator<Entry<String, List<Double>>> it = matrix_B.entrySet().iterator();
		int count = 0 ;
		while(it.hasNext()){
			Entry<String, List<Double>> entry = it.next() ;
			List<Double> list = entry.getValue() ;
			for(int i=0; i<list.size(); i++){
				B_Matrix[count][i] = list.get(i) ;
			}
			count ++ ;
			System.out.println("特征中心化矩阵map——>矩阵：" + count);
		}
 		return B_Matrix ;
	}
	
	/*
	 *  计算协方差矩阵
	 */
	static double[][] computeCovariance(Double[][] matrix_B) throws IOException{ 
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.0000"); 
		int column = matrix_B[0].length ; // 中心特征矩阵的列数
		System.out.println("列数：" + column) ;
		int row = matrix_B.length ; // 行数
		System.out.println("行数：" + row) ;
		double[][] covariance_Matrix = new double[column][column];
		for(int i=0; i<column; i++){ // 列
			System.out.println("计算协方差矩阵：" + (i+1)) ;
			for(int j=0; j<column; j++){ // 行
				Double sum = 0.0 ;
				for(int k=0; k< row; k++){
					sum += matrix_B[k][i] * matrix_B[k][j] ;
				}
				covariance_Matrix[i][j] =  Double.parseDouble(df.format((sum / (row-1)))) ;  //   (column * column)维度
			}
		}
		
		printMatrix("C:/Users/Administrator/Desktop/DataCastle/测试集/协方差矩阵.csv",covariance_Matrix);
		return covariance_Matrix ;
	}

	
	/**
	 * 求特征值矩阵
	 * 
	 * @param covariance_Matrix			协方差矩阵
	 * @return result 					向量的特征值二维数组矩阵
	 * @throws IOException 
	 */
	public static double[][] getEigenvalueMatrix(double[][] covariance_Matrix) throws IOException {
		Matrix A = new Matrix(covariance_Matrix);
		// 由特征值组成的对角矩阵,eig()获取特征值
//		A.eig().getD().print(10, 6);
		System.out.println("计算特征值...") ;
		double[][] eigenvalue_matrix = A.eig().getD().getArray();
		printMatrix("C:/Users/Administrator/Desktop/DataCastle/测试集/特征值矩阵.csv", eigenvalue_matrix); 
		return eigenvalue_matrix;
	}
	
	/**
	 * 标准化矩阵（特征向量矩阵）
	 * 
	 * @param eigenvalue_matrix			特征值矩阵
	 * @return result 			标准化后的二维数组矩阵
	 * @throws IOException 
	 */
	public static double[][] getEigenVectorMatrix(double[][] covariance_Matrix) throws IOException {
		System.out.println("计算特征向量...") ;
		Matrix A = new Matrix(covariance_Matrix);
//		A.eig().getV().print(6, 2);
		double[][] eigenvector_matrix = A.eig().getV().getArray();
		printMatrix("C:/Users/Administrator/Desktop/DataCastle/测试集/特征向量矩阵.csv", eigenvector_matrix); 
		return eigenvector_matrix;
	}
	
	/**
	 * 寻找主成分,选取大的特征值对应的特征向量，得到新的数据集
	 * 
	 * @param prinmaryArray		原始二维数组数组
	 * @param eigenvalue		特征值二维数组
	 * @param eigenVectors		特征向量二维数组
	 * @return principalMatrix 	主成分矩阵
	 */
	public Matrix getPrincipalComponent(double[][] eigenvalue, double[][] eigenVectors){
		System.out.println("选取主成分...") ;
		Matrix A = new Matrix(eigenVectors);// 定义一个特征向量矩阵
		double[][] tEigenVectors = A.transpose().getArray();// 特征向量转置
		Map<Integer, double[]> principalMap = new HashMap<Integer, double[]>();// key=主成分特征值，value=该特征值对应的特征向量
		TreeMap<Double, double[]> eigenMap = new TreeMap<Double, double[]>(
				Collections.reverseOrder());// key=特征值，value=对应的特征向量；初始化为翻转排序，使map按key值降序排列
		double total = 0;// 存储特征值总和
		int index = 0, n = eigenvalue.length;
		double[] eigenvalueArray = new double[n];// 把特征值矩阵对角线上的元素放到数组eigenvalueArray里
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j)
					eigenvalueArray[index] = eigenvalue[i][j];
			}
			index++;
		}

		for (int i = 0; i < tEigenVectors.length; i++) {
			double[] value = new double[tEigenVectors[0].length];
			value = tEigenVectors[i];
			eigenMap.put(eigenvalueArray[i], value);
		}

		// 求特征总和
		for (int i = 0; i < n; i++) {
			total += eigenvalueArray[i];
		}
		// 选出前几个主成分
		double temp = 0;
		int principalComponentNum = 0;// 主成分数
		List<Double> plist = new ArrayList<Double>();// 主成分特征值
		for (double key : eigenMap.keySet()) {
			if (temp / total <= threshold) {
				temp += key;
				plist.add(key);
				principalComponentNum++;
			}
		}
		System.out.println("\n" + "当前阈值: " + threshold);
		System.out.println("取得的主成分数: " + principalComponentNum + "\n");

		// 往主成分map里输入数据
		for (int i = 0; i < plist.size(); i++) {
			if (eigenMap.containsKey(plist.get(i))) {
				principalMap.put(i, eigenMap.get(plist.get(i)));
			}
		}

		// 把map里的值存到二维数组里
		double[][] principalArray = new double[principalMap.size()][];
		Iterator<Entry<Integer, double[]>> it = principalMap.entrySet()
				.iterator();
		for (int i = 0; it.hasNext(); i++) {
			principalArray[i] = it.next().getValue();
		}

		Matrix principalMatrix = new Matrix(principalArray);
		System.out.println("主成分矩阵维度:" + principalMatrix.getRowDimension() + "X" + principalMatrix.getColumnDimension());
		return principalMatrix;
	}

	/**
	 * 矩阵相乘
	 * @param primary		原始二维数组
	 * @param matrix		主成分矩阵
	 * @return result 		结果矩阵
	 * @throws IOException 
	 */
	public Matrix getResult(Map<String,List<Double>> trainMap, Matrix matrix) throws IOException {
		PrintWriter resultWriter = new PrintWriter(new FileWriter(new File
								("C:/Users/Administrator/Desktop/DataCastle/测试集/降维矩阵.csv"))) ;
		int column = trainMap.entrySet().iterator().next().getValue().size() ; // 中心特征矩阵的列数
		int row = trainMap.size() ; // 行数
		double[][] train_Matrix = new double[row][column];
		
		Iterator<Entry<String, List<Double>>> it = trainMap.entrySet().iterator();
		int count = 0 ;
		while(it.hasNext()){
			Entry<String, List<Double>> entry = it.next() ;
			List<Double> list = entry.getValue() ;
			for(int i=0; i<list.size(); i++){
				train_Matrix[count][i] = list.get(i) ;
			}
			count ++ ;
		}
		printMatrix("C:/Users/Administrator/Desktop/DataCastle/测试集/原始矩阵.csv", train_Matrix); 
		
		
		Matrix primaryMatrix = new Matrix(train_Matrix);
		System.out.println("维度：" + matrix.getRowDimension() + " * " + matrix.getColumnDimension());
//		matrix.print(matrix.getRowDimension(),  matrix.getColumnDimension()) ;
		Matrix result = primaryMatrix.times(matrix.transpose());
		
		int row2 = result.getRowDimension() ;
		int column2 = result.getColumnDimension() ;
		result.print(resultWriter, row2, column2);  // 将降维后的新矩阵写入文件
		resultWriter.flush();
		resultWriter.close();
		return result;
	}
	
	/*
	 * 	打印矩阵
	 */
	private static void printMatrix(String writerDir,double[][] matrix) throws IOException {
		FileWriter writer = new FileWriter(new File(writerDir)) ;
		int row = matrix.length ;  //行
		System.out.println("矩阵有" + row + "行...");
		int column = matrix[0].length ; // 列
		System.out.println("矩阵有" + column + "列...");
		for(int i=0; i<row; i++){
			for(int j=0; j<column; j++){
				writer.write(matrix[i][j] + "\t") ;
			}
			writer.write("\n") ;
			writer.flush();
		}
		writer.close();
	}
}
