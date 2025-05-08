### 使用 Homebrew 安装

```
brew install redis
```


### 启动Redis（会自动以后台服务方式运行（每次开机自启））

```
brew services start redis
```
### 其他Reids命令
```
brew services stop redis        # 停止 Redis 服务
brew services restart redis     # 重启 Redis 服务
brew uninstall redis            # 卸载 Redis
```
| 项目         | 默认值                                |
|--------------|----------------------------------------|
| 监听端口     | 6379                                   |
| 配置文件路径 | /opt/homebrew/etc/redis.conf          |
| 数据目录     | /opt/homebrew/var/db/redis            |


### redis.conf
```
requirepass 123456
```

### Redis Desktop Manager
```
https://github.com/qishibo/AnotherRedisDesktopManager/releases
```
