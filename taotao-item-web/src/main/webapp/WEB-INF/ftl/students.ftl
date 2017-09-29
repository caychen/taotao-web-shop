<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>测试页面</title>
	<link rel="stylesheet" href="">
</head>
<body>
	<table border="1" cellspacing="0">
		<thead>
			<tr>
				<th>序号</th>
				<th>学号</th>
				<th>姓名</th>
				<th>年龄</th>
				<th>地址</th>
			</tr>
		</thead>
		<tbody>
			<#list students as student>
				<#if student_index % 2 == 0>
					<tr style="background-color:red">
				<#else>
					<tr style="background-color:blue">
				</#if>
						<td>${student_index}</td>
						<td>${student.id}</td>
						<td>${student.name}</td>
						<td>${student.age}</td>
						<td>${student.address}</td>
					</tr>
			</#list>
		</tbody>
	</table>
	
	${date?date}<br/>
	${date?time}<br/>
	${date?datetime}<br/>
	${date?string('yyyy-MM-dd hh:mm:ss')}<br/>
	
	${value!}<br/>
	
	<#if value??>
		value非null
	<#else>
		value为null
	</#if>
	
	<#include "hello.ftl">
</body>
</html>