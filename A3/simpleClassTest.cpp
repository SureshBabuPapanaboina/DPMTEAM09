#include "wikiClasses.h"

//change path if your wiki files are in a separate location
const string path = "./";

int main() {
    
    cout  << "Creating list of articles" << endl;
    
    WikiGraph wg;
    wg.push_page(make_wiki_page(path + "wiki/wp/n/New_York_City.htm",
                                path + "wiki/text/New_York_City.txt"));
    wg.push_page(make_wiki_page(path + "wiki/wp/c/Chicago.htm",
                                path + "wiki/text/Chicago.txt"));
    wg.push_page(make_wiki_page(path + "wiki/wp/t/Toronto.htm",
                                path + "wiki/text/Toronto.txt"));
    wg.push_page(make_wiki_page(path + "wiki/wp/m/Montreal.htm",
                                path + "wiki/text/Montreal.txt"));
    wg.push_page(make_wiki_page(path + "wiki/wp/m/Miami%2C_Florida.htm",
                                path + "wiki/text/Miami%2C_Florida.txt"));
    
    cout << "Testing the management of the graph" << endl;
    cout << "Here is the graph as organizing wikipedia pages" << endl;
    cout << "===============================================" << endl;
    cout << wg;
    cout << "===============================================" << endl;
    cout << "Here is the underlying abstract graph" << endl;
    cout << "=====================================" << endl;
    Graph g = wg;
    cout << g;
    cout << "=====================================" << endl;
    
    
    const string out_edge_file = "/Users/gcoman/out_graph.txt";
    const string out_wiki_file = "/Users/gcoman/out_wiki.txt";
    cout << endl << "Testing the file input and output:" << endl;
    ofstream ffo_edge; ffo_edge.open(out_edge_file);
    if(ffo_edge.fail()) { cout << "File " << out_edge_file << "could not be opened" << endl; return 1;}
    ofstream ffo_wiki; ffo_wiki.open(out_wiki_file);
    if(ffo_wiki.fail()) { cout << "File " << out_wiki_file << " could not be opened" << endl; return 1;}
    wg.save_to_output_files(ffo_edge, ffo_wiki);
    ffo_wiki.close();ffo_edge.close();
    
    ifstream ffi_edge; ffi_edge.open(out_edge_file);
    if(ffi_edge.fail()) { cout << "File " << out_edge_file << "could not be opened" << endl; return 1;}
    ifstream ffi_wiki; ffi_wiki.open(out_wiki_file);
    if(ffi_wiki.fail()) { cout << "File " << out_wiki_file << " could not be opened" << endl; return 1;}
    WikiGraph wg2(ffi_edge, ffi_wiki);
    ffi_edge.close();ffi_wiki.close();
    
    
    
    cout << "Here is the graph as organizing wikipedia pages" << endl;
    cout << "===============================================" << endl;
    cout << wg2;
    cout << "===============================================" << endl;
    cout << "Here is the underlying abstract graph" << endl;
    cout << "=====================================" << endl;
    Graph g2 = wg2;
    cout << g2;
    cout << "=====================================" << endl;
}
