package service;

import java.util.Date;
import java.util.List;

/** 
 * �����ҵ����
 */

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ActivityBiz {
	/**
	 * ���ӻ
	 */
	public boolean save(String pID, String pName, String pContent, String pOrganizer, Date pTime, String pSite) {
		Record pro = new Record().set("ACTIVITY_ID", pID).set("ACTIVITY_NAME", pName).set("ACTIVITY_CONTENT", pContent)
				.set("ACTIVITY_ORGANIZER", pOrganizer).set("ACTIVITY_TIME", pTime).set("ACTIVITY_SITE", pSite);
		boolean res = Db.save("islandtrading_activity", pro);
		return res;
	}

	/**
	 * ɾ���
	 */
	public boolean deleteByID(String pid) {
		boolean res = Db.deleteById("islandtrading", "ACTIVITY_ID", pid);
		return res;
	}

	/**
	 * �޸Ļ��Ϣ
	 */
	public int update(String pID, String pName, String pContent, String pOrganizer, String pTime, String pSite) {
		String sql = "UPDATE islandtrading_activity SET" + " ACTIVITY_ID='" + pID + "'," + "ACTIVITY_NAME='" + pName + "'," + "ACTIVITY_CONTENT='" + pContent
				+ "'," + "ACTIVITY_ORGANIZER='" + pOrganizer + "'," + "ACTIVITY_TIME='" + pTime + "'," + "ACTIVITY_SITE='" + pSite + "',"
				+ " WHERE id='" + pID + "'";
		int res = Db.update(sql);
		return res;
	}

	public List<Record> findAll() {
		List<Record> pros = Db.find("select * from islandtrading_activity");
		return pros;
	}

	/**
	 * ������Ʒ
	 */
	public Record findByID(String id) {
		Record rec = Db.findById("islandtrading_activity", "ACTIVITY_ID", id);
		return rec;
	}

}
