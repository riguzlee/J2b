console.log("ok");
document.querySelector('input').addEventListener('change', function () {
    var that = this;

    lrz(that.files[0], {
        width: 800
    })
        .then(function (rst) {
            var img = new Image(),
                div = document.createElement('div'),
                p = document.createElement('p'),
                sourceSize = toFixed2(that.files[0].size / 1024),
                resultSize = toFixed2(rst.fileLen / 1024),
                scale = parseInt(100 - (resultSize / sourceSize * 100));

            p.style.fontSize = 13 + 'px';
            p.innerHTML      = '源文件：<span class="text-danger">' +
                sourceSize + 'KB' +
                '</span> <br />' +
                '压缩后传输大小：<span class="text-success">' +
                resultSize + 'KB (省' + scale + '%)' +
                '</span> ';

            div.className = 'preview';
            div.appendChild(img);
            div.appendChild(p);

            img.onload = function () {
            	console.log('added');
                that.parentNode.appendChild(div);
            };

            img.src = rst.base64;

            /*            /!* ==================================================== *!/
             // 原生ajax上传代码，所以看起来特别多 ╮(╯_╰)╭，但绝对能用
             // 其他框架，例如ajax处理formData略有不同，请自行google，baidu。
             var xhr = new XMLHttpRequest();
             xhr.open('POST', '/upload');

             xhr.onload = function () {
             if (xhr.status === 200) {
             // 上传成功
             } else {
             // 处理其他情况
             }
             };

             xhr.onerror = function () {
             // 处理错误
             };

             // issues #45 提到似乎有兼容性问题,关于progress
             xhr.upload.onprogress = function (e) {
             // 上传进度
             var percentComplete = ((e.loaded / e.total) || 0) * 100;
             };

             // 添加参数
             rst.formData.append('fileLen', rst.fileLen);
             rst.formData.append('xxx', '我是其他参数');

             // 触发上传
             xhr.send(rst.formData);
             /!* ==================================================== *!/*/

            return rst;
        });
});
function toFixed2 (num) {
    return parseFloat(+num.toFixed(2));
}