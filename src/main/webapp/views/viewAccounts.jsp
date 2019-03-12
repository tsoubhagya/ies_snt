<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View Accounts</title>
</head>
<body>
	<h2>View Accounts</h2>
	<table border="1">
		<thead>
			<tr>
				<td>S.No</td>
				<td>First Name</td>
				<td>Last Name</td>
				<td>Email</td>
				<td>Role</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${accounts}" var="acc" varStatus="index">
				<tr>
					<td><c:out value="${index.count}" /></td>
					<td><c:out value="${acc.firstName}" /></td>
					<td><c:out value="${acc.lastName}" /></td>
					<td><c:out value="${acc.email}" /></td>
					<td><c:out value="${acc.role}" /></td>

					<td><a href="#">Edit</a> 
						<c:if test="${acc.activeSw =='Y' }">
							<a href="#">Delete</a>
						</c:if> 
						<c:if test="${acc.activeSw =='N' }">
							<a href="#">Activate</a>
						</c:if>
					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>