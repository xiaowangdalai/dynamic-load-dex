dx --dex --output=ajar.jar dynamic.jar

升级思路：
1、首先是要集成ajar
2、使用的时候，先在本地目录下查找（目录下应包含jar和版本描述文件）
3、如果发现版本描述问价的版本号比当前的高，那么则采用classloader的方式调用接口