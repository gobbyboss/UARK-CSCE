<html>
<head><title>addstudent.php</title></head>
<body>
    <table><form action="search.php" method="post">
                <tr>
                        <td>Search again for a document:</td>
                        <td><input type="Text" name="search" align="TOP" size="20"></td>
                        <td><input type="Submit" name="submit" value="Search"></td>
                </tr>
        </form></table>
    <?php
    echo "<h2>Search Query: " . $_POST[search] . "</h2>";
    <br>
    echo exec("java Retrieve out $_POST[search]");
    ?>
</body>
</html>
