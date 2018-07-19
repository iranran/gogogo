package hello.programmer.common.utils;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**使用sftp协议传输*/
public class SFTPUtil {

	private static final Logger logger = LoggerFactory.getLogger(SFTPUtil.class);
    
	/**传输模式，0覆盖 1重传 2追加*/
	public static final int OVERWRITE = 0;
	public static final int RESUME = 1;
	public static final int APPEND = 2;

	private String host;// 服务器连接ip
	private String username;// 用户名
	private String password;// 密码
	private int port;// 端口号
	private String basePath;// 相对路径
	
	private ChannelSftp sftp = null;
	private Session sshSession = null; 
	
	public SFTPUtil() {
	}

	public SFTPUtil(String host, int port, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}
	public SFTPUtil(String host, int port, String username, String password,String basePath) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.basePath = basePath;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * 通过SFTP连接服务器
	 * @throws Exception
	 * @return void
	 * @author zcs
	 * @time 2017年5月17日 下午3:24:17
	 */
	public void connect() throws Exception {
		try {
//			FTPClient ftp = new FTPClient();
			JSch jsch = new JSch();
//			Object ss=new FTPUtil();
			jsch.getSession(username, host, port);
			
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			
			channel.connect();
			logger.info("============与sftp服务器成功建立通道，服务器地址为：{}:{}===============",host,port);
			sftp = (ChannelSftp) channel;
		} catch (Exception e) {
			this.disconnect();
			logger.info("与sftp服务器成功连接，服务器地址为： " + host + ":" + port,e);
			throw new Exception("与sftp服务器连接失败，服务器地址为： " + host + ":" + port);
		}
	}

	/**
	 *  在n次内尝试连接sftp服务器
	 * @param n
	 * @return boolean
	 * @author zcs
	 * @time 2017年5月21日 下午5:28:56
	 */
	public boolean tryConnect(int n) throws Exception{
		int i = 1;
		long startTime;
		long endTime;
		startTime = System.currentTimeMillis();
		while (i <= n) {
			try {
				this.connect();
				endTime = System.currentTimeMillis();
				long costTime = endTime - startTime;
				logger.info("第【{}】次连接成功，共花费时间为：{}ms-------------------------",i,costTime);
				break;
			} catch (Exception e) {
				if (i < n) {
					logger.error("==========尝试第:{}次连接sftp服务器并失败===================",i,e);
					i++;
				} else {
					this.disconnect();
					endTime = System.currentTimeMillis();
					long costTime = endTime - startTime;
					logger.error("连接失败，共花费时间为：{}ms------------------------",costTime);
					throw new Exception(e);
				}
			}
		}
		return true;
	}

	/**
	 *  关闭连接
	 * @return void
	 * @author zcs
	 * @time 2017年5月17日 下午3:24:06
	 */
	public void disconnect() {
		try {
			if (this.sftp != null) {
				if (this.sftp.isConnected()) {
					this.sftp.disconnect();
				}
			}
		} catch (Exception e) {
			logger.error("关闭sftp连接失败", e);
		}
		try {
			if (this.sshSession != null) {
				if (this.sshSession.isConnected()) {
					this.sshSession.disconnect();
				}
			}
		} catch (Exception e) {
			logger.error("关闭sshSession连接失败", e);
		}
		logger.info("=========sftp服务器成功关闭=============");
	}

