package tmp;

import java.sql.*;
import org.sqlite.JDBC;

public class TestSQLite {
	public static void main(String[] args) {
		try {
			// ����SQLite��JDBC

			Class.forName("org.sqlite.JDBC");

			// ����һ�����ݿ���naivemail.db�����ӣ���������ھ��ڵ�ǰĿ¼�´���֮

			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:naivemail.db");

			Statement stat = conn.createStatement();

			stat.executeUpdate("insert into account (a_account,a_passwd) values('��ʲô��','asdfsad');"); // ��������
			stat.executeUpdate("insert into account (a_account,a_passwd) values('jy','asdfsad');");
			
			ResultSet rs = stat.executeQuery("select * from account;"); // ��ѯ����

			while (rs.next()) { // ����ѯ�������ݴ�ӡ����
				System.out.println("a_id = " + rs.getInt("a_id")); // ������
				
				System.out.print("a_account = " + rs.getString("a_account") + " "); // ������һ

				System.out.println("a_passwd = " + rs.getString("a_passwd")); // �����Զ�

			}
			rs.close();
			conn.close(); // �������ݿ������

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}