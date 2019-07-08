package bhe.net.cn.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public RequestWrapper(HttpServletRequest request, String requestBody) {
        super(request);
        body = requestBody;
    }

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        body = strBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (body != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }
            };
        } else {
            return null;
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (body != null) {
            return new BufferedReader(new StringReader(body));
        } else {
            return null;
        }
    }

    public String getBody() {
        return body;
    }

}
