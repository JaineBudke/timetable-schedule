package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class Engine {

	
	private ArrayList< ArrayList <String> > students; 
	private Graph graph;
	
	
	private static final Logger LOGGER = Logger.getLogger( Engine.class.getName() );

	
	/**
	 * Constrói objeto da classe Engine.
	 */	
	public Engine() {
		students = new ArrayList< ArrayList<String> >();
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	
	/**
	 * Lê arquivo de entrada e separa as atividades
	 * @param path Caminho do arquivo
	 */
	public void readArchive(String path) {

		
		try(FileReader arq = new FileReader(path);
			BufferedReader lerArq = new BufferedReader(arq);
			) {

			String linha = lerArq.readLine(); // lê a primeira linha
			

			while ( linha != null ) {
				ArrayList<String> lectures = new ArrayList<String>(); 
				
				
				String[] element = linha.split(" "); // separa a linha pelo espaçamento

				for( int i=0; i < element.length; i++ ){

					if( !element[i].equals("e") ){

						lectures.add(element[i]); // adiciona atividade
							
					}
					
				}

				students.add(lectures);
				
				linha = lerArq.readLine(); // lê a próxima linha
			}

		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}

	}
	
	
	/**
	 * Lê arquivo de entrada e separa as atividades
	 * @param index Indice do arquivo
	 */
	public void readArchivebyFilename(String filename) {


		File f = new File("");
		String absolutePath = f.getAbsolutePath();

		File folder = new File(absolutePath + "/src/data");
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		listOfFiles.sort(null);

		/**
		String filename = listOfFiles.get(0).getName();
		for (int i = 1; i <= index; i++) {
			filename = listOfFiles.get(i).getName();
		}**/
		
		
		this.readArchive(absolutePath+"/src/data/" + filename);
		
	}

	
	/**
	 * Cria grafo a partir de arquivo de texto:
	 * Vertices: aulas; Arestas: relação dada a aulas assistidas pelo mesmo aluno.
	 */
	public Graph createGraph(  ) {

		graph = new Graph();

		//System.out.println("Gerando grafo");
		
		for( int student=0; student < students.size(); student++ ){ // percorre lista de estudantes
			
			// lista com aulas do estudante analisado
			ArrayList<String> lecturesStudent = students.get(student);

			Vertex[] vertexStudent = new Vertex[lecturesStudent.size()];

			// adiciona vertices do estudante ao grafo
			// percorre aulas do estudante analisado
			for( int i=0; i < lecturesStudent.size(); i++ ){

				
				
				// cria um vértice com o rótulo da aula
				Vertex vertex = new Vertex( "v"+lecturesStudent.get(i) );
				// se não contém vertice
				if( graph.containsVertex(vertex) == null ){
					graph.addVertex(vertex);
					vertexStudent[i] = vertex;
					//LOGGER.info("Adicionado vertice");
				} else {
					vertexStudent[i] = graph.containsVertex(vertex);
				}

			}

			//LOGGER.info("Adiciona arestas");
			// adiciona arestas do estudante ao grafo
			for( int j=0; j < lecturesStudent.size(); j++ ){ // percorre todas as aulas
				for( int k=j+1; k < lecturesStudent.size(); k++ ){ // percorre as atividades a partir de j

					Vertex v1 = vertexStudent[j];
					Vertex v2 = vertexStudent[k];

					Edge edge = new Edge( v1, v2 );
					
					
					if( graph.containsEdge(edge) == false ) {
						graph.addEdge(edge);
						v1.addAdjacent(edge);
						v2.addAdjacent(edge);
						
					} else {
						// aumenta o peso da aresta
						//Edge e = graph.containsEdge(edge);
						//e.increaseWeight();
					}

				}

			}
			
		}
		
		return graph;
		

		
	}
	
	/**
	 * Gera um grafo com numberVertex nós
	 * @param numberVertex Numero de nós no grafo
	 * @param density Densidade do grafo (0,1)
	 * @return Grafo gerado
	 */
	public Graph generate(int numberVertex, double alpha) {

		Graph newGraph = new Graph();

		//preenche grafo
		for(Integer i = 0; i < numberVertex; i++) {
			Vertex v = new Vertex(  i.toString() );
			newGraph.addVertex(v);
		}

		long seed = System.nanoTime();
		Random ran = new Random(seed);
		
		for(Integer i = 0; i < numberVertex; i++) {
			for(Integer j = 0; j < numberVertex; j++) {
				if( i != j) {
					if(ran.nextDouble() < alpha) {
						Vertex v1 = newGraph.getVertex(i);
						Vertex v2 = newGraph.getVertex(j);
						newGraph.addEdge(v1, v2);
					}
				}
			}
		}

		return newGraph;
}
	
	
	
	
	
	
}
