# MarkSeekbar
一个只带有标记的seekbar，特意通过点击标记跳到指定位置。但不能任意点击非标记位置移动。


对原生的SeekBar做了几个修改：

实现了进度在浮动窗显示
去掉了进度条变色区域
添加了可点击标记，点击指定标记，跳转到指定进度
屏蔽了非标记区域的点击事件，只能通过滑动到非标记进度
效果图如下： 

这个实现参考了，在GitHub上的地址： 
https://github.com/flyerSea/MarkSeekbar.git

浮动进度显示参考了GitHub上的这两个项目： 
https://github.com/azzits/CustomSeekbar.git 
https://github.com/soyoungboy/NumberSeekBar.git

test