	/**
	 *  上传文件,整体传输
	 * @param origin  
	 * @param destiny  目录+文件名
	 * @param monitor
	 * @param mode
	 * @throws Exception
	 * @author zcs
	 * @time 2017年5月17日 下午3:21:52
	 */
	public void uploadAll(String origin,String destiny,SftpProgressMonitor monitor,int mode) throws Exception{
		byte[] bsrc = origin.getBytes("utf-8");
		InputStream in= null;
		try {
			in = new ByteArrayInputStream(bsrc);
			sftp.put(in,destiny,monitor,mode);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	/**
	 *  上传文件，整体传输
	 * @param origin
	 * @param destiny 目录+文件名
	 * @param mode
	 * @throws Exception
	 * @author zcs
	 * @time 2017年5月17日 下午3:23:33
	 */
	public void upload(String origin,String destiny,int mode) throws Exception{
		byte[] bsrc = origin.getBytes("utf-8");
		InputStream in= null;
		try {
			in= new ByteArrayInputStream(bsrc);
			sftp.put(in,destiny,mode);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	/**
	 *  在n次内尝试传输文件，直至成功
	 * @param origin
	 * @param destiny
	 * @param mode
	 * @param tryTimes
	 * @return
	 * @return boolean
	 * @author zcs
	 * @time 2017年5月23日 下午2:18:21
	 */
	public boolean uploadNTimes(String origin,String destiny,int mode,int tryTimes) throws Exception{
		long startTime = System.currentTimeMillis();
		int k=1;
		while(k<=tryTimes){
			try{
				this.upload(origin, destiny,mode);
				long endTime = System.currentTimeMillis();
				long costTime = endTime - startTime;
				logger.info("第【{}】次传输成功，共花费时间为：{} ms----------------------",k,costTime);
				break;
			}catch(Exception e){
				if(k<tryTimes){
					logger.info("尝试第:{}次传输数据并失败-----------------",k);
					k++;
				}else{
					long endTime = System.currentTimeMillis();
					long costTime = endTime - startTime;
					logger.error("传输失败，共花费时间为：{} ms-----------------------",costTime);
					throw new Exception(e);
				}
			}
		}
		return true;
	}
	/**
	 *  上传文件 部分传输
	 * @param origin
	 * @param destiny
	 * @param partSize
	 * @param mode
	 * @throws Exception
	 * @return boolean
	 * @author zcs
	 * @time 2017年5月17日 下午8:16:24
	 */
	public boolean uploadByPart(String origin,String destiny,int partSize,int mode) throws Exception{
		byte[] temp = new byte[partSize*1024];
		byte[] bsrc = origin.getBytes("utf-8");
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new ByteArrayInputStream(bsrc);
			Integer read = null;
			if(sftp!= null){
				os = sftp.put(destiny,mode);
				if(os!=null){
					int i=0;
					do {
		                read = is.read(temp, i*temp.length, (i+1)*temp.length);
		                if (read > 0) {
		                	os.write(temp, 0, read);
		                }
		                os.flush();
		                i++;
		            } while (read >= 0);
				}
			}
			return !(read>=0);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
	
	public String download(String destiny) throws Exception{
		InputStream is = null;
		try {
			sftp.get(destiny);
			return IOUtils.toString(is, "UTF-8");
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	public String downloadNTimes(String destiny,int tryTimes) throws Exception{
		long startTime = System.currentTimeMillis();
		int k=1;
		String dataStr=null;
		while(k<=tryTimes){
			try{
				dataStr=download(destiny);
				long endTime = System.currentTimeMillis();
				long costTime = endTime - startTime;
				logger.info("第【{}】次传输成功，共花费时间为：{} ms----------------------",k,costTime);
				break;
			}catch(Exception e){
				if(k<tryTimes){
					logger.info("尝试第:{}次传输数据并失败-----------------",k);
					k++;
				}else{
					long endTime = System.currentTimeMillis();
					long costTime = endTime - startTime;
					logger.error("传输失败，共花费时间为：{} ms-----------------------",costTime);
					throw new Exception(e);
				}
			}
			if(dataStr!=null){
				return dataStr;
			}
		}
		return dataStr;
	}
	

	/**
	 * 如果不存在，在相对路径下创建目录
	 * @author 张伯文
	 * @date 2017年8月29日下午10:13:51
	 * 
	 * @param folderName
	 * @return 是否成功创建目录
	 */
	public boolean createFolderIfNotExist(String folderName){
		boolean isHasFolder=false;
		try {
			sftp.ls(basePath+folderName);
			isHasFolder=true;
		} catch (SftpException e) {
//			e.printStackTrace();
		}
		
		if(isHasFolder){
			return true;
		}else{
			try {
				sftp.mkdir(basePath+folderName);
				return true;
			} catch (SftpException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
