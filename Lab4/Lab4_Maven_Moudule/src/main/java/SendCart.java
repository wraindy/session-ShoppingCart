import com.alibaba.fastjson.JSON;

import javax.management.StandardEmitterMBean;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//当不同用户查看自己的购物车是，发送该用户对应的购物车
@WebServlet(urlPatterns = "/SendCart")
public class SendCart extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取用户的购物车session
        HttpSession session = req.getSession();
        Map<String,String>cart = (LinkedHashMap<String, String>)session.getAttribute("CartSession");   //MyFilter定义了

        if(cart != null){

            //如果用户购物车有东西，循环遍历，获取商品名--商品数量--商品图片src路径,一同放到tmpCartList
            List<TmpCart> tmpCartList = new ArrayList<TmpCart>();
            for(Map.Entry<String,String> entry : cart.entrySet()){

                TmpCart tmpCart = new TmpCart();
                tmpCart.gname = entry.getKey();
                tmpCart.gnum = entry.getValue();
                tmpCart.gprice = Cart.goods.get(tmpCart.gname).getPrice();
                tmpCart.gimgsrc = Cart.goods.get(tmpCart.gname).getImageurl();
                tmpCartList.add(tmpCart);

            }

            if(tmpCartList.isEmpty()){
                System.out.println("该用户购物车为空...");
            }else {

                String jsonString = JSON.toJSONString(tmpCartList);

                resp.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = resp.getWriter();
                writer.write(jsonString);
                writer.close();
                System.out.println("用户请求查看购物车，JSON字符串为："+jsonString);
            }
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
