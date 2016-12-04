package service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * ���ദ��ҵ���� ����
 */
public class ClassifyBiz {
	/**
	 * ���ӷ���
	 */
	public boolean save(String pId, String pType, String pImage) {
		Record pro = new Record().set("id", pId).set("type", pType).set("image", pImage);
		boolean res = Db.save("classify_b", pro);
		return res;
	}

	/**
	 * ɾ������
	 */
	public boolean deleteByID(String pid) {
		boolean res = Db.deleteById("classify_b", "id", pid);
		return res;
	}

	/**
	 * �޸ķ�����Ϣ
	 */
	public int update(String pId, String pType, String pImage) {
		String sql = "UPDATE classify_b SET" + " id='" + pId + "type='" + pType + "'," + "image='" + pImage
				+ " WHERE id='" + pId + "'";
		int res = Db.update(sql);
		return res;
	}

	public List<Record> findAll() {
		List<Record> pros = Db.find("select * from activity_b");
		return pros;
	}

	/**
	 * ������Ʒ
	 */
	public Record findByID(String pid) {
		Record rec = Db.findById("activity_b", "id", pid);
		return rec;
	}
}
