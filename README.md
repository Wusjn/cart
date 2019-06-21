## 如何运行
1. 下载`che-cart-plugin`（见`https://github.com/Wusjn/che-cart-plugin`）和图数据库`Graph-Lucene`（联系`https://github.com/Wusjn`要）
2. `cart`中找到`db.path`（在`cart/src/main/resource/application.properties`中），修改路径至`Graph-Lucene`文件夹
3. 运行`cart`中的`CartApplication.java`，启动插件服务器
4. 启动`eclipse/che:6.16.0`服务器，挂载`che-cart-plugin`插件（见`https://github.com/Wusjn/che-cart-plugin`）
5. 打开地址`{eclipse/che服务器ip}:8080`，在其中创建新的workspace，添加一个project
6. 在project中的任意一个java文件中，在任意位置输入`??`，点击右键，选择`Show Api Recommendation`，结果会在下方显示

注意：
    路径不能有中文
