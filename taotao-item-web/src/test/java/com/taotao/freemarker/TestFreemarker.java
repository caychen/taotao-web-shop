package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarker {

	@Test
	public void testFreemarker() throws Exception {
		String path = "F:/Code/Maven/Taotao/taotao-item-web/src/main/webapp/";
		// 1、创建一个模版文件
		// 2、创建一个Configuration对象
		Configuration config = new Configuration(Configuration.getVersion());

		// 3、设置模版所在的路径
		config.setDirectoryForTemplateLoading(new File(path + "WEB-INF/ftl/"));

		// 4、设置模版的字符集
		config.setDefaultEncoding("UTF-8");

		// 5、使用Configuration对象加载一个模版文件，需要指定模版文件的文件名
		Template template = config.getTemplate("students.ftl");

		// 6、创建一个数据集，可以是entity也可以是map，推荐使用map
		Map<String, Object> data = new HashMap<String, Object>();
		//基本数据类型
		data.put("hello", "hello freemarker!");

		//对象
		Student student = new Student(1, "小米", 11, "上海市");
		data.put("student", student);
		
		//集合
		List<Student> students = new ArrayList<Student>();
		students.add(new Student(1, "小米1", 11, "上海市11"));
		students.add(new Student(2, "小米2", 12, "上海市12"));
		students.add(new Student(3, "小米3", 13, "上海市13"));
		students.add(new Student(4, "小米4", 14, "上海市14"));
		students.add(new Student(5, "小米5", 15, "上海市15"));
		students.add(new Student(6, "小米6", 16, "上海市16"));
		students.add(new Student(7, "小米7", 17, "上海市17"));
		students.add(new Student(8, "小米8", 18, "上海市18"));
		students.add(new Student(9, "小米9", 19, "上海市19"));
		students.add(new Student(10, "小米10", 20, "上海市20"));
		data.put("students", students);
		
		data.put("date", new Date());
		
		data.put("value", null);
		
		// 7、创建一个Writer对象，指定输出文件的路径及文件名
		Writer out = new FileWriter(new File(path + "/tmp/hello.html"));

		// 8、使用模版对象的process方法输出文件
		template.process(data, out);

		// 9、关闭流
		out.close();
	}
}
