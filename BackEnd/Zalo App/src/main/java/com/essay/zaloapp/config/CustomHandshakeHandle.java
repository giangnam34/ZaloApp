//package com.essay.zaloapp.config;
//
//import com.essay.zaloapp.secruity.StompPrincipal;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//import java.util.Map;
//
//public class CustomHandshakeHandle extends DefaultHandshakeHandler {
//    private final Logger LOG = LoggerFactory.getLogger(CustomHandshakeHandle.class);
//
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//
//            LOG.info("User with ID '{}' opened the page", userDetails.getUsername());
//
//            return new StompPrincipal(userDetails.getUsername());
//        }
//        return null;
//    }
//}
