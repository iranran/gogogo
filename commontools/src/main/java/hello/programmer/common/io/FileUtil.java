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
	
	public static void main(String[] args) throws IOException{
//		List<String> lines = readLines("C:\\Users\\BJ0179\\Desktop\\41个开户失败用户.csv");
//		for(String line : lines){
//			String[] arr = line.split(",");
//			String userid = arr[1].split("\"")[1];
//			String id = arr[0].split("\"")[1];
//			String um = arr[5].split("\"")[1];
//			//System.out.println(id+" "+um);
//
//			System.out.println("update ast_money_account set unmatched_amount = unmatched_amount - " + um +", asset_out_freeze_amount =asset_out_freeze_amount + "
//			+um +", target_match_amount=0 where id = " + id +";");
//			//System.out.println(userid+" "+amount);
//		}

//		String idcard = "650104196610240028\n" +
//				"411122197007317522\n" +
//				"513902199805054950\n" +
//				"410105197410081022\n" +
//				"320111196606050448\n" +
//				"150426195705270010\n" +
//				"372401197110192724\n" +
//				"330821196301135427\n" +
//				"622322197109020026\n" +
//				"659001199003151231\n" +
//				"440106197511101514\n" +
//				"430105196106181044\n" +
//				"330203196310030023\n" +
//				"440107198803160613\n" +
//				"510211197410051823\n" +
//				"342425197709140010\n" +
//				"320322197606106821\n" +
//				"320621197204242628\n" +
//				"433030197207040468\n" +
//				"440103195808165748\n" +
//				"653021196602260014\n" +
//				"142421197301120021\n" +
//				"650102198512301225\n" +
//				"610321196510040467\n" +
//				"320111196906171225\n" +
//				"412722198209053068\n" +
//				"612129197406240043\n" +
//				"441900198611085946\n" +
//				"622621197403150016\n" +
//				"110102198003190103\n" +
//				"341222198306095538\n" +
//				"620102195403120047\n" +
//				"630104197408281521\n" +
//				"370502196405303626\n" +
//				"142331196606240529\n" +
//				"310109196806146029\n" +
//				"330126196906240741\n" +
//				"42112619890620382X\n" +
//				"362526196001220016\n" +
//				"610402196806141204\n" +
//				"450304195807301540";
//		Arrays.asList(idcard.split("\n")).stream().map(c-> "'"+c+"'").collect(Collectors.toList());
//		String cards = String.join(",",Arrays.asList(idcard.split("\n")).stream().map(c-> "'"+c+"'").collect(Collectors.toList()));
//		System.out.println(cards);
//
//
//		String userids = "009a0b4e0e2146d78217c7e8255c5bb6\n" +
//				"03882f739f0e4a0aa1539df100248f4d\n" +
//				"059040a871ff43a4bd1b1b8ef2f3c507\n" +
//				"0a8b0a631ca344a89645502c0a4f7353\n" +
//				"120a84a6eb5849e3b064c4d2747d751e\n" +
//				"126e5ec83f1c4cb59e67ff32b7a8b0ce\n" +
//				"1c803fc90ddf4ac099eec19cf40c09e4\n" +
//				"2196cf33cbd64fe4ad238e7768149235\n" +
//				"23f98e3fea86469f8387334f1444825a\n" +
//				"250fc18b26184c10bbe3daf84da6d5e4\n" +
//				"2f3bade7f8024132ae715dc481d41f5a\n" +
//				"32dc6c8d898e4c79be58121db17244e5\n" +
//				"4813c81f374c493e9dc0388007202c7a\n" +
//				"493ae95a1f3e406bac10de2a1151dbee\n" +
//				"4c7220b1b64e485ab1b5deaf51348d92\n" +
//				"513afc6715da46ce81a33c72797bf260\n" +
//				"53ba95f0814548c7a490ea3131421353\n" +
//				"55f47d9a8fc64259a10a5d55e870fe55\n" +
//				"599359a14db846c39b9f187de40793a3\n" +
//				"63dcfbeb54c844eba40281c353bf6989\n" +
//				"6b219e784bdf465baa0cfa1f07cac340\n" +
//				"6ed94cc6922344168959e8ea73c0085a\n" +
//				"7f97e6793a094916bffe9e69274c3e88\n" +
//				"84e641827de646a49005a50ffdf9b479\n" +
//				"8a649730dec9490ab33ab70b94651bc4\n" +
//				"a10344684e9345cf9069896b2ae2c5bb\n" +
//				"a86eb4c7634a440f8a66a0dee9a76ecc\n" +
//				"a8f4efcbaacb4db9a382b5c47187837d\n" +
//				"b15105156e2d45d988571376cbcbb4bf\n" +
//				"b1beecad9a4148448e4c45c66044b96c\n" +
//				"b34be6f526fb41d1ace2c8f3208cec19\n" +
//				"b49acc7d058c4d7fa2447f360fb1029f\n" +
//				"b4cc5b9702414bf08b6120ac6ea5f6b2\n" +
//				"b4eafb865a854476b43fcfca3e6cab62\n" +
//				"c77f8c955b0b49bd8f817d0264f4d113\n" +
//				"d3e5311ca56b489983c0b2421f2ef809\n" +
//				"d5ad3826539e49528efb2e8a783f619b\n" +
//				"e44c3a1f9b394d4eaab3a0cc6f04500b\n" +
//				"e77268f6bb5744ed8019b692d20f2250\n" +
//				"f8578a7c13344764a7f9ea468e3dd495\n" +
//				"fdecf7cafb514469ba15552feea8c993\n";
//		Arrays.asList(idcard.split("\n")).stream().map(c-> "'"+c+"'").collect(Collectors.toList());
//		String useridss = String.join(",",Arrays.asList(userids.split("\n")).stream().map(c-> "'"+c+"'").collect(Collectors.toList()));
//		System.out.println(useridss);
//
//
//		String commonUserid = "009a0b4e0e2146d78217c7e8255c5bb6\n" +
//				"03882f739f0e4a0aa1539df100248f4d\n" +
//				"059040a871ff43a4bd1b1b8ef2f3c507\n" +
//				"120a84a6eb5849e3b064c4d2747d751e\n" +
//				"126e5ec83f1c4cb59e67ff32b7a8b0ce\n" +
//				"1c803fc90ddf4ac099eec19cf40c09e4\n" +
//				"250fc18b26184c10bbe3daf84da6d5e4\n" +
//				"32dc6c8d898e4c79be58121db17244e5\n" +
//				"493ae95a1f3e406bac10de2a1151dbee\n" +
//				"53ba95f0814548c7a490ea3131421353\n" +
//				"599359a14db846c39b9f187de40793a3\n" +
//				"63dcfbeb54c844eba40281c353bf6989\n" +
//				"a86eb4c7634a440f8a66a0dee9a76ecc\n" +
//				"b15105156e2d45d988571376cbcbb4bf\n" +
//				"b49acc7d058c4d7fa2447f360fb1029f\n" +
//				"b4eafb865a854476b43fcfca3e6cab62\n" +
//				"c77f8c955b0b49bd8f817d0264f4d113\n" +
//				"d3e5311ca56b489983c0b2421f2ef809\n" +
//				"d5ad3826539e49528efb2e8a783f619b\n" +
//				"fdecf7cafb514469ba15552feea8c993\n";
//		String comm = String.join(",",Arrays.asList(commonUserid.split("\n")).stream().map(c-> "'"+c+"'").collect(Collectors.toList()));
//		System.out.println(comm);



		String db = "213685\n" +
				"213688\n" +
				"213697\n" +
				"215311\n" +
				"215322\n" +
				"215323\n" +
				"215324\n" +
				"220482\n" +
				"220495\n" +
				"220496\n" +
				"220500\n" +
				"220504\n" +
				"220505\n" +
				"220507\n" +
				"220508\n" +
				"220513\n" +
				"220514\n" +
				"220515\n" +
				"220517\n" +
				"221088\n" +
				"221089\n" +
				"221093\n" +
				"221098\n" +
				"221100\n" +
				"505149\n" +
				"211505\n" +
				"211499\n" +
				"211496\n" +
				"221095\n" +
				"1306004\n" +
				"1306014\n" +
				"1320860\n" +
				"1321426\n" +
				"1321765\n" +
				"1321768";
		System.out.println(String.join(",",Arrays.asList(db.split("\n"))));
	}

	//二次修复，1.删除多余的第一次修复的loanuser数据
	//         2.批量更新1800个第一次未覆盖的借款人信息【不能从1步骤里面取】






}
