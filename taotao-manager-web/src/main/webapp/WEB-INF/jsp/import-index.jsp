<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <a class="easyui-linkbutton" onclick="importIndex();">一键导入商品数据到索引库</a>
</div>

<script type="text/javascript">
	function importIndex(){
		$.ajax({
			url:'/index/import',
			type:'post'
		}).then(function(response){
			if(response.status == 200){
				$.messager.alert('提示', '导入索引库成功!');
			}
		}, function(){
			$.messager.alert('提示', '导入索引库失败!');
		});
	}
</script>