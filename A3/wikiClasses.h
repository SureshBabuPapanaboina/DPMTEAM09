#ifndef comp322_hw3_wikiClasses_h
#define comp322_hw3_wikiClasses_h


#include <vector>
#include <list>
#include <map>
#include <string>
#include <fstream>
#include <iostream>
#include <set>

using namespace std;


/* Class that stores information about an abstract graph data structure */
class Graph {
public:
    /* public data */
    struct Edge {
        int origin;
        int destination;
        int weight;
    };
protected:
    /* private data */
    vector<list<Edge> > adj_list; //adjacency list
    vector<list<Edge> > get_adj_list() const {return adj_list;}
public:
    /*Constructors and Destructor*/
    Graph() { //default constructor
        list<Edge> l_dummy;
        adj_list.push_back(l_dummy);
    }
    Graph(Graph& other_graph){ //copy constuctor
        adj_list.resize(other_graph.adj_list.size());
        for (int i = 1; i < adj_list.size(); i++) {
            for(auto z : other_graph.adj_list[i]) {
                adj_list[i].push_back(z);
            }
        }
    }
    // same as organize list from previous homework
    Graph(list<Edge> lst, int num_vertices){
    	if (num_vertices == 0 ) return ;
    	adj_list.resize(num_vertices + 1);
    	for (auto &e : lst ){
    		adj_list.at(e.origin).push_back(e);
    		adj_list.at(e.destination).push_back(Edge {e.destination , e.origin, e.weight});
    	}
    }
    Graph(ifstream& in_file); // reads the list of edges from a file (same as last homework)
    ~Graph(); //destructor
    
    /*public methods*/
    unsigned int get_num_nodes() const { return static_cast<unsigned int>(adj_list.size() - 1);}
    
    /* push_node : adds an additional node to the graph: to do this,
     it adds the new list lst to the graph, as well as additional
     edges to corresponding lists (much like the constructor above) */
    void push_node(list<Edge>& lst);
    
    /* operator<< : same as print organized for abstract graphs */
    friend ostream& operator<< (ostream& o, Graph const& g);
    
    /* saveToFile : saves the edges of the graph, as explained in Homework 2*/
    void save_to_output_file(ofstream& o_edges) const;
};



/* WikiGraph : class that implements a graph of Wikipedia pages. It extends Graph
 privately, and provides interface methods for adding and removing pages from the graph.*/
class WikiGraph : public Graph {
private:
    // this is a map that returns the node in the abstract graph associated with the wikipage
    // with input title
    map<string,int> title_to_node;
    // this is a map that returns a WikiPage associated with a node in the abstract graph
    vector<WikiPage> node_to_wiki;
    
public:
    struct WikiPage {
        int ID;
        string title;
        string html_location;
        string txt_location;
    };
    
    /*Constructors and Destructor*/
    WikiGraph() { WikiPage wp_dummy; node_to_wiki.push_back(wp_dummy);} // default
    /* the constructor below reads a WikiGraph that was saved
     in two separate external files : PROVIDED BELOW */
    WikiGraph(WikiGraph& other_graph) : Graph(other_graph) {
        for (auto& x : other_graph.title_to_node) {
            title_to_node[x.first] = x.second;
        }
        node_to_wiki.resize(other_graph.node_to_wiki.size());
        for (int i=1; i < node_to_wiki.size(); i++) {
            node_to_wiki[i] = other_graph.node_to_wiki[i];
        }
    } // copy constructor
    WikiGraph(ifstream& in_edges, ifstream& in_wiki_info);
    ~WikiGraph(); // destructor
    
    /* operator<< : same as print organized for Wiki Graph */
    friend ostream& operator<< (ostream& o, WikiGraph const& wikiGraph);
    
    /* push_page : adds the WikiPage the the graph*/
    void push_page(WikiPage& wp);
    
    /* override push_node to make sure only WikiPages are added to the graph*/
    void push_node(list<Edge>& lst) { cout << "Can only push WikiPages in this graph" << endl;};
    
    /* saves the wiki graph info to two separate files: in one file we save the edges in the graph, like by line. The second output file contains the wikiPages, with all fields of the struct WikiPage on a separate line. Please see the definition of the WikiGraph(ifstream& in_edges, ifstream& in_title_to_node); constructor (BELOW) for more details */
    void save_to_output_files(ofstream& out_edges, ofstream& out_title_to_node) const;
    

};



/* make_wiki_page : reads one wikipage that has html source in path_to_html, and text source in path_to_txt. If one of HTML or TXT sources is missing, then the page should be ignored (return an empty WikiPage. */
WikiGraph::WikiPage& make_wiki_page(string path_to_html, string path_to_txt);



/********************************/
// Provided methods - facilies for working with the html and text files associated with wikipages
/********************************/


/* getPageTitle : extracts the title of wikipedia page from the HTML source */
string getPageTitle(ifstream& html_file);

/* allAssociations : returns all the values that are associated with the given 'field' in the string 'in_str' */
set<string> allAssociations(ifstream& f);

/* countOccurences : solution to the method in the previous homework */
int countOccurences(ifstream& in_file, string string_to_count);


#endif
