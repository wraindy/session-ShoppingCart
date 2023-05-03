import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//过滤网页：详情页、购物车页
//过滤调试功能：查看购物车/s，删除用户/r、执行购物车操作/Cart、查看商品详情/Senditem、请求购物车/SendCart
@WebFilter(urlPatterns = {"/cart.html", "/item.html","/s","/r","/Cart","/Senditem","/SendCart"})
public class MyFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter已启动...");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        //若是新用户浏览，为其设置session
        if(session.getAttribute("User") == null||session.getAttribute("CartSession") == null){

            //购物车，存商品名字和商品数量
            Map<String, String> cart = new LinkedHashMap<String, String>();
            session.setAttribute("CartSession",cart);

            //没有登录与注册服务，改用User属性作为是否登录的标志，统一为”test”
            session.setAttribute("User","test");


            //设置session存活时间：500秒（即500秒内没有和服务器交互则session失效，但是用户的cookie存活时间是直到浏览器关闭）
            session.setMaxInactiveInterval(500);

            System.out.println("检测到用户请求访问资源，但该用户未登录或无购物车session，已给与登录身份...");

            //使其登录后重定向至商城主页
            resp.sendRedirect(req.getContextPath() + "/index.html");

            System.out.println("已令该用户重定向至商城主页...");

        } else {


            // session中有“cart”属性，继续处理请求
            filterChain.doFilter(servletRequest, servletResponse);

        }
    }

    public void destroy() {

    }
}
