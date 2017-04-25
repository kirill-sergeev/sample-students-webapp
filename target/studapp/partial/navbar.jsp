<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-light navbar-toggleable-md bg-faded" style="background-color: #e3f2fd;">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Student's Website</a>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/group">Groups</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/student">Students</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/teacher">Teachers</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/discipline">Disciplines</a>
            </li>
        </ul>

        <form class="form-inline my-2 my-lg-0" action="${pageContext.request.contextPath}/student/search/" method="POST">
            <div class="input-group">
                <input type="hidden" name="action" value="search">
                <input type="text" name="name" class="form-control" placeholder="Search for students...">
                <span class="input-group-btn">
                        <button class="btn btn-secondary" type="submit">Go!</button>
                    </span>
            </div>
        </form>

        <form class="form-inline my-2 my-lg-0" action="${pageContext.request.contextPath}/logout" method="POST">
            <button type="submit" class="btn btn-secondary">Log Out</button>
        </form>
    </div>
</nav>


