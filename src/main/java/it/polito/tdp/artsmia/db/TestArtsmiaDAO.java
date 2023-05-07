package it.polito.tdp.artsmia.db;

import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ExhibitionObject;
import it.polito.tdp.artsmia.model.HelpingClass;

public class TestArtsmiaDAO {

	public static void main(String[] args) {

		ArtsmiaDAO dao = new ArtsmiaDAO();
			
		List<ArtObject> objects = dao.listObjects();
		List<HelpingClass> l = dao.listHelpingClass();
		System.out.println(l.get(0));
		System.out.println(l.size());
	}

}
