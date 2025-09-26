const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 8080;

// MIME类型映射
const mimeTypes = {
    '.html': 'text/html',
    '.css': 'text/css',
    '.js': 'text/javascript',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.gif': 'image/gif',
    '.svg': 'image/svg+xml',
    '.ico': 'image/x-icon'
};

const server = http.createServer((req, res) => {
    let filePath = req.url === '/' ? '/index.html' : req.url;
    filePath = path.join(__dirname, filePath);
    
    const extname = path.extname(filePath).toLowerCase();
    const contentType = mimeTypes[extname] || 'application/octet-stream';
    
    fs.readFile(filePath, (error, content) => {
        if (error) {
            if (error.code === 'ENOENT') {
                // 文件不存在，返回404
                res.writeHead(404, { 'Content-Type': 'text/html' });
                res.end(`
                    <html>
                        <head><title>404 - 页面未找到</title></head>
                        <body>
                            <h1>404 - 页面未找到</h1>
                            <p>请求的页面不存在</p>
                            <a href="/">返回首页</a>
                        </body>
                    </html>
                `);
            } else {
                // 服务器错误
                res.writeHead(500);
                res.end(`服务器错误: ${error.code}`);
            }
        } else {
            // 成功读取文件
            res.writeHead(200, { 'Content-Type': contentType });
            res.end(content, 'utf-8');
        }
    });
});

server.listen(PORT, () => {
    console.log(`🚀 FlowMaster 统一入口服务器启动成功！`);
    console.log(`📱 访问地址: http://localhost:${PORT}`);
    console.log(`🌐 用户工作台: http://localhost:3000`);
    console.log(`⚙️ 管理后台: http://localhost:3003`);
    console.log(`🎨 流程设计器: http://localhost:3001`);
    console.log(`🔧 后端API: http://localhost:8081/user-service`);
    console.log(`📚 API文档: http://localhost:8081/user-service/swagger-ui.html`);
    console.log(`\n按 Ctrl+C 停止服务器`);
});

// 优雅关闭
process.on('SIGINT', () => {
    console.log('\n正在关闭服务器...');
    server.close(() => {
        console.log('服务器已关闭');
        process.exit(0);
    });
});
