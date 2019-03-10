/**
 * 
 */
package hello.programmer.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


/**
 * @see <a href="http://commons.apache.org/proper/commons-io/javadocs/api-2.4/org/apache/commons/io/FileUtils.html">org.apache.commons.io.FileUtils</a>
 */
public class FileUtil extends FileUtils{
	
	private static Logger logger = Logger.getLogger(FileUtil.class.getName());
	/**
	 * 
	 * @param filename - 文件路径
	 * @return the list of Strings, never null
	 */
	public static List<String> readLines(String filename){
		try {
			return FileUtils.readLines(new File(filename));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 调用代码如下：
	 * <pre class="code">{@code
	 * FileUtil.readLines("F:\\auth.txt",new LinesFilter(){
			public boolean filter(String line){
				return !line.equals("");
			}
		})
	    </code>
	    }</pre>
	 * @param filename - 文件路径
	 * @param linesFilter - 过滤行条件
	 * @return the list of Strings, never null
	 */
	public static List<String> readLines(String filename,LinesFilter linesFilter){
		try {
			List<String> lines = FileUtils.readLines(new File(filename));
			List<String> _lines = new ArrayList<String>(lines);
			for(String line : _lines){
				if(!linesFilter.filter(line)){
					lines.remove(line);
				}
			}
			return lines;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 文件转成byte数组
	 * @param file - 文件
	 * @return bytes
	 * @throws IOException
	 */
	public static byte[] toByteArray(File file) throws IOException{
		InputStream in = new FileInputStream(file);
		try {
		    byte[] bytesArray = IOUtils.toByteArray(in);
		    return bytesArray;
		} finally {
		    IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * 
	 * @param file - the file to write
	 * @param lines - 要写入的内容,as list
	 * @throws IOException
	 */
	public static void writeLines(File file,List<String> lines) throws IOException{

		FileUtil.writeLines(file, "utf-8", lines, "\n" , true);
	}
	
	/**
	 * 
	 * @param file - the file to write
	 * @param line - 要写入的内容
	 * @throws IOException
	 */
	public static void writeLines(File file,String line) throws IOException{
		List<String> lines = new ArrayList<String>();
		lines.add(line);
		writeLines(file,lines);
	}
	
	/**
	* 创建文件，如果目录不存在，则创建
	* 
	* @param filePath - 文件全路径
	* @return 创建成功返回true
	* @throws IOException
	*/
	public static File createFile(String filePath) throws IOException{
		File file = new File(filePath);
		if(!file.exists()) {  
        	if( !file.getParentFile().exists() ){
        		if(!file.getParentFile().mkdirs()){
        			throw new IOException("创建父目录失败！" + filePath);
        		}
        	}
        	if( !file.createNewFile() ){
        		throw new IOException("文件创建失败！" + filePath);
        	}
        }
		return file;
	}
	
	public static boolean createDirectory(String dir){

	    File directory = new File(dir);

	    //director.mkdir()方法只能创建一级目录，其父级目录必须存在，否则会有异常
	    if (directory.mkdir()) {

	      System.out.println("Success using alternative 1");

	    } 
	    else {

	      //使用mkdirs()方法可以创建多层级目录
	      if (directory.mkdirs()) {

	        System.out.println("Success using alternative 2");

	      }
	      else {

	        System.out.println("Failed using both alternative 1 and alternative 2");

	      }
	    }
	    return true;
	}
	
	/**
	* 创建文本文件，并写入内容，如果目录不存在，则创建
	* 
	* @param fileDirectory - 文件所在目录
	* @param filename - 文件名
	* @param content- 文件内容
	* @throws IOException
	*/
	public static void createFile(String fileDirectory,String filename,String content) throws IOException{
		String filePath = fileDirectory + filename;
		File file = createFile(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(fos, "UTF-8");
		out.write(content);
		out.close();
		fos.close();
	}
	
	public static void genAddSql() throws IOException{
		List<String> lines = readLines("C:\\Users\\T\\Desktop\\新建文件夹\\20180507_1.csv");

		StringBuilder insertSql=new StringBuilder("insert into ast_loan_user " + "(borrower_type,borrower_name,borrower_id,"
				+ "borrower_num,have_marry,have_son," + "address,bank_code,op_time,is_deleted,remark,type,op_name)"
				+ " values");
		List<String> paras=new LinkedList<>();


		for(String line : lines){
			String[] content = line.split("\\|");
			String contracCode = content[9];
			String idcard = content[12];
			if(idcard.toLowerCase().endsWith("x")){
				continue;
			}
			//System.out.println(contracCode+" "+idcard);

			String[] para = new String[]{
					"1",
					"'" + content[11] + "'",
					"'" + content[12] + "'",
					"''",
					"1",
					"1",
					"''",
					"null",
					"'2018-05-07 12:12:12'",
					"0",
					"''",
					"1",
					"'" + "qudian接口同步" + "'",
			};
			paras.add("(" + String.join(",", Arrays.asList(para)) + ")");
		}


		List<String> contentList = new ArrayList<>();

		int batchInsertSize = 1000;
		double sliceTimes = Math.ceil((double)paras.size()/batchInsertSize);
		for(int i=0; i<sliceTimes; i++){
			int start = i*batchInsertSize;
			int end;
			if(i == sliceTimes - 1){//最后一页
				end = paras.size();
			}
			else{
				end = (i + 1)  * batchInsertSize;
			}

			System.out.println(i+" "+start+" "+end);

			String content = insertSql.toString() + String.join(",",paras.subList(start,end)) + ";";
			contentList.add(content);
		}
		System.out.println(paras.size());

		writeLines(new File("C:\\Users\\T\\Desktop\\新建文件夹\\add.sql"), contentList);

	}

	public static void genUpdateSql()throws IOException{
		List<String> contractLines = readLines("C:\\Users\\T\\Desktop\\新建文件夹\\20180507_1.csv");
		Map<String,String> map = new HashMap<>();
		contractLines.stream().forEach(line->{
			String[] content = line.split("\\|");
			map.put(content[11] + content[12],content[9]);
			//System.out.println(content[11] + content[12]);
		});


		List<String> sqllist = new ArrayList<>();
		List<String> lines = readLines("C:\\Users\\T\\Desktop\\新建文件夹\\right_id.txt");

		Set<String> contracts = new HashSet<>();
		for(String line : lines){
			String[] arr = line.split("\t");
			String contractCode = map.get(arr[1]+arr[2]);
			//System.out.println(arr[1] + arr[2]);
			if(arr[2].equals("445381199112276338")){
				System.out.println(contractCode);
			}
			String id = arr[0];
			String updateSql = "update ast_loan_info set borrowerid=" + id + ",mainborrowerid=" + id + " where contractCode ='qudian-" + contractCode+"';";
			//System.out.println(updateSql);
			sqllist.add(updateSql);

			contracts.add(contractCode);

		}

		sqllist.sort((a,b) -> a.split("borrowerid=")[1].compareTo(b.split("borrowerid=")[1]));
		System.out.println(contracts.size());
		System.out.println(sqllist.size());

		writeLines(new File("C:\\Users\\T\\Desktop\\新建文件夹\\update_loan.sql"), sqllist);


	}

	public static void wrongproject()throws IOException{
		List<String> names = Arrays.asList(new String[]{"20180502_1.csv","20180502_2.csv","20180502_3.csv"
		,"20180507_1.csv","20180507_2.csv","20180507_3.csv"});
		List<String> wrongContracts = new ArrayList<>();
		for(String name : names){
			List<String> contractLines = readLines("C:\\Users\\BJ0179\\Desktop\\Desktop\\新建文件夹\\wrong\\" + name);
			for(String line : contractLines){
				String[] arr = line.split("\\|");
				String idcard = arr[12];
				System.out.println(idcard);
				if(idcard.indexOf(".") != -1){
					wrongContracts.add(idcard);
				}
			}
		}
		System.out.println(wrongContracts.size());




	}
	
	public static void main(String[] args) throws IOException {

			}






}
