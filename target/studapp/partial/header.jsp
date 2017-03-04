<html>
<head>
    <title>${param.title}</title>
    <link rel='stylesheet' href='webjars/bootstrap/4.0.0-alpha.6-1/css/bootstrap.min.css'>
    <script href='webjars/bootstrap/4.0.0-alpha.6-1/js/bootstrap.min.js'></script>
</head>
<body>

<nav class="navbar navbar-light navbar-toggleable-md bg-faded" style="background-color: #e3f2fd;">
    <a class="navbar-brand" href="/">Student search</a>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="new-student">Add a new student...</a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0" action="search-student" method="POST">

            <div class="input-group">
                <input type="text" name="name" class="form-control" placeholder="Search for students...">
                <span class="input-group-btn">
                        <button class="btn btn-secondary" type="submit">Go!</button>
                    </span>
            </div>
        </form>
    </div>
</nav>