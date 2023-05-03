import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

//增删查改购物车、结账付款
@WebServlet(urlPatterns = "/Cart",loadOnStartup = 0)
public class Cart extends HttpServlet {


    //缓存数据库
    public static Map<String, Good> goods = new LinkedHashMap<String, Good>();
    static {

        //服务器启动时，从数据库获取商品数据放到goods中，作为缓存
        DBController.getAllGoods(goods);
        System.out.println("正在从数据库中获取商品------------------------------------------------------");
        for (Map.Entry<String, Good> entry : goods.entrySet()) {
            String key = entry.getKey();
            Good value = entry.getValue();
            System.out.println("Key: " + key);
            System.out.println("Gid: " + value.getGid());
            System.out.println("Gname: " + value.getGname());
            System.out.println("Ginfo: " + value.getGinfo());
            System.out.println("Price: " + value.getPrice());
            System.out.println("Imageurl: " + value.getImageurl());
            System.out.println();
            System.out.println();
        }
        System.out.println("商品内容获取完毕------------------------------------------------------");
    }

    //对于购物车的更新，采用get的方式提交请求
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //务必设置编码格式，否则服务器打印乱码（/s和/r调试功能）
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        //从get/post参数中获取操作购物的行为参数、操作商品名
        String parameter = req.getParameter("action");
        String gname = req.getParameter("gname");
        //String num = req.getParameter("num");

        //获取用户存储在服务器的Session信息，准备修改
        HttpSession session = req.getSession();
        Map<String, String> cartSession = (Map<String, String>) session.getAttribute("CartSession");

        System.out.println("检测到操作购物车的请求...");

        if ("empty".equals(parameter)){
            //清空购物车，但没有退出用户登录√

            session.removeAttribute("CartSession");
            session.setAttribute("CartSession",new LinkedHashMap<String, String>());

            System.out.println("用户请求清空购物车，已处理...");

        } else if ("update".equals(parameter)) {
            //若用户调整某个商品数量(1---9998)

            String count = req.getParameter("count");
            if(cartSession.containsKey(gname)){

                int tmp = Integer.parseInt(count);
                cartSession.put(gname,Integer.toString(tmp));

                System.out.println("用户请求更新购物车商品数量："+gname+"   数量："+count+"    已添加完毕...");

            }else{
                //保持健壮性，如果用户非法输入
                System.out.println("该用户非法修改购物车数量，服务器不做处理...");
            }

        } else if ("delete".equals(parameter)) {
            //若用户删除某个商品（购物车点击“删除”按钮，删除某个商品）

            if(cartSession.containsKey(gname)){

                cartSession.remove(gname);
                System.out.println("用户在购物车请求删除："+gname+"   已删除...");

            }else {
                System.out.println("用户请求删除不存在的商品："+gname+"  删除失败...");
            }



        } else if ("add".equals(parameter)) {
            //若用户在商城主页将商品加入购物车

            //存在两种添加情况
            //1、用户再次将商品加入购物车
            //2、用户之前没有将这个商品加入购物车
            if(cartSession.containsKey(gname)){
                int nums = Integer.parseInt(cartSession.get(gname));
                nums++;
                cartSession.put(gname,Integer.toString(nums));
            }else {
                cartSession.put(gname,"1");
            }
            
            System.out.println("用户请求在主页将商品添加至购物车："+gname+"   添加完毕...");
        }else if ("checkout".equals(parameter)) {
            //用户点击结账，作用同清空购物车

            session.removeAttribute("CartSession");
            session.setAttribute("CartSession",new LinkedHashMap<String, String>());
            session.setAttribute("Count",0);

            //以下方法是同步，无法让用户跳转回主页
            //resp.sendRedirect("/index.html");

            System.out.println("用户请求结账，已处理...");

            //服务器内输出该用户的账单
            List<TmpCart> tmpCartList = new ArrayList<TmpCart>();
            for(Map.Entry<String,String> entry : cartSession.entrySet()){

                TmpCart tmpCart = new TmpCart();
                tmpCart.gname = entry.getKey();
                tmpCart.gnum = entry.getValue();
                tmpCart.gprice = Cart.goods.get(tmpCart.gname).getPrice();
                tmpCart.gimgsrc = Cart.goods.get(tmpCart.gname).getImageurl();
                tmpCartList.add(tmpCart);

            }

            if(tmpCartList.isEmpty()){

                System.out.println("该用户购物车为空,无需结账...");

            }else {
                int total = 0;
                Iterator<TmpCart> iterator = tmpCartList.iterator();
                while (iterator.hasNext()){
                    TmpCart next = iterator.next();
                    System.out.println("商品："+next.gname);
                    System.out.println("数量："+next.gnum);
                    System.out.println("单价："+next.gprice+"元");
                    System.out.println();
                    total+=Integer.parseInt(next.gnum)*Integer.parseInt(next.gprice);
                }
                System.out.println("总价："+total);
            }

            System.out.println("--------------------------------------------------");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        this.doGet(req, resp);

    }
}
