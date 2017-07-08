/**
 * 
 */
package hello.programmer.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * @author liweizz
 * @Description 压缩和解压缩
 * @date 2014-6-14 上午9:57:44
 */
public class ZipUtils {

	/**
	 * 压缩文件夹 例: zipDiry("E:/dw", "E:/abcdeft.zip");
	 * @param dir - 要压缩的文件夹,或者文件目录
	 * @param targetZip - 目标zip文件
	 * @throws Exception 
	 */
	public static void zip( String dir,String targetZip) throws Exception{
		if( targetZip.lastIndexOf(".zip")==-1){
			targetZip = targetZip + ".zip";
		}
        File tarZip = new File(  targetZip );
        if (!tarZip.exists()){
        	tarZip.createNewFile();
        }
        FileOutputStream fous = new FileOutputStream(tarZip);
        ZipOutputStream zipOut = new ZipOutputStream(fous);
        
        File _file = new File( dir );
        zipFile("", _file, zipOut);
        zipOut.close();
        fous.close();
	}
	
	 private static void zipFile( String parent,File inputFile, ZipOutputStream ouputStream ) throws Exception {
	    	String _p = parent;
	        try {
	            if(inputFile.exists()) {
	                if (inputFile.isFile()) {
	                    FileInputStream IN = new FileInputStream(inputFile);
	                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
	                    ZipEntry entry = new ZipEntry( parent + inputFile.getName() );
	                    //org.apache.tools.zip.ZipEntry
	                    ouputStream.putNextEntry(entry);
	                    // 向压缩文件中输出数据   
	                    int nNumber;
	                    byte[] buffer = new byte[512];
	                    while ((nNumber = bins.read(buffer)) != -1) {
	                        ouputStream.write(buffer, 0, nNumber);
	                    }
	                    // 关闭创建的流对象   
	                    bins.close();
	                    IN.close();
	                } else {
	                    try {
	                        File[] files = inputFile.listFiles();
	                        for (int i = 0; i < files.length; i++) {
	                            zipFile(_p +  inputFile.getName()+"/",files[i], ouputStream);
	                        }
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }
	 
	 
	     /**
	      * 解压缩
	     * @param zippath zip文件地址
	     * @param zipdir 解压后存放地址
	     * @return 所有压缩包中的文件路径
	     * */
	    public static List<String> unzip(String zippath,String zipdir) throws Exception{
	       ZipFile zfile=new ZipFile(zippath);
	       Enumeration<ZipEntry> zList = zfile.getEntries();
	       ZipEntry ze=null;   
	       byte[] buf=new byte[2048];
	       List<String> pathlist=new ArrayList<String>();
	       while(zList.hasMoreElements()){   
	            ze=(ZipEntry)zList.nextElement();          
	           if(ze.isDirectory()){
	               continue;   
	            }
	           //if(checkName(ze.getName()))continue;;
	           OutputStream os=new BufferedOutputStream(new FileOutputStream( FileUtil.createFile(zipdir+ ze.getName())));
	           InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
	           int readLen=0;   
	           while ((readLen=is.read(buf, 0, 2048))!=-1) {   
	                os.write(buf, 0, readLen);   
	            }   
	            is.close();
	            os.close();
	            
	            pathlist.add(zipdir+ze.getName());
	        }   
	        zfile.close();
	        return pathlist;
	    }   
	    
	    /**
	      * 解压缩，直接解压不包含目录
	     * @param zippath zip文件地址
	     * @param zipdir 解压后存放地址
	     * @return 所有压缩包中的文件路径
	     * */
	    public static List<String> unzipNoDirectory(String zippath,String zipdir) throws Exception{
			 ZipFile zfile=new ZipFile(zippath);
		       Enumeration<ZipEntry> zList=zfile.getEntries();
		       ZipEntry ze=null;   
		       byte[] buf=new byte[2048];
		       List<String> pathlist=new ArrayList<String>();
		       while(zList.hasMoreElements()){   
		            ze=(ZipEntry)zList.nextElement();          
		           if(ze.isDirectory()){
		               continue;   
		            }
		           //if(checkName(ze.getName()))continue;;
		           OutputStream os=new BufferedOutputStream(new FileOutputStream( FileUtil.createFile(zipdir+ getRealFileName(ze.getName()))));
		           InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
		           int readLen=0;   
		           while ((readLen=is.read(buf, 0, 2048))!=-1) {   
		                os.write(buf, 0, readLen);   
		            }   
		            is.close();
		            os.close();
		            
		            pathlist.add(zipdir+getRealFileName(ze.getName()));
		        }   
		        zfile.close();
		        return pathlist;
		 }
	    
	    
	    
	    public static File unzipOneFile(String zippath,String zipdir,String filename) throws Exception{
	    	ZipFile zfile=new ZipFile(zippath);
	        Enumeration<ZipEntry> zList=zfile.getEntries();
	        ZipEntry ze=null;   
	        byte[] buf=new byte[2048];
	        File excel = null;
	        while(zList.hasMoreElements()){   
	             ze=(ZipEntry)zList.nextElement();          
	            if(ze.isDirectory()){
	                continue;   
	             }
	            //if(checkName(ze.getName()))continue;;
	            String zeName = getRealFileName(ze.getName());
	            if( !zeName.equals(filename)){
	            	continue;
	            }
	            excel = FileUtil.createFile(zipdir + getRealFileName(zeName));
	            OutputStream os=new BufferedOutputStream(new FileOutputStream(excel));
	            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));   
	            int readLen=0;   
	            while ((readLen=is.read(buf, 0, 2048))!=-1) {   
	                 os.write(buf, 0, readLen);   
	             }   
	             is.close();
	             os.close();
	             
	             break;
	         }   
	         zfile.close();
	         return  excel;
	    }
	    
	    public static String getRealFileName(String absFileName){   
	        String[] dirs=absFileName.split("/");
	       if(dirs.length>1){   
	            return dirs[dirs.length-1];
	        }else{
	        	return absFileName;
	        }
	    }
	    
	    /**
	     * 获取此zip文件中的所有文件地址，此方法不用将zip解压
	     * @param zippath zip文件地址
	     * @return List<filepath>
	     **/
	    public static List<String> getZipFileNameList(String zippath) throws Exception{
	    	ZipFile zfile=new ZipFile(zippath);
	        Enumeration<ZipEntry> zList=zfile.getEntries();
	        ZipEntry ze=null;   
	     
	        List<String> pathlist=new ArrayList<String>();
	        while(zList.hasMoreElements()){   
				ze = zList.nextElement();
	            if(ze.isDirectory())
	                continue;   
	             
	             pathlist.add(ze.getName());
	         }   
	         zfile.close();
	         return pathlist;
	    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		//	ZipUtils.zip("F:/var", "F:/log_2014.zip");
		//	ZipUtils.unzip("F:/log_2014.zip", "F:/usr/");
			System.out.println(ZipUtils.getZipFileNameList("F:/log_2014.zip"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
