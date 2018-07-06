/**
 * @title JdbcUtil
 * @package hello.programmer.common.database.query
 * @description: 简理财
 * @copyright: Copyright (c) 2017
 * @company:北京简便快乐信息技术有限公司
 * @author liwei
 * @date 2018/1/29 8:59
 */
package hello.programmer.common.database.query;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class JdbcUtil {

    // 表示定义数据库的用户名
    private static String USERNAME ;

    // 定义数据库的密码
    private static String PASSWORD;

    // 定义数据库的驱动信息
    private static String DRIVER;

    // 定义访问数据库的地址
    private static String URL;

    // 定义数据库的链接
    private Connection connection;

    // 定义sql语句的执行对象
    private PreparedStatement pstmt;

    // 定义查询返回的结果集合
    private ResultSet resultSet;

    static{
        //加载数据库配置信息，并给相关的属性赋值
        loadConfig();
    }

    /**
     * 加载数据库配置信息，并给相关的属性赋值
     */
    public static void loadConfig() {
        try {
//            InputStream inStream = JdbcUtil.class
//                    .getResourceAsStream("/jdbc.properties");
//            Properties prop = new Properties();
//            prop.load(inStream);
//            USERNAME = prop.getProperty("jdbc.username");
//            PASSWORD = prop.getProperty("jdbc.password");
//            DRIVER= prop.getProperty("jdbc.driver");
//            URL = prop.getProperty("jdbc.url");

            USERNAME = "root";
            PASSWORD = "ni4shui?Ya";
            DRIVER= "com.mysql.jdbc.Driver";
            URL = "jdbc:mysql://10.103.27.142:3306/index_test?&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true";

        } catch (Exception e) {
            throw new RuntimeException("读取数据库配置文件异常！", e);
        }
    }

    public JdbcUtil() {

    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接
     */
    public Connection getConnection() {
        try {
            Class.forName(DRIVER); // 注册驱动
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 获取连接
        } catch (Exception e) {
            throw new RuntimeException("get connection error!", e);
        }
        return connection;
    }

    /**
     * 执行更新操作
     *
     * @param sql
     *            sql语句
     * @param params
     *            执行参数
     * @return 执行结果
     * @throws SQLException
     */
    public boolean updateByPreparedStatement(String sql, List<?> params)
            throws SQLException {
        boolean flag = false;
        int result = -1;// 表示当用户执行添加删除和修改的时候所影响数据库的行数
        pstmt = connection.prepareStatement(sql);
        int index = 1;
        // 填充sql语句中的占位符
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        result = pstmt.executeUpdate();
        flag = result > 0 ? true : false;
        return flag;
    }

    public boolean updateBatch(String sql, List<List<Object>> params)
            throws SQLException {
        boolean flag = false;
        int result = -1;// 表示当用户执行添加删除和修改的时候所影响数据库的行数
        pstmt = connection.prepareStatement(sql);

        // 填充sql语句中的占位符
        if (params != null && !params.isEmpty()) {
            for(List<Object> paramList : params){
                int index = 1;
                for (int i = 0; i < paramList.size(); i++) {
                    pstmt.setObject(index++, paramList.get(i));
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        flag = result > 0 ? true : false;
        return flag;
    }

    /**
     * 执行查询操作
     *
     * @param sql
     *            sql语句
     * @param params
     *            执行参数
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findResult(String sql, List<?> params)
            throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int index = 1;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(i + 1);
                Object cols_value = resultSet.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 释放资源
     */
    public void releaseConn() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        JdbcUtil jdbcUtil = new JdbcUtil();
//        jdbcUtil.getConnection();
//        try {
//            List<Map<String, Object>> result = jdbcUtil.findResult(
//                    "select * from table_fg", null);
//            for (Map<String, Object> m : result) {
//                System.out.println(m);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            jdbcUtil.releaseConn();
//        }
        insert2();
    }

    public static void insert() {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        try {
            Random random = new Random();
            String sql = "insert into idx_test values(?,?,?,?)";


            List<List<Object>> params = new ArrayList<>();
            for(int i=2; i<1010001; i++){
                long t0 = System.currentTimeMillis();
                List<Object> paramList = new ArrayList<>();
                paramList.add(null);
                paramList.add(random.nextInt(i));
                paramList.add(random.nextInt(i) + random.nextInt(10)*random.nextInt(i));
                paramList.add(random.nextInt(i) - random.nextInt(i));

                params.add(paramList);
                if(i % 10000 ==0){
                    boolean r = jdbcUtil.updateBatch(sql, params);
                    System.out.println(i + " 5000个insert耗时=" + (System.currentTimeMillis() - t0));
                    params = new ArrayList<>();
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.releaseConn();
        }
    }


    public static void insert2() {
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection();
        try {
            Random random = new Random();
            String sql = "insert into test2 values(?,?,?)";


            List<List<Object>> params = new ArrayList<>();
            for(int i=1000002; i<2100002; i++){
                long t0 = System.currentTimeMillis();
                List<Object> paramList = new ArrayList<>();
                paramList.add(null);
                paramList.add(random.nextInt(i) + random.nextInt(10)*random.nextInt(i));
                //paramList.add(i%2);
                paramList.add(2);
                params.add(paramList);
                if(i % 10000 ==0){
                    boolean r = jdbcUtil.updateBatch(sql, params);
                    System.out.println(i + " 10000个insert耗时=" + (System.currentTimeMillis() - t0));
                    params = new ArrayList<>();
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.releaseConn();
        }
    }
}
