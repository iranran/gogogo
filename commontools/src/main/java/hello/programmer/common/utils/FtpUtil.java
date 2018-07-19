package hello.programmer.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;

public class FtpUtil {
	 private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
	private String host;// 服务器连接ip
	private String username;// 用户名
	private String password;// 密码
	private int port;// 端口号
	private String basePath;// 相对路径

	// public static boolean downFile(String url, int port, String username,
	// String password, String remotePath,
	// String fileName, String localPath) {
	// boolean success = false;
	// FTPClient ftp = new FTPClient();
	// try {
	// int reply;
	// ftp.connect(url, port);
	// // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
	// ftp.login(username, password);// 登录
	// reply = ftp.getReplyCode();
	// if (!FTPReply.isPositiveCompletion(reply)) {
	// ftp.disconnect();
	// return success;
	// }
	// ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
	// FTPFile[] fs = ftp.listFiles();
	// for (FTPFile ff : fs) {
	// if (ff.getName().equals(fileName)) {
	// File localFile = new File(localPath + "/" + ff.getName());
	//
	// OutputStream is = new FileOutputStream(localFile);
	// ftp.retrieveFile(ff.getName(), is);
	// is.close();
	// }
	// }
	//
	// ftp.logout();
	// success = true;
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (ftp.isConnected()) {
	// try {
	// ftp.disconnect();
	// } catch (IOException ioe) {
	// }
	// }
	// }
	// return success;
	// }

	public FtpUtil(String host, String username, String password, int port, String basePath) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.basePath = basePath;
	}

	public FTPClient getFTPClient() {
		return getFTPClient(host, password, username, port);
	}

	public boolean makDir(String refFtpPath) {
		return makDir(username, password, basePath + "/" + refFtpPath, host, port, "utf-8");
	}

	public String readFile(String refFtpPath, String fileName, String charSet) {
		return readFile(username, password, basePath + "/" + refFtpPath, host, port, fileName, charSet);
	}

	public boolean upload(String refFtpPath, String fileName, String fileContent, String charSet) {
		return upload(basePath + "/" + refFtpPath, fileName, username, password, host, port, fileContent, charSet);
	}

