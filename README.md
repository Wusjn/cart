## 如何运行
1. 下载`eclipse che`（官网下载，只在5.5.0版本测试过）和图数据库`Graph-Lucene`（找`nli2code/Wusjn`要）
2. cart中找到`db.path`（在cart/src/main/resource/application.properties中），修改路径至`Graph-Lucene`文件夹
3. 运行CartApplication
4. 运行命令：
    
    `docker run -it -v (che-5.5.0的路径):/repo -v (che的数据存放位置，任意设置):/data -v /var/run/docker.sock:/var/run/docker.sock -e CHE_HOST=(自己的ip地址) eclipse/che:5.5.0 start`
    
    `docker run -it -v /Users/apple/Downloads/liwp/che-5.5.0:/repo -v /Users/apple/Downloads/liwp/chedata:/data -v /var/run/docker.sock:/var/run/docker.sock -e CHE_HOST=10.0.26.84 eclipse/che:5.5.0 start `
    
5. 打开localhost:8080，创建新的workspace（设置中选择java而不是java-mysql）
6. 创建hello world程序，在任意位置输入`?`，点击上方的三角按钮`▶️`，结果会在下方显示

注意：
    路径不能有中文