<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html lang="zh-CN">

<head>
<title>注册登录</title>
<style>
    *, *:before, *:after {
    box-sizing: border-box;
    
    
    
    margin: 0;
    padding: 0;
}

body {
    font-family: 'Open Sans', Helvetica, Arial, sans-serif;
    background: #ededed;
}

input, button {
    border: none;
    outline: none;
    background: none;
    font-family: 'Open Sans', Helvetica, Arial, sans-serif;
}

.tip {
    font-size: 20px;
    margin: 40px auto 50px;
    text-align: center;
}

.content {
    overflow: hidden;
    position: absolute;
    left: 50%;
    top: 50%;
    width: 900px;
    height: 550px;
    margin: -300px 0 0 -450px;
    background:bisque;
  

}

.form {
    position: relative;
    width: 640px;
    height: 100%;
    transition: -webkit-transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
    padding: 50px 30px 0;
}

.sub-cont {
    overflow: hidden;
    position: absolute;
    left: 640px;
    top: 0;
    width: 900px;
    height: 100%;
    padding-left: 260px;
    background: bisque;
    transition: -webkit-transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
}

.content.s--signup .sub-cont {
    -webkit-transform: translate3d(-640px, 0, 0);
    transform: translate3d(-640px, 0, 0);
}

button {
    display: block;
    margin: 0 auto;
    width: 260px;
    height: 36px;
    border-radius: 30px;
    color: #fff;
    font-size: 15px;
    cursor: pointer;
}

.img {
    overflow: hidden;
    z-index: 2;
    position: absolute;
    left: 0;
    top: 0;
    width: 260px;
    height: 100%;
    padding-top: 360px;
}

.img:before {
    content: '';
    position: absolute;
    right: 0;
    top: 0;
    width: 900px;
    height: 100%;
    /* 更换小窗口背景图片 ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！*/
    background-image: url(./img/snow.gif);
    background-size: cover;
    transition: -webkit-transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
}

.img:after {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
}

.content.s--signup .img:before {
    -webkit-transform: translate3d(640px, 0, 0);
    transform: translate3d(640px, 0, 0);
}

.img__text {
    z-index: 2;
    position: absolute;
    left: 0;
    top: 50px;
    width: 100%;
    padding: 0 20px;
    text-align: center;
    color: #fff;
    transition: -webkit-transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out;
    transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
}

.img__text h2 {
    margin-bottom: 10px;
    font-weight: normal;
}

.img__text p {
    font-size: 14px;
    line-height: 1.5;
}

.content.s--signup .img__text.m--up {
    -webkit-transform: translateX(520px);
    transform: translateX(520px);
}
.img__text.m--in {
    -webkit-transform: translateX(-520px);
    transform: translateX(-520px);
}

.content.s--signup .img__text.m--in {
    -webkit-transform: translateX(0);
    transform: translateX(0);
}

.img__btn {
    overflow: hidden;
    z-index: 2;
    position: relative;
    width: 100px;
    height: 36px;
    margin: 0 auto;
    background: transparent;
    color: #fff;
    text-transform: uppercase;
    font-size: 15px;
    cursor: pointer;
}
.img__btn:after {
    content: '';
    z-index: 2;
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    border: 2px solid #fff;
    border-radius: 30px;
}

.img__btn span {
    position: absolute;
    left: 0;
    top: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    transition: -webkit-transform 0.6s;
    transition: transform 0.6s;
    transition: transform 0.6s, -webkit-transform 0.6s;
}

.img__btn span.m--in {
    -webkit-transform: translateY(-72px);
    transform: translateY(-72px);
}

.content.s--signup .img__btn span.m--in {
    -webkit-transform: translateY(0);
    transform: translateY(0);
}

.content.s--signup .img__btn span.m--up {
    -webkit-transform: translateY(72px);
    transform: translateY(72px);
}

h2 {
    width: 100%;
    font-size: 26px;
    text-align: center;
}

label {
    display: block;
    width: 260px;
    margin: 25px auto 0;
    text-align: center;
}

