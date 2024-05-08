<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Random Number Operation Game</title>
<script>
    function reloadPage() {
        location.reload();
    }
</script>
</head>
<body>
    <h2>Random Number Operation Game</h2>
    
    <%
 
        int score = 0;
    Integer sessionScore = (Integer) session.getAttribute("score");
    if(sessionScore != null) {
        score = sessionScore.intValue();
    }

        String userAnswer = request.getParameter("answer");
        if (userAnswer != null && !userAnswer.isEmpty()) {
            int num1 = Integer.parseInt(request.getParameter("num1"));
            int num2 = Integer.parseInt(request.getParameter("num2"));
            String operation = request.getParameter("operation");
            int correctAnswer = 0;
            if ("+".equals(operation)) {
                correctAnswer = num1 + num2;
            } else if ("-".equals(operation)) {
                correctAnswer = num1 - num2;
            } else if ("*".equals(operation)) {
                correctAnswer = num1 * num2;
            } else if ("/".equals(operation)) {
                correctAnswer = num1 / num2;
            }

            if (userAnswer.equals(String.valueOf(correctAnswer))) {
                score++;
                session.setAttribute("score", score);
                out.println("<p>Congratulations! Your answer is correct!</p>");
            } else {
            	score = 0; 
                session.setAttribute("score", score);
                out.println("<p>Sorry, your answer is incorrect. The correct answer is: " + correctAnswer + "</p>");
            }
        }
    %>
    
    <form action="" method="post">
        <p>Solve the expression:</p>
        <%
      
            int num1 = (int)(Math.random() * 100);
            int num2 = (int)(Math.random() * 100);
            String[] operations = {"+", "-", "*", "/"};
            String operation = operations[(int)(Math.random() * operations.length)];
        %>
        <p><%= num1 %> <%= operation %> <%= num2 %> = </p>
        
        <input type="hidden" name="num1" value="<%= num1 %>" />
        <input type="hidden" name="num2" value="<%= num2 %>" />
        <input type="hidden" name="operation" value="<%= operation %>" />
        
        <input type="text" name="answer" placeholder="Your Answer" required />
        <br/><br/>

        
            <p>Score: <%= score %></p>
    
        
        
        <input type="submit" value="Check Answer" />
    </form>
</body>
</html>
