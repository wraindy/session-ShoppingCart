var payLinkbtn = document.getElementsByClassName('payLink');
var cnt = "";
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
            payLinkbtn[0].innerHTML = "查看购物车" + cnt;
        }
    };
}

//用户登录后，每次打开网页都会刷新购物车小数字
window.onload = function() {
    updateCnt();
}


//用正则表达式获取url中的参数gname
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}



//将gname传给后台，后台返回一个HTML片段，将其插入到页面中
var gname = getParameterByName('gname');
var xhr = new XMLHttpRequest();
xhr.open("GET", "http://localhost:8080/Senditem?gname=" + gname, true);
xhr.send();
xhr.onreadystatechange = function() {
    if (xhr.readyState == 4 && xhr.status == 200) {
        var itemDiv = document.getElementById("item");
        itemDiv.innerHTML = xhr.responseText;
    }
};

