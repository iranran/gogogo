package hello.programmer.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil extends FileUtils{
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public final static String CHAR_SET_UTF8 = "UTF-8";

	public static String getFileStr(String path, String charSet) throws Exception {
//		File file = new File(path);
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		Exception exception=null;
		try {
//			reader = new BufferedReader(new FileReader(file));
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, charSet);
			reader = new BufferedReader(isr);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(new String(line)).append("\r\n");
			}
			reader.close();
		} catch (Exception e) {
			logger.error("读取文件异常", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		
		return sb.substring(0, sb.length()-"\r\n".length());
	}

	/**
	 * 传入文件名以及字符串, 将字符串信息保存到文件中
	 * 文件存在则覆盖
	 * 文件夹不存在则异常
	 * @param path
	 * @param str
	 * @throws Exception 
	 */
	public static void textToFile(final String path, String str, String charSet) throws Exception {
		OutputStreamWriter osw = null;
		Exception exception=null;
		try {
			FileOutputStream fos = new FileOutputStream(path);
			osw = new OutputStreamWriter(fos, charSet);
			osw.write(str);
			osw.flush();
		} catch (IOException e) {
			logger.error("读取文件异常", e);
		} finally {
			IOUtils.closeQuietly(osw);
		}
	}
	/**
	 * 传入文件名以及字符串, 将字符串信息保存到文件中
	 * 文件存在则覆盖
	 * 文件夹不存在则创建
	 * @author 张伯文
	 * @date 2017年9月13日上午11:38:28
	 * 
	 * @param folderPath
	 * @param fileName
	 * @param str
	 * @param charSet
	 * @throws Exception
	 */
	public static void textToFile(String folderPath,final String fileName, String str, String charSet) throws Exception {
		FileUtil.createFolderIfNotExixtException(folderPath);
		FileUtil.textToFile(folderPath+"/"+fileName, str, charSet);
	}
	public static void deleteAllFileException(String folderPath) throws Exception{
		File file = new File(folderPath);
		String[] fileList=file.list();
		for(String fileName:fileList){
			deleteFileException(folderPath+"/"+fileName);
		}
		
	}
	public static boolean deleteAllFile(String folderPath){
		try{
			FileUtil.deleteAllFileException(folderPath);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public static boolean deleteFile(String filePath) {
		boolean isSuccess = false;
		File file = new File(filePath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			isSuccess = file.delete();
		} else {
			isSuccess = true;
		}
		return isSuccess;
	}
	public static void deleteFileException(String filePath) throws Exception {
		boolean isSuccess=deleteFile(filePath);
		if(!isSuccess){
			throw new Exception("文件删除失败，path="+filePath);
		}
	}
	public static boolean isFileExist(String filePath){
		File file=new File(filePath);
		return file.exists();
	}
	public static boolean createFolderIfNotExist(String folderPath){
		File file=new File(folderPath);
		if(file.exists()){
			return true;
		}else{
			return file.mkdirs();
		}
	}
	public static void createFolderIfNotExixtException(String folderPath) throws Exception{
		boolean isSuccess=createFolderIfNotExist(folderPath);
		if(!isSuccess){
			throw new Exception("文件夹创建失败，path="+folderPath);
		}
	}
	
	/**
	 * 获取目录中的文件数量
	 * 路径错误则异常
	 * @author 张伯文
	 * @date 2017年9月13日上午11:46:38
	 * 
	 * @param folderPath
	 * @return
	 */
	public static int getFolderFileCount(String folderPath) {
		File file=new File(folderPath);
		return file.list().length;
	}
	
	public static int uploadNanJingData(String uploadBatPath,String folderPath) throws Exception {
		long waitTime=1000*60*10;
		$.log("【天津所】准备上传，超时ms设置为"+waitTime);
		if(uploadBatPath.endsWith(".sh")){//如果是linux，用bash执行
			uploadBatPath="bash "+uploadBatPath;
		}
		$.log("【天津所】cmd:"+uploadBatPath);
		Process process=Runtime.getRuntime().exec(uploadBatPath);
		long curMs=System.currentTimeMillis();
		while(FileUtil.getFolderFileCount(folderPath)>0){
			if(System.currentTimeMillis()-curMs<waitTime){
				$.log("【天津所】等待上传完成"+(System.currentTimeMillis()-curMs));
				Thread.sleep(3000);
			}else{//等待超时，返回
				$.log("【天津所】等待超时，尝试杀掉线程");
				process.destroy();//杀掉线程
				FileUtil.deleteAllFileException(folderPath);
				return -1;
			}
		}
		return 0;

	}
	
	private static final String DEFAULT_ENCODING = "GBK";//编码  
	private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB 
	public static String readInfoStream(InputStream input) throws Exception {  
	    if (input == null) {  
	        throw new Exception("输入流为null");  
	    }  
	    //字节数组  
	    byte[] bcache = new byte[2048];  
	    int readSize = 0;//每次读取的字节长度  
	    int totalSize = 0;//总字节长度  
	    ByteArrayOutputStream infoStream = new ByteArrayOutputStream();  
	    try {  
	        //一次性读取2048字节  
	        while ((readSize = input.read(bcache)) > 0) {  
	            totalSize += readSize;  
	            if (totalSize > PROTECTED_LENGTH) {  
	                throw new Exception("输入流超出50K大小限制");  
	            }  
	            //将bcache中读取的input数据写入infoStream  
	            infoStream.write(bcache,0,readSize);  
	        }  
	        return infoStream.toString(DEFAULT_ENCODING);  
	    } catch (IOException e1) {  
	        throw new Exception("输入流读取异常");  
	    } finally {  
	        IOUtils.closeQuietly(infoStream);
	    }  
	}  
	
	public static void main(String[] args)throws Exception {
//		try{
//			FileUtil.deleteAllFileException("C:\\nanjingData\\bin\\data");
//		}catch(Exception e){
//			e.printStackTrace();
//		}

		try {
//			Runtime.getRuntime().exec("C:\\Users\\Administrator\\Desktop\\170901南京所\\fae-upload\\upload.bat");
//			FileUtil.uploadNanJingData("C:\\Users\\Administrator\\Desktop\\170901南京所\\fae-upload\\upload.bat", 
//					"C:\\Users\\Administrator\\Desktop\\170901南京所\\fae-upload");
			Process process=Runtime.getRuntime().exec("cmd /c C:\\Users\\Administrator\\Desktop\\170901南京所\\fae-upload\\upload.bat");
//			$.log(readInfoStream(process.getInputStream()));
//			$.log(process.waitFor());
//			;
//			$.log("finish");
//			Process process=Runtime.getRuntime().exec("C:\\Users\\Administrator\\Desktop\\170901南京所\\fae-upload\\note.exe");
//			process.destroy();//杀掉线程
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		Runtime.getRuntime().exec("‪C:\\nanjingData\\bin\\Notepad3.exe");

	}
	
}
