<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/static/newLayer.html"%>
<script type="text/javascript" src="../static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="../static/js/ZeroClipboard.js"></script>
<script type="text/javascript" src="../static/js/clipboard.min.js"></script>

<link href="../static/css/style.css" rel=stylesheet>
<link href="../static/css/yinhuo.css" rel=stylesheet>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=0">
<title>WiFi密码</title>
</head>

<body>
	
	<div class="title_wifi">
		<span>Wifi名称:Li family</span>
		
	</div>
	 <div id="btn" class="js-copy" data-clipboard-text="22">
	 	  <button  id="copypassword">点击复制wifi密码</button>
	 </div>

  <canvas id="canvas" width="500" height="500" ></canvas>
<div class="checkbox-wrap">
  <input class="checkbox" id="checkbox" type="checkbox" />
  <label class="firefly" for="checkbox">
    <div class="abdomen">
      <div class="thorax">
        <div class="head">
          <div class="eyes"></div>
          <div class="antennae"></div>
        </div>
      </div>
      <div class="wings">
        <div class="wing wing-a"></div>
        <div class="wing wing-b"></div>
      </div>
    </div>
  </label>
</div>
<div style="text-align:center;clear:both">
</div>
  <script  src="../static/js/index.js"></script>

</body>

<script type="text/javascript">
$(function(){
	$(".firefly").click();

})
	$(function() {
		$.ajax({
			url : "/wifi/lhm.do",
			dataType : "json",
			success : function(data) {
				
				$("#btn").attr("data-clipboard-text",data.password)
			}
		})
		$("#copypassword").click(function() {
			var password=$("#wifipassword").val();
			copyToClipboard(password);
		})
	})

 var btn = document.getElementById('btn');
    var clipboard = new Clipboard(btn);//实例化

    //复制成功执行的回调，可选
    clipboard.on('success', function(e) {
        newLayer("wifi密码已复制")
    });

    //复制失败执行的回调，可选
    clipboard.on('error', function(e) {
        console.log(e);
    });
	
</script>

</html>