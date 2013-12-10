package tmp;

import java.sql.*;
import org.sqlite.JDBC;

public class TestSQLite {
	public static void main(String[] args) {
		try {
			// 连接SQLite的JDBC

			Class.forName("org.sqlite.JDBC");

			// 建立一个数据库名naivemail.db的连接，如果不存在就在当前目录下创建之

			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:naivemail.db");

			Statement stat = conn.createStatement();

			stat.executeUpdate("insert into account (a_account,a_passwd) values('搞什么啊','asdfsad');"); // 插入数据
			stat.executeUpdate("insert into account (a_account,a_passwd) values('jy','asdfsad');");
			
			ResultSet rs = stat.executeQuery("select * from account;"); // 查询数据

			while (rs.next()) { // 将查询到的数据打印出来
				System.out.println("a_id = " + rs.getInt("a_id")); // 列属性
				
				System.out.print("a_account = " + rs.getString("a_account") + " "); // 列属性一

				System.out.println("a_passwd = " + rs.getString("a_passwd")); // 列属性二

			}
			rs.close();
			conn.close(); // 结束数据库的连接

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}