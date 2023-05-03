import javax.servlet.annotation.WebServlet;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBController {

    static {

        //注册依赖jar包（src/main/webapp/WEB-INF/lib）
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static String dbname = "";  //此处填写数据库账号
    static String dbpsw = "";//此处填写数据库密码
    static String dburl = "jdbc:mysql://xxxx.xxxx.cn:3306/lab4?characterEncoding=utf-8";
    //此处填写数据库url，注意，mysql8.0和5.0的版本的填写方式不同，上面是8.0的



    //负责提供获取商品数据的接口
public static void getAllGoods(Map<String, Good> goodsMap) {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
        //Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(dburl, dbname, dbpsw);
        stmt = conn.createStatement();
        String sql = "SELECT * FROM goods";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Good good = new Good();
            good.setGid(rs.getString("gid"));
            //System.out.println(rs.getString("gid"));
            good.setGname(rs.getString("gname"));
            //System.out.println(rs.getString("gname"));
            good.setGinfo(rs.getString("ginfo"));
            //System.out.println(rs.getString("ginfo"));
            good.setPrice(rs.getString("price"));
            good.setImageurl(rs.getString("imageurl"));
            goodsMap.put(good.getGname(), good);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

}






