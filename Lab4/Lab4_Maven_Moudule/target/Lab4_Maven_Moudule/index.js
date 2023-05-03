var cnt = "";
var Cartbtn = document.getElementById("viewCartLink");
var CartLinkbtn = document.getElementsByClassName('CartLink');

//用于更新商品数量（购物车小数字）
function updateCnt() {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/SendLatestCnt", true);
    xhr.send();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var json = JSON.parse(xhr.responseText);
            if(json == null || json == undefined || json == "" || json == 0)
                cnt = "(0)";
            else
                cnt = "("+json+")";
            Cartbtn.innerHTML = "查看购物车" + cnt;
        }
    };
}

//每个商品的点击加入购物车按钮，都添加监听器
for(var i = 0; i <CartLinkbtn.length; i++){
    CartLinkbtn[i].addEventListener('click',updateCnt);
}


//用户登录后，每次打开网页都会刷新购物车小数字
window.onload = function() {
    updateCnt();
}
