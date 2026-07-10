//package com.fundamentals.spa.interceptor;
//
//import com.fundamentals.spa.entity.enums.UserRole;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class AuthInterceptor implements HandlerInterceptor {
//    private static final List<String> PUBLIC_PATHs = List.of(
//            "/",
//            "/login",
//            "/register");
//
//    private static final List<String> ADMIN_PATHS = List.of(
//            "/admin/dashboard",
//            "/admin/bookings/confirm",
//            "/admin/bookings/cancel",
//            "/treatments/add",
//            "/treatments/edit",
//            "/treatments/delete"
//    );
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) throws IOException {
//        String endpoint = request.getRequestURI();
//
//        if (endpoint.startsWith("/css") ||
//                endpoint.startsWith("/images") ||
//                        endpoint.startsWith("/js")) {
//            return true;
//        }
//
//        if (PUBLIC_PATHs.contains(endpoint)){
//            return true;
//        }
//
//        HttpSession session = request.getSession(false);
//        if (session == null){
//            response.sendRedirect("/login");
//            return false;
//        }
//
//        UUID userId = (UUID) request.getSession().getAttribute("user_id");
//        if (userId == null) {
//            session.invalidate();
//            response.sendRedirect("/login");
//            return false;
//        }
//
//        UserRole role = (UserRole) session.getAttribute("user_role");
//
//        if (ADMIN_PATHS.stream().anyMatch(endpoint::startsWith) && role != UserRole.ADMIN){
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("You do not have permission to access this resource");
//            return false;
//        }
//        return true;
//    }
//}
