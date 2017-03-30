//package com.sergeev.studapp.actions;
//
//import com.sergeev.studapp.model.Course;
//import com.sergeev.studapp.model.Group;
//import com.sergeev.studapp.model.User;
//import com.sergeev.studapp.service.CourseService;
//import com.sergeev.studapp.service.GroupService;
//import com.sergeev.studapp.service.UserService;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@WebServlet(name = "GroupServlet", urlPatterns = "/api/*")
//public class GroupServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        RestRequest resource = new RestRequest(request.getPathInfo());
//        String id = resource.getId();
//        String action = resource.getAction();
//
//        String title;
//        Group group;
//        Map<Group, Integer> groupsStudents;
//        List<User> students;
//        List<Course> courses;
//
//
////        out.println(request.getPathInfo());
////        out.println(request.getParameterMap());
//
//        switch (action) {
//            case "item":
//                group = GroupService.read(id);
//                courses = CourseService.readByGroup(id);
//                students = UserService.readByGroup(id);
//                request.setAttribute("group", group);
//                request.setAttribute("students", students);
//                request.setAttribute("courses", courses);
//                request.getRequestDispatcher("/group.jsp").forward(request, response);
//                return;
//            case "add":
//                request.getRequestDispatcher("/add-group.jsp").forward(request, response);
//                return;
//            case "create":
//                title = request.getParameter("title");
//                GroupService.create(title);
//                response.sendRedirect("/api/group");
//                break;
//            case "change":
//                id = request.getParameter("id");
//                group = GroupService.read(id);
//                request.setAttribute("group", group);
//                request.getRequestDispatcher("/change-group.jsp").forward(request, response);
//                return;
//            case "update":
//                title = request.getParameter("title");
//                GroupService.update(title, id);
//                response.sendRedirect("/api/group");
//                break;
//            case "group":
//                groupsStudents = GroupService.studentsCount();
//                request.setAttribute("groupsStudents", groupsStudents);
//                request.getRequestDispatcher("/groups.jsp").forward(request, response);
//                return;
//            case "remove":
//                GroupService.delete(id);
//                response.sendRedirect("/api/group");
//                break;
//        }
//
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
//    }
//
//    private class RestRequest {
//
//        private Pattern regexpAllPattern = Pattern.compile("/group[/]?");
//        private Pattern regexpItemPattern = Pattern.compile("/group/([0-9]+)");
//        private Pattern regexpActionPattern = Pattern.compile("/group/(\\w*)");
//        private Pattern regexpIdActionPattern = Pattern.compile("/group/(\\w*)/([0-9]*)");
//
//        private String id;
//        private String action;
//
//        public RestRequest(String pathInfo) throws ServletException {
//
//            Matcher matcher;
//
//            matcher = regexpActionPattern.matcher(pathInfo);
//            if (matcher.find()) {
//                action = matcher.group(1);
//                return;
//            }
//
//            matcher = regexpIdActionPattern.matcher(pathInfo);
//            if (matcher.find()) {
//                action = matcher.group(1);
//                id = matcher.group(2);
//                return;
//            }
//
//            matcher = regexpItemPattern.matcher(pathInfo);
//            if (matcher.find()) {
//                action = "item";
//                id = matcher.group(1);
//                return;
//            }
//
//            matcher = regexpAllPattern.matcher(pathInfo);
//            if (matcher.find()) {
//                action = "group";
//                return;
//            }
//            throw new ServletException("Invalid URI");
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getAction() {
//            return action;
//        }
//
//        public void setAction(String action) {
//            this.action = action;
//        }
//    }
//}
