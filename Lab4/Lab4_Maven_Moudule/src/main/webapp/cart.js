const decreaseButtons = Array.from(document.querySelectorAll('a[href="#"]')).filter(a => a.textContent === '减少');
const increaseButtons = Array.from(document.querySelectorAll('a[href="#"]')).filter(a => a.textContent === '增加');
const deleteButtons = Array.from(document.querySelectorAll('.deleteButton'));
const inputs = Array.from(document.querySelectorAll('input[type="text"]'));
const cleanupLink = document.querySelector('#cleanupLink');
const payLink = document.querySelector('#payLink');


//第一次打开网页的时候，异步加载购物车内容
var table = document.getElementById('table');
var xhr = new XMLHttpRequest();
xhr.open('GET', "http://localhost:8080/SendCart", true);
xhr.send();
xhr.onreadystatechange = function () {
  if (xhr.readyState == 4 && xhr.status == 200) {
    var json = JSON.parse(xhr.responseText);
    for (var i = 0; i < json.length; i++) {
      var tr = document.createElement('tr');
      var td1 = document.createElement('td');
      var td2 = document.createElement('td');
      var td3 = document.createElement('td');
      var td4 = document.createElement('td');
      var td5 = document.createElement('td');
      var td6 = document.createElement('td');
      td1.innerHTML = '<img src="' + json[i].gimgsrc + '" class="cartImg">';
      td2.innerHTML = json[i].gname;
      td3.innerHTML = json[i].gprice;
      td4.innerHTML = '<a href="#">减少</a><input type="text" value="' + json[i].gnum + '"><a href="#">增加</a>';
      td5.innerHTML = json[i].gprice * json[i].gnum;
      td6.innerHTML = '<a href="#" class="deleteButton">删除</a>';
      tr.appendChild(td1);
      tr.appendChild(td2);
      tr.appendChild(td3);
      tr.appendChild(td4);
      tr.appendChild(td5);
      tr.appendChild(td6);
      
      //解决办法：https://stackoverflow.com/questions/30913032/failed-to-execute-insertbefore-on-node-the-node-before-which-the-new-node-i
      document.getElementById("totalRow").parentNode.insertBefore(tr, document.getElementById("totalRow"));


      //增加动态增加对应的事件监听器：增加、减少、输入框、删除

      //删除按钮（删除的是一整行商品）
      td6.addEventListener('click', (event) => {
        event.preventDefault();
        const row = event.target.parentElement.parentElement;
        row.remove();
        updateTotalPrice();
      
        const name = row.children[1].textContent;
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/Cart', true);
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhr.send(`action=delete&gname=${name}`);
      });
      
      // 增加按钮
      const increaseButton = td4.children[2];
      increaseButton.addEventListener('click', (event) => {
        event.preventDefault();
        const input = event.target.previousElementSibling;
        if (input.value < 9998) {
          input.value++;
          updateSubtotal(event.target.parentElement.parentElement);
          updateTotalPrice();
        }
      });

      // 减少按钮
      const decreaseButton = td4.children[0];
      decreaseButton.addEventListener('click', (event) => {
        event.preventDefault();
        const input = event.target.nextElementSibling;
        if (input.value > 1) {
          input.value--;
          updateSubtotal(event.target.parentElement.parentElement);
          updateTotalPrice();      
        }
      });
      
      // 输入框值变化
      const input = td4.children[1];
      input.addEventListener('change', (event) => {
        let value = parseInt(event.target.value);
        if (isNaN(value) || value <= 0) {
          value = 1;
        } else if (value >= 9999) {
          value = 9998;
        }
        event.target.value = value;
        updateSubtotal(event.target.parentElement.parentElement);
        updateTotalPrice();
      })

    }
    //获取数据完毕，更新总价
    updateTotalPrice();
  } 
}

//清空购物车选项
cleanupLink.addEventListener('click', (event) => {
if (confirm("确定清空购物车？")) {
  event.preventDefault();
  const rows = Array.from(document.querySelectorAll('tr'));
  rows.forEach((row, index) => {
    if (index > 0 && index < rows.length - 1) {
      row.remove();
    }
  });
  updateTotalPrice();
  var xhr = new XMLHttpRequest();
  xhr.open('GET', "http://localhost:8080/Cart?action=empty", true);
  xhr.send();
  }
});

//更新小计，将商品最新的数量上报服务器（仅当输入框的值变化的时候，不包含删除商品）
function updateSubtotal(row) {

  const price = parseInt(row.children[2].textContent);
  const quantity = parseInt(row.querySelector('input[type="text"]').value);
  const subtotalElement = row.children[4];
  const subtotal = price * quantity;
  subtotalElement.textContent = subtotal;

  const count = quantity;
  const gname = row.children[1].textContent;
  var xhr = new XMLHttpRequest();
  xhr.open('POST',"http://localhost:8080/Cart", true);
  xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  xhr.send("action=update&gname="+gname+"&count="+count);

}


//更新总价,无需上报服务器，本地计算即可
function updateTotalPrice() {
  const rows = Array.from(document.querySelectorAll('tr'));
  let totalPrice = 0;
  rows.forEach((row) => {
    const priceElement = row.children[2];
    const quantityElement = row.querySelector('input[type="text"]');
    if (priceElement && quantityElement) {
      const price = parseInt(priceElement.textContent);
      const quantity = parseInt(quantityElement.value);
      totalPrice += price * quantity;
    }
  });
  document.querySelector('#totalPrice').textContent = totalPrice;
}

//购物车结账！
payLink.addEventListener('click', (event) => {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', 'http://localhost:8080/Cart?action=checkout', true);
  xhr.send();

  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4 && xhr.status == 200) {
      alert('成功结账，跳转回商城主页！');
      window.location.href = '/index.html';
    }
  }
});
