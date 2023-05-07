package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ExhibitionObject;
import it.polito.tdp.artsmia.model.HelpingClass;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ExhibitionObject> listExhibitionObjects() {
		
		String sql = "SELECT * FROM exhibition_objects";
		List<ExhibitionObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ExhibitionObject exhObj = new ExhibitionObject(res.getInt("exhibition_id"), 
														res.getInt("object_id"));
				
				result.add(exhObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<HelpingClass> listHelpingClass() {
		
		String sql = "SELECT eo1.object_id AS ob_id1, eo2.object_id AS ob_id2, COUNT(DISTINCT eo1.exhibition_id) AS counter "
				+ "FROM exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE eo1.exhibition_id = eo2.exhibition_id "
				+ "AND eo1.object_id != eo2.object_id "
				+ "GROUP BY ob_id1, ob_id2";
		
		List<HelpingClass> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				HelpingClass exhObj = new HelpingClass(res.getInt("ob_id1"), 
													   res.getInt("ob_id2"),
													   res.getInt("counter"));
				
				result.add(exhObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
