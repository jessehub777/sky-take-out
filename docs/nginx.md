### Nginx的默认配置文件路径

```
/opt/homebrew/etc/nginx/nginx.conf
```

可使用`nginx -t`命令查看以及测试nginx配置文件的正确性。

### 修改nginx.conf文件

```
server {
    listen 80;
    server_name localhost;

    location / {
        root   html/sky;
        index  index.html index.htm;
    }

    # 反向代理,处理管理端发送的请求
    location /api/ {
        proxy_pass   http://localhost:8080/admin/;
        #proxy_pass   http://webservers/admin/;
    }
    
    # 反向代理,处理用户端发送的请求
    location /client/ {
        proxy_pass   http://localhost:8080/user/;
    }
    
    # WebSocket
    location /ws/ {
        proxy_pass   http://webservers/ws/;
        proxy_http_version 1.1;
        proxy_read_timeout 3600s;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "$connection_upgrade";
    }

}
```

### 重新启动nginx

```
nginx -s reload
```