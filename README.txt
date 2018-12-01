demo奉上:

1、使用intellij 导入项目，用intellij配置tomcat服务器(查教程)
导入过程有什么问题请先搜索再问(基本上你们现在遇到的配置问题别人都遇到过,一搜一大堆)

2、项目前端在WebCotent\WEB-INF下
index.ftl 你们可以当作index.html来使用,语法完全一样
index.ftl 前端语法上跟html完全一样
(解释一下:ftl使用freemarker框架,因为index.html是静态资源,配置比较麻烦,助教愣是没配出来)

3、因为助教没配静态资源的环境,所以.css,.js文件使用不了,所以这些东西都写在index.ftl里就行了
当然你如果试图去配,请自便.

4、前端传递给后台的数据包括:
startAddress、startLongitude、startLatitude、endAddress、endLongitude、endLatitude、choose
其中choose是当前选择路径的方式如步行最少、时间最短
后台使用这七个名字接收变量,对应到form表单里的input如下:
<input type="text" name="startAddress" id="startAddress" value="复旦大学张江校区"/>
<input type="hidden" id="hiddenStartLongitude" value="121.604569"/>
<input type="hidden" id="hiddenStartLatitude" value="31.196348"/>
<input type="text" name="endAddress" id="endAddress" value="人民广场"/>
<input type="hidden" id="hiddenEndLongitude" value="121.478941"/>
<input type="hidden" id="hiddenEndLatitude" value="31.236009"/>

以下radio是单选项针对choose变量
<input type="radio" name="items" value="1" />步行最少<br/>
<input type="radio" name="items" value="2" />换乘最少<br/>
<input type="radio" name="items" value="3" />时间最短<br/>

要修改前端请记得传递这几个值,所以请在clickButton()提交代码里处理检查数据操作


5、后台代码里,config、constant包可以不用
主要关注controller、service(这是最重要的)、bean包
controller:仅处理前后交接(其实你们也可以不用管)
service:处理业务逻辑(这是你们最需要实现的)
bean:定义一些实体,如图Graph等

6、后台返回ReturnValue类,每个类属性意思自己看代码,看变量名就知道是什么意思了

7、这个工程可能有bug(比如我不知道百度地图api提供的公交路线途径点最多支持多少个)
所以,请有志之士可以帮我修改

8、地铁数据的输入文件放在src包下(请不要修改他的放置位置,否则会出错,如果要改名字,记得改Constant类的常量FILE_PATH)
在Constant类里我把这个文件读入了,你们可以修改换成能够读其他格式的代码

9、祝你一次就能配置成功、一次运行出来.Good luck!

