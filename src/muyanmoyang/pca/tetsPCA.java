package muyanmoyang.pca;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import muyanmoyang.loadData.DataLoad;
import Jama.Matrix;

public class tetsPCA {
	public static void main(String[] args) throws IOException {
//		DataLoad data = new DataLoad() ;
//		PCA pca = new PCA() ;
//		// 加载训练数据
//		Map<String,List<Double>> trainMap = data.loadTrainData("C:/Users/Administrator/Desktop/DataCastle/test_x.csv") ;
//		// 计算训练集矩阵各列的平均值
//		List<Double> averageList = pca.computeDataAdjust(trainMap,"C:/Users/Administrator/Desktop/DataCastle/测试集/各列数据的平均值.csv") ;
//		// 经过特征中心化后的矩阵B
//		Map<String,List<Double>> map_B = pca.computeB(trainMap, averageList,"C:/Users/Administrator/Desktop/DataCastle/测试集/B_matrix.csv") ; // 得到特征中心化的B矩阵
//		// 计算协方差矩阵
//		Double[][] matrix_B = pca.getMatrixFromB(map_B) ;
//		double[][] covariance_matrix = pca.computeCovariance(matrix_B) ;
//		// 计算特征值
//		double[][] eigenvalue_matrix = pca.getEigenvalueMatrix(covariance_matrix) ;
//		// 计算特征向量
//		double[][] eigenvector_matrix = pca.getEigenVectorMatrix(covariance_matrix) ;
//		
//		// 主成分矩阵
//		Matrix principalMatrix = pca.getPrincipalComponent(eigenvalue_matrix, eigenvector_matrix);
//		// 降维后的矩阵
//		Matrix resultMatrix = pca.getResult(trainMap, principalMatrix);
		
//		dataProcess process = new dataProcess() ;
//		process.processNewMatrix("C:/Users/Administrator/Desktop/DataCastle/测试集/降维矩阵.csv",
//					"C:/Users/Administrator/Desktop/DataCastle/测试集/new_降维矩阵.csv");
	}
}
