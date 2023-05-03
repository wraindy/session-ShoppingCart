import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

//用于调试，在服务器端和网页端打印用户的购物车
@WebServlet(urlPatterns = "/s")
public class SeeCartDebug extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //拦截未注册的用户
        if(req.getSession()==null)return;

        //设置输出流且设置响应内容的格式，否则无法解析html标签
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write("这是来自服务器的调试信息...");

        LinkedHashMap<String, String> cart = (LinkedHashMap<String, String>) req.getSession().getAttribute("CartSession");


        System.out.println("-----------------------------------");


        if(cart.isEmpty()){

            System.out.println("当前用户购物车为空");
            writer.write("<h3>当前用户购物车为空</h3>");

        }else {

            System.out.println("当前用户的购物车有：");
            for (Map.Entry<String,String> entry: cart.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("商品："+key+"  ...  数量："+value);
                writer.write("<h3>商品："+key+"  ...  数量："+value+"</h3>");
            }

        }

        writer.close();
        System.out.println("-----------------------------------");

    }
}
