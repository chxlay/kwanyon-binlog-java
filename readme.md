### binlog 相关封装，canal 框架台累赘，需要安装客户端，才可以使用客户端，所以再此封装一个可以直接使用的 binlog 客户端
#### 接收到 binlog 事件解析成 Message 数据进行发送，默认使用的是 SpringEvent 进行发送，实现接口 MessagePublisher 可扩展为 kafka Rabbit 等消息中间件进行发布


* #### **注意: 从其他项目中独立抽取搬迁到此开放的，搬迁后并未测试,可能会有一些小问题,使用者结合自己项目自行调整**
功能图示：
![结构图.png](%E7%BB%93%E6%9E%84%E5%9B%BE.png)