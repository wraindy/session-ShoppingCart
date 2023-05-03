import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.Map;

import static java.lang.Thread.sleep;

//实时计算购物车商品数量
@WebServlet(urlPatterns = "/SendLatestCnt")
public class SendLatestCnt extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int cnt = 0;
        Map<String, String> cartSession = (Map<String, String>) req.getSession().getAttribute("CartSession");

        for(Map.Entry<String,String> entry : cartSession.entrySet()){
            cnt += Integer.parseInt(entry.getValue());
        }

        String jsonString = JSON.toJSONString(cnt);
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write(jsonString);
        writer.close();
        System.out.println("用户请求查看购物车商品数量："+jsonString);

    }
}
