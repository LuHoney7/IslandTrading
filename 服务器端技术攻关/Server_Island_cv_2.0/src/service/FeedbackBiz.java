package service;

import java.util.List;

/** 
 * ��������ҵ����
 * ����
 */

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class FeedbackBiz {

	// TODO Auto-generated constructor stub

	/**
	 * ɾ������
	 */
	public boolean deleteByID(String pid) {
		boolean res = Db.deleteById("feedback_b", "id", pid);
		return res;
	}

	public List<Record> findAll() {
		List<Record> pros = Db.find("select * from feedback_b");
		return pros;
	}

	/**
	 * ���ҷ���
	 */
	public Record findByID(String pid) {
		Record rec = Db.findById("feedback_b", "id", pid);
		return rec;
	}

}
