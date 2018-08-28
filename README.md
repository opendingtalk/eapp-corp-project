
## 运行环境开发工具
java7  
intellij idea

## 项目结构
```
.
├── README.md
├── eapp-corp-project.iml
├── lib
│   ├── lippi-oapi-encrpt.jar
│   ├── taobao-sdk-java-auto_1479188381469-20180525-source.jar
│   └── taobao-sdk-java-auto_1479188381469-20180525.jar
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       ├── Application.java
    │   │       ├── config
    │   │       │   ├── ApiUrlConstant.java
    │   │       │   └── Constant.java
    │   │       ├── controller
    │   │       │   ├── AflowController.java
    │   │       │   ├── CallbackController.java
    │   │       │   └── IndexController.java
    │   │       └── util
    │   │           ├── LogFormatter.java
    │   │           └── ServiceResult.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── ApplicationTests.java
```
                    
                
## 项目配置
1.更新Constant.java文件的CORP_ID,APPKEY，APPSECRET, AGENTID，可在企业自建E应用的详情里找到。
2.更新Constant.java文件的ENCODING_AES_KEY，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取，可以随机生成。
3.更新Constant.java文件的TOKEN，长度建议为3-8个字符，从a-z, A-Z, 0-9共62个字符中选取。
4.更新Constant.java文件的PROCESS_CODE，表示审批模板的唯一码，具体可以在【钉钉管理后台-审批-编辑表单-顶部url中找到】。
5.更新application.properties文件的服务器启动端口。

## 打包命令
mvn clean package  -Dmaven.test.skip=true  
打成的包在工程文件的target目录下。文件为  "工程名"-"版本号".jar。()

## 服务部署    
java -jar  target/"工程名"-"版本号".jar
