package com.example.cms.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class NgrokHeaderFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (response instanceof HttpServletResponse) {
      ((HttpServletResponse) response).setHeader("ngrok-skip-browser-warning", "true");
    }
    chain.doFilter(request, response);
  }
}
