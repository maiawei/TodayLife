const splitStr = "#todayLife#"

function loadAllImg() {
  var allImg = document.getElementsByTagName("img");
  var urlList="";
  for (var i = 0; i < allImg.length; i++) {
    allImg[i].src = allImg[i].alt;
    urlList=urlList+allImg[i].src+splitStr;
    allImg[i].onclick = function (){
        window.picJavaScript.openImg(this.src);
    }
  }
  window.picJavaScript.getImgArray(urlList);
}

function loadSingleImg() {
   var allImg = document.getElementsByTagName("img");
   var urlList="";
   for (var i = 0; i < allImg.length; i++) {
     urlList=urlList+allImg[i].alt+splitStr;
     allImg[i].onclick = function (){
         if(this.src.indexOf("default_todaylife_img") < 0){
            window.picJavaScript.openImg(this.src);
         }else{
            loadImgWithProgress(this,this.alt);
         }
     }
   }
   window.picJavaScript.getImgArray(urlList);
}

function loadImgWithProgress(img,url){
     img.src = "file:///android_asset/loading.gif";
     var newImg = new Image();
     newImg.src = url;
     newImg.onerror = () => {
         newImg.src = "file:///android_asset/default_todaylife_img.jpg";
     }
     newImg.onload = () => {
         img.src = newImg.src;
     }
}

