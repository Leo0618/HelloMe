# HelloMe
an app for some todo task



> some utils and framework will be put in



----
##11/15/2016 10:29:35 AM 


######1.项目加入MultiDex支持
-  app的gradle中defaultConfig节点配置 multiDexEnabled true
-  app的gradle中android节点下加入dexOptions配置为 jumboMode = true
-  添加依赖  compile "com.android.support:multidex:1.0.1"

######2.热修复方案Tinker集成
项目中加入热修复方案，目前采用的是微信Tinker，对于Tinker是新手的用户建议进入官方Wiki进行学习了解，文档很详细，[https://github.com/Tencent/tinker/wiki](https://github.com/Tencent/tinker/wiki "Tinker"),项目中当前时间点仅提供了简单使用的Demo,再深入理解了Tinker之后可以进行深度处理，demo调试步骤如下：

- down下项目后同步下确保没错误报告
- 执行gradle命令 assembleDebug ，这时候会在build目录中的apkPatchDir输出带时间格式的apk安装包(带了bug的安装包)，安装这个安装包，测试，此时应该是这样的：

![HasBug](http://i.imgur.com/P2udY7w.png)

- 修改bug,demo中在HotFixActivity中的showBugState方法进行更换文本显示和图片资源替换
- app的gradle修改 def fileNameComm = "这里修改为apkPatchDir下面apk文件名称" ，同步下，然后执行gradle命令 tinkerPatchDebug , 此时会在build的output目录生成tinker补丁`patch_signed_7zip.apk`,此时应该这样的：

![patch file](http://i.imgur.com/UM5Vomj.png)

- 将`patch_signed_7zip.apk`放入手机SD卡根目录，点击修复，然后进行重启，可以看到bug已经修复，结果应该是这样的：

![noBug](http://i.imgur.com/AkTDJiS.png)


----
##11/16/2016 3:08:42 PM 


######添加自定义View：DoubleStrikeThroughTextView
一个简单自定义控件，具有多条删除线显示功能的TextView，效果如下：
![DoubleStrikeThroughTextView](http://i.imgur.com/BYxNqFn.png)