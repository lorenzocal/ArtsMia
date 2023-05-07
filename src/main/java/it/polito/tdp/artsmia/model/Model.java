package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Map<Integer, List<Integer>> mapExhibitionObjects;
	private Graph<ArtObject, DefaultWeightedEdge> graph;
	private List<ArtObject> artObjectList;
	private Map<Integer, ArtObject> artObjectIdMap;
	private List<HelpingClass> helpingList;

	public Model() {
		this.dao = new ArtsmiaDAO();
		this.mapExhibitionObjects = this.createMapExhibitionObjects();
		this.graph = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.artObjectIdMap = this.mapObjects();
		this.helpingList = this.dao.listHelpingClass();
	}
	
	public ArtsmiaDAO getDao() {
		return dao;
	}

	public Map<Integer, List<Integer>> getMapExhibitionObjects() {
		return mapExhibitionObjects;
	}

	public Map<Integer, ArtObject> mapObjects(){
		Map<Integer, ArtObject> result = new HashMap<Integer, ArtObject>();
		for (ArtObject ob : this.dao.listObjects()) {
			result.put(ob.getId(), ob);
		}
		return result;
	}
	
	public Map<Integer, List<Integer>> createMapExhibitionObjects(){
		
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		
		for (ExhibitionObject eo : dao.listExhibitionObjects()) {
			Integer key = eo.getExhibitionId();
			if (map.containsKey(key)) {
				map.get(key).add(eo.getObjectId());
			}
			else {
				List<Integer> list = new ArrayList<Integer>();
				list.add(eo.getObjectId());
				map.put(key, list);
			}
		}
		
		return map;
	}
	
	public Integer calculateEdgeWeight(ArtObject o1, ArtObject o2) {
		
		Integer counter = 0;
		Map<Integer, List<Integer>> map = this.mapExhibitionObjects;
		
		for (Integer exhibition : map.keySet()) {
			
			List<Integer> listObjectsPerExhibition = map.get(exhibition);
			
			if (listObjectsPerExhibition.contains(o1.getId()) && listObjectsPerExhibition.contains(o2.getId())) {
				
				counter++;
			}
		}
		
		return counter;
	}
	
	public void createGraph() {
		
		Graphs.addAllVertices(this.graph, this.artObjectList);
		
		for (ArtObject o1 : this.artObjectList) {
			for (ArtObject o2 : this.artObjectList) {
				if (!o1.equals(o2) && !this.graph.containsEdge(o2, o1)){
					Integer edgeWeight = this.calculateEdgeWeight(o1, o2);
					if (edgeWeight != 0) {
						this.graph.addEdge(o1, o2);
						this.graph.setEdgeWeight(o2, o1, edgeWeight);
					}
				}
			}
		}
	}
	
	public void createGraphOptimized() {
		
		Graphs.addAllVertices(this.graph, this.artObjectIdMap.values());
		
		for (HelpingClass hc : this.helpingList) {
			
			ArtObject o1 = this.artObjectIdMap.get(hc.getObjectId1());
			ArtObject o2 = this.artObjectIdMap.get(hc.getObjectId2());
			Integer edgeWeight = hc.getCounter();
			
			if (!this.graph.containsEdge(o2, o1)){
				if (edgeWeight != 0) {
					this.graph.addEdge(o1, o2);
					this.graph.setEdgeWeight(o2, o1, edgeWeight);
				}
			}
		}
	}
	
	public Map<Integer, ArtObject> getArtObjectIdMap() {
		return artObjectIdMap;
	}

	public Graph<ArtObject, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public Integer calculateEdgeWeightTest(Integer id1, Integer id2) {
		
		Integer counter = 0;
		Map<Integer, List<Integer>> map = this.mapExhibitionObjects;
		
		for (Integer exhibition : map.keySet()) {
			
			List<Integer> listObjectsPerExhibition = map.get(exhibition);
			
			if (listObjectsPerExhibition.contains(id1) && listObjectsPerExhibition.contains(id2)) {
				
				counter++;
			}
		}
		
		return counter;
	}
	
	public List<ArtObject> componenteConnessa(ArtObject artObject){
		
		BreadthFirstIterator<ArtObject, DefaultWeightedEdge> visita = new BreadthFirstIterator<ArtObject, DefaultWeightedEdge>(this.graph, artObject);
		List<ArtObject> result = new ArrayList<ArtObject>();
		while (visita.hasNext()) {
			ArtObject ao = visita.next();
			result.add(ao);
		}
//		
//		List<Fermata> percorso = new ArrayList<Fermata>();
//		Fermata corrente = arrivo;
//		percorso.add(arrivo);
//		
//		DefaultEdge e = visita.getSpanningTreeEdge(corrente);
//		
//		
//		while (e != null) {
//			Fermata precedente = Graphs.getOppositeVertex(this.grafo, e, corrente);
//			percorso.add(precedente);
//			corrente = precedente;
//			e = visita.getSpanningTreeEdge(corrente);
//		}
		
		return result;
	
	}
}