	public static FTPClient getFTPClient(String ftpHost, String ftpPassword, String ftpUserName, int ftpPort) {
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				 logger.info("未连接到FTP，用户名或密码错误。");
				ftpClient.disconnect();
			} else {
				 logger.info("FTP连接成功。");
			}
		} catch (SocketException e) {
			e.printStackTrace();
			// logger.info("FTP的IP地址可能错误，请正确配置。");
		} catch (IOException e) {
			e.printStackTrace();
			// logger.info("FTP的端口错误,请正确配置。");
		}
		return ftpClient;
	}

	public static boolean makDir(String ftpUserName, String ftpPassword, String ftpPath, String ftpHost, int ftpPort,
			String charSet) {
		boolean isSuccess = false;
		FTPClient ftpClient = null;
		try {
			ftpClient = getFTPClient(ftpHost, ftpPassword, ftpUserName, ftpPort);
			ftpClient.setControlEncoding(charSet); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			// ftpClient.changeWorkingDirectory(ftpPath);
//			ftpClient.isAvailable()
//			ftpClient.
//			boolean dd=ftpClient.makeDirectory(ftpPath+3);
//			dd=ftpClient.makeDirectory(ftpPath+3);
//			dd=ftpClient.makeDirectory(ftpPath+3);
//			FTPFile[] ls=ftpClient.listDirectories(ftpPath);
			try{
				boolean ret=ftpClient.changeWorkingDirectory(ftpPath);
				if(!ret){//不存在则创建
					isSuccess = ftpClient.makeDirectory(ftpPath);
				}else{//存在返回成功
					return true;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
//			ftpClient.listDirectories(parent)

//			isSuccess = ftpClient.makeDirectory(ftpPath);
			
			
			
			
			
			// in = ftpClient.retrieveFileStream(fileName);
		} catch (Exception e) {
			// logger.error("ftp读取失败，path=" + ftpPath);
			// e.printStackTrace();
			// return isSuccess;
		} finally {
			try {
				if (ftpClient != null) {
					ftpClient.disconnect();
				}
				// if (in != null) {
				// in.close();
				// }
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return isSuccess;
	}

	public static String readFile(String ftpUserName, String ftpPassword, String ftpPath, String ftpHost, int ftpPort,
			String fileName, String charSet) {
		// boolean isSuccess=false;
		StringBuffer resultBuffer = new StringBuffer();
		// FileInputStream inFile = null;
		InputStream in = null;
		FTPClient ftpClient = null;
		// logger.info("开始读取绝对路径" + ftpPath + "文件!");
		try {
			try {
				ftpClient = getFTPClient(ftpHost, ftpPassword, ftpUserName, ftpPort);
				ftpClient.setControlEncoding(charSet); // 中文支持
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				ftpClient.changeWorkingDirectory(ftpPath);
	//			FTPFile[] ls=ftpClient.listFiles();
				in = ftpClient.retrieveFileStream(fileName);
			} catch (Exception e) {
				// logger.error("ftp读取失败，path=" + ftpPath);
				// e.printStackTrace();
				return null;
			}
			if (in != null) {
				InputStreamReader inputStreamReader=null;
				try {
					inputStreamReader = new InputStreamReader(in, charSet);
	
				} catch (Exception e1) {
					try {
	
						if (inputStreamReader != null) {
							inputStreamReader.close();
						} else {
							if (in != null) {
								in.close();
							}
						}
					} catch (Exception e) {
					}
					return null;
				}
				BufferedReader br = new BufferedReader(inputStreamReader);
				
	//			inputStreamReader.
				String data = null;
				try {
					while ((data = br.readLine()) != null) {
						resultBuffer.append(data).append("\r\n");
					}
				} catch (Exception e) {
					// logger.error("文件读取错误。");
					// e.printStackTrace();
					return null;
				} finally {
					try {
						if (ftpClient != null) {
							ftpClient.disconnect();
						}
						IOUtils.closeQuietly(in);
						IOUtils.closeQuietly(br);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}
			} else {
				// logger.error("in为空，不能读取。");
				return null;
			}
			if (resultBuffer.length() > 0) {
				return resultBuffer.toString();
			} else {
				return null;
			}
		} finally {
			if (ftpClient != null) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					//do nothing
				}
			}
		}

	}

	public static boolean upload(String ftpPath, String fileName, String ftpUserName, String ftpPassword,
			String ftpHost, int ftpPort, String fileContent, String charSet) {
		FTPClient ftpClient = null;
		boolean isSuccess = false;
		ByteArrayInputStream tInputStringStream = null;
		try {

			ftpClient = getFTPClient(ftpHost, ftpPassword, ftpUserName, ftpPort);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			tInputStringStream = new ByteArrayInputStream(fileContent.getBytes(charSet));
            logger.info("【ftp文件上传】开始上传路径{}下文件{}",ftpPath,fileName);
			for (int i = 1; i <= 5; i++) {// 重试5次
				try {
					boolean ret = ftpClient.storeFile(ftpPath + "/" + fileName, tInputStringStream);
					if (ret) {
						isSuccess = true;
						logger.info("【ftp文件上传】上传成功");
						break;
					}
					logger.info("【ftp文件上传】上传失败，第{}次上传失败，10s后重试",i);
					Thread.sleep(10);
				} catch (Exception e) {
					logger.info("【ftp文件上传】上传失败，第{}次上传失败，原因{}",e.getMessage());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ftpClient != null) {
					ftpClient.disconnect();
				}
				if (tInputStringStream != null) {
					tInputStringStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	public static void main(String[] argc) {

		// FTPClient ftpClient=FtpUtil.getFTPClient("114.112.56.250",
		// "Sl,LYFSgR8a!", "test_crms_jlc", 21);
		// boolean ret=FtpUtil.upload("/jlc/projectCheckTest", "aas.txt",
		// "test_crms_jlc", "Sl,LYFSgR8a!", "114.112.56.250", 21,
		// "helloworldaa爱爱爱1", "utf-8");
		// $.log("upload="+ret);
		//
		// String data = FtpUtil.readFile("test_crms_jlc", "Sl,LYFSgR8a!",
		// "/jlc/projectCheckTest",
		// "114.112.56.250", 21, "aas.txt", "utf-8");
		// $.log("data=" + data);
		//
		//
		// boolean ret2=FtpUtil.makDir("test_crms_jlc", "Sl,LYFSgR8a!",
		// "/jlc/projectCheckTest/哈哈",
		// "114.112.56.250", 21, "utf-8");
		// $.log("ret2=" + ret2);

		FtpUtil ftpUtil = new FtpUtil("10.103.27.221", "testftp", "Yinker.com123", 21, "/root/rsd");
		boolean ret4 = ftpUtil.makDir("aa1");
		$.log("ret4=" + ret4);
		boolean ret3 = ftpUtil.upload("aa1", "aas.csv", "aas哈哈哈", "utf-8");
		$.log("ret3=" + ret3);
		String data2 = ftpUtil.readFile("aa1", "aas.csv", "utf-8");
		$.log("data2=" + data2);

		// public FtpUtil(String host, String username, String password, int
		// port, String basePath) {

	}
}