label span {
    font-size: 12px;
    color: #909399;
    text-transform: uppercase;
}

input {
    display: block;
    width: 100%;
    margin-top: 5px;
    padding-bottom: 5px;
    font-size: 16px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.4);
    text-align: center;
}
.admin ,.normal{
     display:inline;  
     width:10%;
     text-align:center;
     margin-top:10px;
     padding:0;
}
#span{
    margin-left:170px;
    color:black;
}
#span2{
    margin-left:20px;
     color:black;
}

.forgot-pass {
    margin-top: 15px;
    text-align: center;
    font-size: 12px;
    color: #cfcfcf;
}

.forgot-pass a {
    color: #cfcfcf;
}

.submit {
    margin-top: 40px;
    margin-bottom: 20px;
    background: #d4af7a;
    text-transform: uppercase;
}

.fb-btn {
    border: 2px solid #d3dae9;
    color: #8fa1c7;
}
.fb-btn span {
    font-weight: bold;
    color: #455a81;
}

.sign-in {
    transition-timing-function: ease-out;
}
.content.s--signup .sign-in {
    transition-timing-function: ease-in-out;
    transition-duration: 0.6s;
    -webkit-transform: translate3d(640px, 0, 0);
    transform: translate3d(640px, 0, 0);
}

.sign-up {
    -webkit-transform: translate3d(-900px, 0, 0);
    transform: translate3d(-900px, 0, 0);
}
.content.s--signup .sign-up {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
}
</style>
<style>
    
    body{
        width: 100%;
        height: 100%;
        /* 换整体背景图片 ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！*/
        background: url(./img/2.jpg) no-repeat !important;
        background-size: cover  !important;
    }
</style>
<style>
    #content{
        background: url(./img/bkm.jpeg) no-repeat ;
    }
    #sub-cont{
        background: url(./img/bkm.jpeg) no-repeat ;
    }
</style>
</head>

<body>
        <div class="content" id="content">
            <form action="Login.do" method="post">
            <div class="form sign-in">
                <h2>登录界面</h2>
                <label>
                    <span>用户名</span>
                    <input type="text" name="name"/>
                </label>
                <label>
                    <span>密码</span>
                    <input type="password"  name="password"/>
                </label>
                 <label class="admin">
                    <span id="span">admin</span>
                     <input type="radio" name="admin" value="1" class="admin"/>
                </label>
                 <label class="admin">
                    <span id="span2">normal</span>
                     <input type="radio" name="admin" value="0" class="admin"/>
                </label>   
                <button type="submit" class="submit">确定</button>
               
            </div>
            </form>
            <div class="sub-cont" id="sub-cont">
                <div class="img">
                    <div class="img__text m--up">
                        <h2>兄嘚还没注册吗？</h2>
                        <p>现在就去注册！</p>
                    </div>
                    <div class="img__text m--in">
                        <h2>已经有账号了吗？</h2>
                        <p>去登录！好久不见啦</p>
                    </div>
                    <div class="img__btn">
                        <span class="m--up">注册</span>
                        <span class="m--in">确定</span>
                    </div>
                </div>
                <form action="register.do" method="post">
                    <div class="form sign-up">
                        <h2>注册</h2>
                        <label>
                            <span>用户名</span>
                            <input type="text" name="" />
                        </label>
                        <label>
                            <span>密码</span>
                            <input type="password" name="password"/>
                        </label>
                        <label class="admin">
                    <span id="span">admin</span>
                     <input type="radio" name="admin" value="1" class="admin"/>
                </label>
                 <label class="admin">
                    <span id="span2">normal</span>
                     <input type="radio" name="admin" value="1" class="admin"/>
                </label>  
                        <button type="submit" class="submit">确定</button>   
                    </div>
                </form>
            </div>
        </div>


    <script>
        document.querySelector('.img__btn').addEventListener('click', function() {
    document.querySelector('.content').classList.toggle('s--signup')
})
    </script>
</body>

</html>