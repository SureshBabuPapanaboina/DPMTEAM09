#ifndef GRAPH_ADJ_LIST_H
#define GRAPH_ADJ_LIST_H

#include <vector>
#include <list>
#include <map>
#include <string>
#include <fstream>

using namespace std;

/*struct definitions*/

struct WikiPage {
  int ID;
  string title;
  string html_location;
  string txt_location;
};

struct Edge {
  int origin;
  int destination;
  int weight;
};

/* type definitions*/
typedef vector<list<Edge> > adjacencyList;
typedef map<int,WikiPage> idToWikiMap;


/*Re-writes from the first homework*/
void print(list<Edge>& lst);

adjacencyList organizeList(list<Edge>& lst, int numberVertices);

void printOrganized(adjacencyList& lst, idToWikiMap page_ofID);

/*New methods*/
idToWikiMap buildMap(list<WikiPage>& lst);

int countOccurences(ifstream& in_file, string string_to_count);

Edge createEdge(WikiPage& page_1, WikiPage& page_2);

list<Edge> createAllEdges(list<WikiPage>& pages);

void saveGraphToFile(adjacencyList& graph_to_save, ofstream& out_file);

list<Edge> readGraphFromFile(ifstream& in_file);


#endif
