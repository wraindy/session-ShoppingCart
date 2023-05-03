# 基于cookie和session的购物车项目



## 前言

这是一份由我完成的JavaWeb大作业。

如有任何疑问，可通过email联系我，备注”GitHub购物车作业咨询“

本文混用”作业“，”项目“，二者为相同含义。

**本项目禁止商用，仅供学习交流使用。**



## 项目学习要求

### 环境部署

JDK---版本1.8（idea可下载）

Tomcat---版本9.0.74（9.0左右即可）

fastjson---版本1.1.34（已在项目路径中：”src/main/webapp/WEB-INF/lib/fastjson-1.1.34.jar“）

MySQL---版本8.0+（可选）

IDEA---版本2023.1（2021之后都可以）

### 前置知识

1、前端三件套

​		重点了解Ajax概念。

2、MySQL的使用（可选）

​		MySQL不一定使用，你可以选择将商品数据放在map中，如：[点击跳转](https://blog.csdn.net/Distance_Jane/article/details/80142799)

3、了解Tomcat的相关概念

4、了解servlet的相关概念

5、了解cookie和session的相关概念

提示，以上内容可以去B站看黑马的视频，强烈推荐：[点击跳转](https://www.bilibili.com/video/BV1Qf4y1T7Hx)



## 作业亮点

1、使用MySQL储存商品数据（可选）

2、后端使用json发送动态购物车数据给前端

3、使用filter做拦截，自动拦截**未登录用户**的访问

4、完整实现购物车的增删改查功能

5、动态实时显示商品数量



## 不足之处

1、本项目并**未实现真正的**登录与注册功能。

​		但是，如果用户第一次访问的页面不是index.html则会强制重定向至index.html，然后服务器赋予cookie，当服务器赋予了cookie之后，即可认为该用户”**已登录**“，当用户访问时没有该cookie，则认为该用户”**未登录**“，可通过清除浏览器关于该站点的cookie，**退出该用户的登录状态**，已经做了调试功能，可以方便你清除cookie，而不用每次都要打开浏览器的设置。

2、”查看购物车“按钮有个数字（表示购物车商品总数量），但是不是真的**动态实时显示商品数量**。

​		因为浏览器的缓存未及时清除，或者因为服务器发送数字是在更新数字**之后**，解决办法就是刷新网页。

3、前端不够优美。

​	因为我不想学前端，反正能完成购物车的功能即可。



## 项目学习指导

### 项目文件介绍

src/main/java存的是实现服务器逻辑的.java文件；src/main/webapp文件存的是前端页面文件。



#### .java文件

Cart---实现购物车增删改查和清空的业务逻辑，从数据库中缓存商品信息

SendLatestCnt---实现动态实时发送当前用户购物车商品数量的功能

TmpCart---辅助SendCart类，是购物车的商品信息，而不是完整的商品信息类（Good才是完整的）



Good---商品信息类

DBController---实现连接数据库并提供获取全部商品信息的函数



MyFilter---实现登录的控制，即cookie、session的分发



SendCart---通过使用fastjson实现服务器动态发送用户购物车的数据给前端展示

Senditem---根据用户post参数，发送不同的商品详情给前端





RemoveSessionDebug---用于调试，访问`localhost:8080/r`可清除当前用户的cookie，解除登录状态

SeeCartDebug---用于调试，访问`localhost:8080/s` ，服务器会返回当前购物车数据给前端展示



#### 前端页面文件

index.html商城主页

item.html商品详情页

cart.html购物车页面

剩下同名的css、js文件则是对应的上述文件。



### 项目分析

1、服务器启动后，首先通过连接数据库（DBController），将商品信息缓存（Cart），至此，前后端商品信息的交互不再使用数据库，也就是说，服务器从启动至关闭，仅仅使用了一次。

2、当用户关闭浏览器后，该用户的cookie会被删除，即sessionID被删除，服务器不会再记得该用户

3、服务器保存的session有500秒的生存期（MyFilter中可见），购物车信息，商品数量都保存在该session中





### 如何改为自己的作业？

小白：只需要改前端页面/商品信息或数量

大神：商城主页并没有采用json发送商品信息，你可以完善一下，后端做个SendGood类，遍历Cart类中的商品缓存，打包成json发送前端（参考我是如何发送购物车内容给cart.html）；前端美化；将本地服务器部署到云服务器中（idea可以直接从远程服务器写项目，你自己琢磨琢磨）

1、如果你真的不会，你就读懂我的逻辑，不一定跟我的环境配置保持一致，重点是了解掌握前后端如何交互数据的，比如说前端→后端是通过get和post请求；后端→前端是通过流（PrintWriter）和json，**这才是这个作业的真正目的**

2、我写这个作业的时候，前端用vscode、后端用idea；当你要找某个函数的时候，可以用vscode的全局搜索

3、服务器使用了fastjson.jar包，不是jdk自带的





### 特别注意

1、如果你有mysql，可以在DBController中修改你的信息，我已做好注释；如果你不会mysql，你就自己想办法换成map，我前面也有链接，[点击跳转 ](https://blog.csdn.net/Distance_Jane/article/details/80142799)；比如说，你直接将商品信息写到static代码块中，Cart访问商品信息的时候你直接从这个代码块所在的类中取就行了。

另外附带一下数据库的建表语句：

```mysql
CREATE TABLE `goods` (
  `gid` int NOT NULL AUTO_INCREMENT,
  `gname` varchar(30) DEFAULT NULL,
  `ginfo` varchar(500) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `imageurl` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```

imageurl就是你的商品图片的url，如果你有服务器，可以直接用服务器的链接，没有的话的就创建个文件夹`src/main/webapp/res`，把图片放到res中，链接改在这里即可。另外，记得调整图片的尺寸哦，我是先调整好图片尺寸为正方形4/500pixel。



2、如果你遇到中文乱码问题，是Tomca9.0版本导致的， 你可以参考我发的参考链接，注意，做好文件备份。

3、我没有修改端口号，默认8080；如果你要部署到服务器上，可以试着修改默认端口号，改为80或443.

4、修改前端页面之后发现页面还是修改之前的样子，原因是浏览器没有清除缓存，注意，这不是指前面所说的用户cookie（此处可能有误）。

5、我已删除我的mysql连接信息+图片url信息，请自行解决。

## 原作业题目

掌握会话及其会话技术，用Cookie对象和Session对象实现购物车功能：

1、商品页面提供商品详情，如图片、单价、详情等；

2、商品添加到购物车后，能在购物车页面查看所添加的商品列表，包括商品名、单价、数量等。



## 参考链接

以下内容是我在完成这个作业的时候参考的内容，希望对你有所帮助：

1、黑马javaweb视频2021版（jsp技术已过时可以不看）：https://www.bilibili.com/video/BV1Qf4y1T7Hx
2、idea如何部署项目到tomcat（仅供参考）：
https://blog.csdn.net/weixin_42566699/article/details/105875632
3、找不到HttpServlet相关包（解决：导入tomcat里面的两个包到项目中即可）：
https://blog.csdn.net/qq_35443962/article/details/103596247
4、Artifact中war和war exploded的区别：
https://blog.csdn.net/qq_41918166/article/details/109490635
5、解决tomcat中文乱码（9.0有效）：
https://blog.csdn.net/sichenss/article/details/123506147
6、阿里fastjson：
https://sourceforge.net/projects/fastjson/files/
7、简单的后端传送json格式至前端正确处理方法
https://blog.csdn.net/weixin_45609535/article/details/122088095