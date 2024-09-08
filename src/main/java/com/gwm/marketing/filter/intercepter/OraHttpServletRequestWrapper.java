package com.gwm.marketing.filter.intercepter;


import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/4/26 18:06
 */
public class OraHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *   缓存下来的HTTP body
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    private  byte[] body;
    private static final int BUFFER_SIZE = 4096;


    public OraHttpServletRequestWrapper(HttpServletRequest request) throws IOException {

        super(request);
        if(request.getInputStream().available() > 0){
            body = StreamUtils.copyToByteArray(request.getInputStream());
        }
    }



    @Override
    public ServletInputStream getInputStream() throws IOException{
        final ByteArrayInputStream bodyStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
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

            @Override
            public int read() throws IOException {
                return bodyStream.read();
            }
        };
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }


}
