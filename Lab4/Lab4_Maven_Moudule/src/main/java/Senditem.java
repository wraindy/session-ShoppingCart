import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;


//根据item.html的参数
@WebServlet(urlPatterns = "/Senditem")
public class Senditem extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String gname = req.getParameter("gname");
        System.out.println("当前用户正在访问的商品："+gname+"------------------");

        Good item = Cart.goods.get(gname);

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String htmlString =
                "<img src=\"" + item.getImageurl() + "\" class=\"itemImg\" id=\"itemsrc\">" +
                "<div class=\"itemInfobox\">" +
                "<h2 id=\"itemName\">" + item.getGname() + "</h2>" +
                "<p id=\"itemInfo\">" + item.getGinfo() + "</p>" +
                "<p id=\"itemPrice\">" + item.getPrice() + "</p>" +
                "</div>" ;
        out.write(htmlString);

        out.close();
    }


}
