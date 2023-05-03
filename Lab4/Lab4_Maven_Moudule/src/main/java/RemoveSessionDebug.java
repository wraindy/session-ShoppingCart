import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//用于调试，清除用户的Session
@WebServlet(urlPatterns = "/r")
public class RemoveSessionDebug extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        //先让服务器对应的session失效
        req.getSession().invalidate();
        //再让浏览器对应的cookie失效
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);

        resp.addCookie(cookie);

        //以上操作将用户退出登录
        System.out.println("debug:  已清除Session，用户已退出登录...  ");

        //特别提醒，先设置响应内容的格式再获取打印流，否则中文乱码
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write("这是来自服务器的调试信息...");
        writer.write("<h2>已删除当前浏览器会话的session（即登录信息）</h2>");
        writer.close();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);

    }
}
